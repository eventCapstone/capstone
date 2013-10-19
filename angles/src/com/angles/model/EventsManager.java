package com.angles.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

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
		
		events.add(new AnglesEvent("John's Wedding", "They grow up so fast", 
				makeCalendar(2013, 11, 15, 18, 0), makeCalendar(2013, 11, 15, 21, 0), 
				walterWhite, UUID.randomUUID()));
		events.add(new AnglesEvent("Guns 'n Roses Concert", "The new and improved edition",
				makeCalendar(2013, 10, 31, 22, 0), makeCalendar(2013, 11, 1, 2, 0),
				walterWhite, UUID.randomUUID()));
		events.add(new AnglesEvent("My first dance recital", "I learned to tap!",
				makeCalendar(2014, 1, 1, 14, 0), makeCalendar(2014, 1, 1, 16, 0),
				walterWhite, UUID.randomUUID()));
		
		return events;
	}
	
	public List<AnglesEvent> getEventList(){
		
	    return eventList;
	}
	
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
	
	public static Calendar parseDate(String str) {
		Calendar calendar = Calendar.getInstance();
		String[] nums = str.split("/");
		
		try
		{
			calendar.set(Calendar.MONTH, Integer.parseInt(nums[0]));
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
}
