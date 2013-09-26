package com.angles.model;

import java.util.Arrays;
import java.util.List;

public class EventsManager {
	
	private static List<AnglesEvent> eventList =
		Arrays.asList(new AnglesEvent[]
			{
				new AnglesEvent("John's Wedding"),
				new AnglesEvent("Guns 'n Roses Concert"),
				new AnglesEvent("My first dance recital")
			}
		);
		
	public static String getAValue(){
		return "OMG!";
	}
	
	public static List<AnglesEvent> getEventList(){
	    return eventList;
	}

}
