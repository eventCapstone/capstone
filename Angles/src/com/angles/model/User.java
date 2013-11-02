package com.angles.model;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class User implements Serializable {
	
	public String firstname;
	
	public String lastname;
	
	public String username;
	
	public String phone;
	
	public String objectId;
	
	public String sessionToken;
	
	public String gravatarId;
	
	public String avatarUrl;
	
	public List<AnglesEvent> events;
	
	public User(String name) {
		
		this.username = username;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof User)
		{
			return (username.equals(((User)other).username));
		}
		return false;
	}
}
