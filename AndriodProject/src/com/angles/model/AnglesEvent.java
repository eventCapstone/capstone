package com.angles.model;

import java.util.List;
import java.util.UUID;

public class AnglesEvent {
	String itsEventTitle;
	String itsEventDescription;
	String itsStartTime;
	String itsEndTime;
	boolean itsInviteIndicator;
	List<User> itsAttendees;
	long itsID;
	
	public String getItsEventTitle() {
		return itsEventTitle;
	}

	public String getItsEventDescription() {
		return itsEventDescription;
	}

	public String getItsStartTime() {
		return itsStartTime;
	}

	public String getItsEndTime() {
		return itsEndTime;
	}

	public boolean isItsInviteIndicator() {
		return itsInviteIndicator;
	}

	public List<User> getItsAttendees() {
		return itsAttendees;
	}

	public long getItsID() {
		return itsID;
	}

	public AnglesEvent(String itsEventTitle)
	{
		this.itsEventTitle = itsEventTitle;
	}

}
