package com.angles.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AnglesEvent implements Serializable {
	public String eventTitle;
	public String eventDescription;
	public Calendar startTime;
	public Calendar endTime;
	public boolean inviteIndicator;
	public List<User> invitees;
	public User host;
	public long eventID;
	
	public AnglesEvent(String eventTitle, User host, Calendar startTime)
	{
		this.eventTitle = eventTitle;
		this.host = host;
		this.startTime = startTime;
	}
	
	/* GETTERS */
	public String getEventTitle() {
		
		return eventTitle;
	}
	
	public String getEventDescription() {
		
		return eventDescription;
	}
	
	public long getStartTime() {
		
		return startTime.getTimeInMillis();
	}
	
	public long getEndTime() {
		
		return endTime.getTimeInMillis();
	}
	
	public boolean getInviteIndicator() {
		
		return inviteIndicator;
	}
	
	public List<User> getInvitees() {
		
		return invitees;
	}
	
	public User getHost() {
		
		return host;
	}
	
	public long getEventID() {
		
		return eventID;
	}
	
	/* SETTERS */
	public void setEventTile(String title) {
		
		eventTitle = title;
	}
	
	public void setEventDescription(String description) {
		
		eventDescription = description;
	}
	
	public void setStartTime(Calendar start) {
		
		startTime = start;
	}
	
	public void setEndTime(Calendar end) {
		
		endTime = end;
	}
	
	public void setInviteIndicator(boolean invite) {
		
		inviteIndicator = invite;
	}
	
	public void setInvitees(List<User> listInvitees) {
		
		invitees.clear();
		
		invitees.addAll(listInvitees);
	}
	
	public void setHost(User user) {
		
		host = user;
	}
	
	public void setEventID(long id) {
		
		eventID = id;
	}
}