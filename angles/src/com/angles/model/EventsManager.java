package com.angles.model;

import java.util.Arrays;
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
		
		return Arrays.asList(new AnglesEvent[]
			{
				new AnglesEvent("John's Wedding", walterWhite, "6:00pm"),
				new AnglesEvent("Guns 'n Roses Concert", walterWhite, "9:00pm"),
				new AnglesEvent("My first dance recital", walterWhite, "4:00pm")
			});
	}
	
	public List<AnglesEvent> getEventList(){
	    return eventList;
	}

}
