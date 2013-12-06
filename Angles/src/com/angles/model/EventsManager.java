package com.angles.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import android.content.Context;

import com.angles.angles.AnglesController;
import com.angles.database.EventTable;
import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery;
import com.google.cloud.backend.android.DBTableConstants;
import com.google.cloud.backend.android.F;

public class EventsManager {
	protected List<CloudEntity> results;
	protected User anglesUser;
	protected List<AnglesEvent> eventList;
	
	public EventsManager(User anglesUser, Context context)
	{
		this.anglesUser = anglesUser;
		loadEventsFromLocalDatabase(context);
		loadEventsFromCloud(context);
	}
	
	public void loadEventsFromLocalDatabase(Context context) {
		EventTable eventTable = new EventTable(context);
		eventList = eventTable.getEvents();
		eventList.add(new AnglesEvent("John's Wedding", "They grow up so fast", 
						makeCalendar(2013, 10, 3, 18, 0), makeCalendar(2014, 10, 15, 21, 0), 
						anglesUser, UUID.randomUUID()));
	}
		
	/**
	 * Load events from remote database for this user
	 */
	public void loadEventsFromCloud(Context context)
	{
		ContextThread thread = new ContextThread(context) {
			@Override
			public void run() {
		
				EventTable eventTable = new EventTable(threadContext);
				List<AnglesEvent> events = new ArrayList<AnglesEvent>();
				
				final CloudBackend cb = new CloudBackend();
				final CloudQuery cq = new CloudQuery("AnglesEvent");
				//Get events where user is host
				cq.setFilter(F.eq(DBTableConstants.DB_EVENT_HOST_USERNAME, anglesUser.userName));
		
				Thread hostThread = new Thread() {
					@Override
					public void run() {
						try {
							results = cb.list(cq);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				hostThread.start();
				try {
					hostThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (results != null && !results.isEmpty()) {
					for (int i=0; i < results.size(); i++) {
						CloudEntity entity = results.get(i);
						Calendar startTime = parseDateTime((String)entity.get(DBTableConstants.DB_EVENT_START_DATE),
								(String)entity.get(DBTableConstants.DB_EVENT_START_TIME));
						Calendar endTime = parseDateTime((String)entity.get(DBTableConstants.DB_EVENT_END_DATE),
								(String)entity.get(DBTableConstants.DB_EVENT_END_TIME));
						AnglesEvent hostedEvent = new AnglesEvent(
								(String)entity.get(DBTableConstants.DB_EVENT_TITLE),
								(String)entity.get(DBTableConstants.DB_EVENT_DESCRIPTION),
								startTime,
								endTime,
								anglesUser,
								UUID.fromString((String)entity.get(DBTableConstants.DB_EVENT_ID)));
						if (!eventList.contains(hostedEvent)) {
							events.add(hostedEvent);
							eventTable.addEvent(hostedEvent);
						}
					}
				}
				
				//Get events where user is guest
				final CloudQuery guestQuery = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
				guestQuery.setFilter(F.eq(DBTableConstants.DB_GUESTS_USERNAME, anglesUser.userName));
				
				Thread guestThread = new Thread() {
					@Override
					public void run() {
						try {
							results = cb.list(guestQuery);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				guestThread.start();
				try {
					guestThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if (results != null && !results.isEmpty()) {
					//find first event
					F filter=null;
					int index=0;
					while(index < results.size()) {
						CloudEntity eventEntity = results.get(index);
						if (!((String)eventEntity.get(DBTableConstants.DB_GUESTS_ATTENDING_STATUS)).equals("NOT_ATTENDING")) {
							filter = F.eq(DBTableConstants.DB_EVENT_ID, (String)eventEntity.get(DBTableConstants.DB_GUESTS_EVENT_ID));
							index++;
							break;
						}
						index++;
					}
					
					//add remaining events
					while(index < results.size()) {
						CloudEntity eventEntity = results.get(index);
						if (!((String)eventEntity.get(DBTableConstants.DB_GUESTS_ATTENDING_STATUS)).equals("NOT_ATTENDING")) {
							filter = F.or(filter, F.eq(DBTableConstants.DB_EVENT_ID, (String)eventEntity.get(DBTableConstants.DB_GUESTS_EVENT_ID)));
						}
						index++;
					}
					
					if (filter != null) {
						final CloudQuery eventQuery = new CloudQuery(DBTableConstants.DB_TABLE_ANGLES_EVENT);
						eventQuery.setFilter(filter);
						
						Thread eventThread = new Thread() {
							@Override
							public void run() {
								try {
									results = cb.list(eventQuery);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						};
						eventThread.start();
						try {
							eventThread.join();
						} 
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					else {
						results = null;
					}
				}
		
				if (results != null && !results.isEmpty()) {
					for (int i=0; i < results.size(); i++) {
						CloudEntity entity = results.get(i);
						Calendar startTime = parseDateTime((String)entity.get(DBTableConstants.DB_EVENT_START_DATE),
								(String)entity.get(DBTableConstants.DB_EVENT_START_TIME));
						Calendar endTime = parseDateTime((String)entity.get(DBTableConstants.DB_EVENT_END_DATE),
								(String)entity.get(DBTableConstants.DB_EVENT_END_TIME));
						AnglesEvent guestEvent = new AnglesEvent(
								(String)entity.get(DBTableConstants.DB_EVENT_TITLE),
								(String)entity.get(DBTableConstants.DB_EVENT_DESCRIPTION),
								startTime,
								endTime,
								new User((String)entity.get(DBTableConstants.DB_EVENT_HOST_USERNAME), ""),
								UUID.fromString((String)entity.get(DBTableConstants.DB_EVENT_ID)));
						if (!eventList.contains(guestEvent)) {
							events.add(guestEvent);
							eventTable.addEvent(guestEvent);
						}
					}
				}
				
				//get guests
				final CloudQuery addGuestsQuery = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
				F filter = null;
				
				if (events.size() > 0) {
					filter = F.eq(DBTableConstants.DB_GUESTS_EVENT_ID, events.get(0).getEventID().toString());
					for (int i = 1; i < events.size(); i++) {
						filter = F.or(filter, F.eq(DBTableConstants.DB_GUESTS_EVENT_ID, events.get(i).getEventID().toString()));
					}
					
					addGuestsQuery.setFilter(filter);
					Thread addGuestThread = new EventListThread(events, eventTable) {
						@Override
						public void run() {
							try {
								results = cb.list(addGuestsQuery);
								Map<UUID, Map<User, Attending>> doubleMap = new HashMap();
								for (int i=0; i < results.size(); i++) {
									UUID eventID = UUID.fromString((String)results.get(i).get(DBTableConstants.DB_GUESTS_EVENT_ID));
									if (!doubleMap.containsKey(eventID)) {
										doubleMap.put(eventID, new HashMap<User, Attending>());
									}
									User guest = new User((String)results.get(i).get(DBTableConstants.DB_GUESTS_USERNAME), "");
									String strStatus = (String)results.get(i).get(DBTableConstants.DB_GUESTS_ATTENDING_STATUS);
									Attending status;
									if (strStatus.equals("ATTENDING")) {
										status=Attending.ATTENDING;
									}
									else if (strStatus.equals("NOT_ATTENDING")) {
										status=Attending.NOT_ATTENDING;
									}
									else if (strStatus.equals("UNDECIDED")) {
										status=Attending.UNDECIDED;
									}
									else {
										status=Attending.MAYBE;
									}
									doubleMap.get(eventID).put(guest, status);
								}
								
								for (AnglesEvent event: events) {
									Map<User, Attending> map = doubleMap.get(event.getEventID());
									if (map == null) {
										map = new HashMap();
									}
									event.setGuests(map);
									eventTable.addGuests(event.getEventID().toString(), map);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					addGuestThread.start();
					try {
						addGuestThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if (events.size() > 0) {
					AnglesController.getInstance().reloadEvents(threadContext);
				}
			}
		};
		thread.start();
	}
	
	public List<AnglesEvent> getEventList(){
		
	    return eventList;
	}
	
	public void addEvent(AnglesEvent event)
	{
		eventList.add(event);
	}
	
	/**
	 * NOTE: Months are indexed from 0 
	 */
	public static Calendar makeCalendar(int year, int month, int day, int hour, int minute)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(year, month, day, hour, minute);
		return calendar;
	}
	
	public static String getDisplayDateTime(Calendar calendar)
	{
		return getDisplayDate(calendar) + " at " + getDisplayTime(calendar); 
	}
	
	public static String getDisplayDate(Calendar calendar)
	{
		return "" +
				(calendar.get(Calendar.MONTH) + 1) + "/" +
				calendar.get(Calendar.DAY_OF_MONTH)+ "/" + 
				calendar.get(Calendar.YEAR);
	}
	
	public static String getDisplayTime(Calendar calendar)
	{
		int hour = calendar.get(Calendar.HOUR_OF_DAY) % 12;
		if (hour == 0) {
			hour = 12;
		}
		return hour + ":" + 
			String.format("%02d", calendar.get(Calendar.MINUTE)) +
			(calendar.get(Calendar.HOUR_OF_DAY) < 12 ? " AM" : " PM");
	}

	public static Calendar parseDateTime(String date, String time) {
		Calendar dateCalendar = parseDate(date);
		Calendar timeCalendar = parseTime(time);
		
		dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
		dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
		return dateCalendar;
	}
	
	public static Calendar parseDate(String str) {
		Calendar calendar = Calendar.getInstance();
		String[] nums = str.split("/");
		
		try
		{
			calendar.set(Calendar.MONTH, Integer.parseInt(nums[0])-1);
			calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums[1]));
			calendar.set(Calendar.YEAR, Integer.parseInt(nums[2]));
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		
		return calendar;
	}
	
	public static Calendar parseTime(String str) 
	{
		Calendar calendar = Calendar.getInstance();
		
		try
		{
			String[] nums = str.split(":");
			String ampm = nums[1].split(" ")[1];
			nums[1] = nums[1].split(" ")[0];
			
			if (ampm.equals("PM") && !nums[0].equals("12")) {
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(nums[0]) + 12);
			}
			else if (ampm.equals("AM") && nums[0].equals("12")) {
				calendar.set(Calendar.HOUR_OF_DAY, 0);
			}
			else {
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(nums[0]));
			}
			calendar.set(Calendar.MINUTE, Integer.parseInt(nums[1]));
		}
		catch (NullPointerException e)
		{
			return null;
		}
		catch (NumberFormatException e)
		{
			return null;
		}
		
		return calendar;
	}
	
	public static String verifyNewEventData(String eventName, String eventDescription,
			Calendar startDate, Calendar endDate) {
		String result = "";
		
		if (eventName.length() == 0) {
			result += "Event must have a name\n";
		}
		else if (eventName.length() > 30) {
			result += "Event name must be less than 30 characters\n";
		}
		if (eventDescription.length() == 0) {
			result += "Event must have a description\n";
		}
		else if (eventDescription.length() > 500) {
			result += "Event description must be less than 500 characters\n";
		}
		if (startDate.compareTo(endDate) >= 0) {
			result += "The event must end after it begins\n";
		}
		if (startDate.compareTo(Calendar.getInstance()) <= 0) {
			result += "The event must start in the future\n";
		}
		
		return result;
	}
	
	public static Attending parseAttending(String status) {
		if (status.equals("ATTENDING")) {
			return Attending.ATTENDING;
		}
		else if (status.equals("NOT_ATTENDING")) {
			return Attending.NOT_ATTENDING;
		}
		else if (status.equals("MAYBE")) {
			return Attending.MAYBE;
		}
		else {
			return Attending.UNDECIDED;
		}
	}
	
	private class EventListThread extends Thread {
		protected List<AnglesEvent> events;
		protected EventTable eventTable;
		
		public EventListThread(List<AnglesEvent> events, EventTable eventTable) {
			this.events = events;
			this.eventTable = eventTable;
		}
	}
	
	private class ContextThread extends Thread {
		protected Context threadContext;
		
		public ContextThread(Context context) {
			this.threadContext = context;
		}
	}
}
