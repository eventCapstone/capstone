package com.angles.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;

import com.angles.angles.AnglesController;
import com.angles.database.EventTable;
import com.google.api.client.util.DateTime;
import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery;
import com.google.cloud.backend.android.DBTableConstants;
import com.google.cloud.backend.android.F;

public class AnglesEvent implements Serializable, Comparable {
	private String eventTitle;
	private String eventDescription;
	private Calendar startTime;
	private Calendar endTime;
	private User host;
	private UUID eventID;
	private Map<User, Attending> guests;
	
	/**
	 * Creates a new event initialized with one attendee - the host - who is ATTENDING
	 * @param eventTitle
	 * @param eventDescription
	 * @param startTime
	 * @param endTime
	 * @param host
	 * @param eventID
	 */
	public AnglesEvent(String eventTitle, String eventDescription, Calendar startTime, Calendar endTime,
			User host, UUID eventID, Map<User, Attending> guests)
	{
		
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.startTime = startTime;
		this.endTime = endTime;
		this.host = host;
		this.eventID = eventID;
		this.guests = guests;
	}
	
	public AnglesEvent(String eventTitle, String eventDescription, Calendar startTime, Calendar endTime,
			User host, UUID eventID)
	{
		this(eventTitle, eventDescription, startTime, endTime, host, eventID, new HashMap<User, Attending>());
	}
	public User getHost()
	{
		return host;
	}
	
	public UUID getEventID()
	{
		return eventID;
	}
	
	public Map<User, Attending> getGuests()
	{
		return guests;
	}
	
	public Attending getStatus(User user)
	{
		if (host.equals(user)) {
			return Attending.ATTENDING;
		}
		return guests.get(user);
	}
	
	
	
	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public void setHost(User host) {
		this.host = host;
	}

	public void setEventID(UUID eventID) {
		this.eventID = eventID;
	}

	public void acceptInvite(Context context)
	{
		if (guests.containsKey(AnglesController.getInstance().getAnglesUser()))
		{
			Thread thread = new ContextThread(context) {
				public void run() {
					//update guest table on cloud
					List<CloudEntity> result=null;
					User user = AnglesController.getInstance().getAnglesUser();
					CloudBackend cb = new CloudBackend();
					CloudQuery cq = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
					
					CloudEntity invite = new CloudEntity(DBTableConstants.DB_TABLE_GUESTS);
					invite.put(DBTableConstants.DB_GUESTS_EVENT_ID, eventID.toString());
					invite.put(DBTableConstants.DB_GUESTS_USERNAME, user.getUserName());
					invite.put(DBTableConstants.DB_GUESTS_ATTENDING_STATUS, "ATTENDING");
					
					try {
						cb.insert(invite);
					}
					catch (IOException e) {
						//don't update guest map if there was an error
						e.printStackTrace();
						return;
					}
					guests.put(AnglesController.getInstance().getAnglesUser(), Attending.ATTENDING);
					EventTable eventsTable = new EventTable(context);
					eventsTable.updateGuestStatus(user.getUserName(), eventID.toString(), "ATTENDING");		
				}
			};
			thread.start();
		}
	}
	
	public void declineInvite(Context context)
	{
		if (guests.containsKey(AnglesController.getInstance().getAnglesUser()))
		{
			Thread thread = new ContextThread(context) {
				public void run() {
					//update guest table on cloud
					List<CloudEntity> result=null;
					User user = AnglesController.getInstance().getAnglesUser();
					CloudBackend cb = new CloudBackend();
					CloudQuery cq = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
					
					CloudEntity invite = new CloudEntity(DBTableConstants.DB_TABLE_GUESTS);
					invite.put(DBTableConstants.DB_GUESTS_EVENT_ID, eventID.toString());
					invite.put(DBTableConstants.DB_GUESTS_USERNAME, user.getUserName());
					invite.put(DBTableConstants.DB_GUESTS_ATTENDING_STATUS, "NOT_ATTENDING");
					
					try {
						cb.insert(invite);
					}
					catch (IOException e) {
						//don't update guest map if there was an error
						e.printStackTrace();
						return;
					}
					guests.put(AnglesController.getInstance().getAnglesUser(), Attending.NOT_ATTENDING);
					EventTable eventsTable = new EventTable(context);
					eventsTable.updateGuestStatus(user.getUserName(), eventID.toString(), "NOT_ATTENDING");		
				}
			};
			thread.start();
		}
	}

	
	public void inviteGuests(Set<User> newGuests)
	{
		for (User user: newGuests)
		{
			//don't add if the user has already been invited
			if (guests.get(user) != null)
			{
				continue;
			}
			
			guests.put(user, Attending.UNDECIDED);
		}
	}
	
	public void uninviteGuest(User user)
	{
		guests.remove(user);
	}
	
	public void setGuests(Map<User, Attending> guests) {
		this.guests = guests;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof AnglesEvent) {
			return eventID.equals(((AnglesEvent)other).getEventID());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return eventID.hashCode();
	}

	@Override
	public int compareTo(Object otherEvent) {
		if (otherEvent instanceof AnglesEvent) {
			return (startTime.compareTo(((AnglesEvent)otherEvent).startTime));
		}
		return 0;
	}
	
	private class ContextThread extends Thread {
		protected Context context;
		
		public ContextThread(Context context) {
			this.context = context;
		}
	}
}
