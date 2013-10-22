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
			User host, UUID eventID)
	{
		this.eventTitle = eventTitle;
		this.eventDescription = eventDescription;
		this.startTime = startTime;
		this.endTime = endTime;
		this.host = host;
		this.eventID = eventID;
		
		this.guests = new HashMap<User, Attending>();
		this.guests.put(host, Attending.ATTENDING);
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
		return guests.get(user);
	}
	
	public void acceptInvite(User user)
	{
		if (guests.containsKey(user))
		{
			guests.put(user, Attending.ATTENDING);
		}
	}
	
	public void declineInvite(User user)
	{
		if (guests.containsKey(user))
		{
			guests.put(user, Attending.NOT_ATTENDING);
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
}
