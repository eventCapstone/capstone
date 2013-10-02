package com.angles.model;

import java.util.List;
import java.util.UUID;

public class AnglesEvent {
	public String eventTitle;
	public String eventDescription;
	public String startTime;
	public String endTime;
	public boolean inviteIndicator;
	public List<User> attendees;
	public User host;
	public long eventID;
	
	public AnglesEvent(String eventTitle, User host, String startTime)
	{
		this.eventTitle = eventTitle;
		this.host = host;
		this.startTime = startTime;
	}
}
