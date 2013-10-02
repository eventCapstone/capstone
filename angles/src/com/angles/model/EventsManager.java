package com.angles.model;

import java.util.Arrays;
import java.util.List;

public class EventsManager {
	private static User walterWhite = new User("Walter White");
	
	private static List<AnglesEvent> eventList =
		Arrays.asList(new AnglesEvent[]
			{
				new AnglesEvent("John's Wedding", walterWhite, "6:00pm"),
				new AnglesEvent("Guns 'n Roses Concert", walterWhite, "9:00pm"),
				new AnglesEvent("My first dance recital", walterWhite, "4:00pm")
			}
		);
		
	public static String getAValue(){
		return "OMG!";
	}
	
	public static List<AnglesEvent> getEventList(){
	    return eventList;
	}

}
