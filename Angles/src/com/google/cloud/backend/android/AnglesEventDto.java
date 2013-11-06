package com.google.cloud.backend.android;

@SuppressWarnings("javadoc")
public final class AnglesEventDto extends com.google.api.client.json.GenericJson {


	@com.google.api.client.util.Key
	  private java.lang.String eventId;

	  /**
	   * The value may be {@code null}.
	   */
	  @com.google.api.client.util.Key
	  private java.lang.String eventDesc;

	  /**
	   * The value may be {@code null}.
	   */
	  @com.google.api.client.util.Key
	  private java.lang.String eventName;

	  /**
	   * The value may be {@code null}.
	   */
	  @com.google.api.client.util.Key
	  private java.lang.String eventHostName;

	  /**
	   * The value may be {@code null}.
	   */
	  @com.google.api.client.util.Key
	  private java.lang.String kindName;

	  @com.google.api.client.util.Key
	  private java.lang.Object properties;
	  
	  public java.lang.String getEventId() {
		return eventId;
	}

	public void setEventId(java.lang.String eventId) {
		this.eventId = eventId;
	}

	public java.lang.String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(java.lang.String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public java.lang.String getEventName() {
		return eventName;
	}

	public void setEventName(java.lang.String eventName) {
		this.eventName = eventName;
	}

	public java.lang.String getEventHostName() {
		return eventHostName;
	}

	public void setEventHostName(java.lang.String eventHostName) {
		this.eventHostName = eventHostName;
	}


	public java.lang.String getKindName() {
		return kindName;
	}

	public void setKindName(java.lang.String kindName) {
		this.kindName = kindName;
	}
	/**
	   * @return value or {@code null} for none
	   */
	  public java.lang.Object getProperties() {
	    return properties;
	  }

	  /**
	   * @param properties properties or {@code null} for none
	   */
	  public AnglesEventDto setProperties(java.lang.Object properties) {
	    this.properties = properties;
	    return this;
	  }
	  @Override
	  public AnglesEventDto set(String fieldName, Object value) {
	    return (AnglesEventDto) super.set(fieldName, value);
	  }

	  @Override
	  public AnglesEventDto clone() {
	    return (AnglesEventDto) super.clone();
	  }
}
