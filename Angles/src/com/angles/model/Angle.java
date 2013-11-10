package com.angles.model;

import java.io.Serializable;
import java.util.Calendar;

import com.google.appengine.api.datastore.Blob;

public class Angle implements Serializable {
	private Blob image;
	private User createdBy;
	
	public Angle(byte[] data, User uploadedBy) {
		super();
		this.image = new Blob(data);
		this.createdBy = uploadedBy;
	}
	public Blob getImage() {
		return image;
	}
	public void setImage(Blob image) {
		this.image = image;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User uploadedBy) {
		this.createdBy = uploadedBy;
	}
}
