package com.angles.model;

import java.io.Serializable;
import java.util.List;

/**
 * A serializable Angles user object
 * @author Mike
 *
 */
public class User implements Serializable {
	private String userName;
	private String email;
	private String phoneNumber;
	
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
	
	
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof User)
		{
			return userName.equals(((User)other).userName);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return userName.hashCode();
	}
}
