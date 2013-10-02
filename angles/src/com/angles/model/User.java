package com.angles.model;

import java.util.List;

public class User {
	public String name;
	public List<AnglesEvent> events;
	
	public User(String name) {
		this.name = name;
	}
}
