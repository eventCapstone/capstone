package com.angles.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	public String userName;
	public String email;
	public String phoneNumber;
	
	public User(String userName, String email) {
		this.userName = userName;
		this.email = email;
		this.phoneNumber = "";
	}
	
	public User(String userName, String email, String phoneNumber) {
		this.userName = userName;
		this.email = email;
		this.phoneNumber = phoneNumber;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof User)
		{
			return (userName.equals(((User)other).userName));
		}
		return false;
	}
}
