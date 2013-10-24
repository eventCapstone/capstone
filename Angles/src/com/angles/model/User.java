package com.angles.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
	public String name;
	public List<AnglesEvent> events;
	
	public User(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other instanceof User)
		{
			return (name.equals(((User)other).name));
		}
		return false;
	}
}
