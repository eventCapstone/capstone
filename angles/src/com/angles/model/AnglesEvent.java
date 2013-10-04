package com.angles.model;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.google.api.client.util.DateTime;

public class AnglesEvent {
	public String eventTitle;
	public String eventDescription;
	public Calendar startTime;
	public Calendar endTime;
	public boolean inviteIndicator;
	public List<User> attendees;
	public User host;
	public long eventID;
	
	public AnglesEvent(String eventTitle, User host, Calendar startTime)
	{
		this.eventTitle = eventTitle;
		this.host = host;
		this.startTime = startTime;
	}
}
