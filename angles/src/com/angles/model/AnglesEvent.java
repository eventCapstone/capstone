package com.angles.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.api.client.util.DateTime;

public class AnglesEvent implements Serializable {
	public String eventTitle;
	public String eventDescription;
	public Calendar startTime;
	public Calendar endTime;
	
	private User host;
	private UUID eventID;
	private Map<User, Attending> attendees;
	
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
			User host, UUID eventID)
	{
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.startTime = startTime;
		this.endTime = endTime;
		this.host = host;
		this.eventID = eventID;
		
		this.attendees = new HashMap();
		this.attendees.put(host, Attending.ATTENDING);
	}
	
	public User getHost()
	{
		return host;
	}
	
	public UUID getEventID()
	{
		return eventID;
	}
	
	public Map<User, Attending> getAttendees()
	{
		return attendees;
	}
	
	public void inviteUsers(Set<User> newInvitees)
	{
		for (User user: newInvitees)
		{
			//don't add if the user has already been invited
			if (attendees.get(user) != null)
			{
				continue;
			}
			
			attendees.put(user, Attending.UNDECIDED);
		}
	}
	
	public void uninviteUser(User user)
	{
		attendees.remove(user);
	}
}
