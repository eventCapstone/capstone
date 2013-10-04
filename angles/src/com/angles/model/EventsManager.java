package com.angles.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class EventsManager {
	private List<AnglesEvent> eventList;
	
	public EventsManager()
	{
		eventList = loadEventsFromDatabase();
	}
		
	/**
	 * TODO: Load events from remote database for this user
	 */
	public List<AnglesEvent> loadEventsFromDatabase()
	{
		User walterWhite = new User("Walter White");
		List events = new ArrayList<AnglesEvent>();
		
		events.add(new AnglesEvent("John's Wedding", walterWhite, 
			makeCalendar(2013, 11, 15, 18, 0)));
		events.add(new AnglesEvent("Guns 'n Roses Concert", walterWhite, 
			makeCalendar(2013, 10, 31, 22, 0)));
		events.add(new AnglesEvent("My first dance recital", walterWhite, 
			makeCalendar(2014, 1, 1, 14, 0)));
		
		return events;
	}
	
	public List<AnglesEvent> getEventList(){
	    return eventList;
	}
	
	public Calendar makeCalendar(int year, int month, int day, int hour, int minute)
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(year, month, day, hour, minute);
		return calendar;
	}
	
	public static String getDisplayDateTime(Calendar calendar)
	{
		int hour = calendar.get(Calendar.HOUR_OF_DAY) % 12;
		if (hour == 0) {
			hour = 12;
		}
		String hourAndMinute = hour + ":" + 
			String.format("%02d", calendar.get(Calendar.MINUTE)) +
			(calendar.get(Calendar.HOUR_OF_DAY) < 12 ? "AM" : "PM");
		return "" +
			calendar.get(Calendar.MONTH) + "/" +
			calendar.get(Calendar.DAY_OF_MONTH)+ "/" + 
			calendar.get(Calendar.YEAR) + " at " +
			hourAndMinute;
	}
}
