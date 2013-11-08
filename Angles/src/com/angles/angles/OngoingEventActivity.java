package com.angles.angles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

public class OngoingEventActivity extends Activity {
	
	private static final String DEBUG_TAG = "OngoingEventsActivity";
	
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		AnglesController.getInstance().getDisplayManager().displayOngoingEventActivity(this);

   	 	AnglesController.getInstance().getTouchManager().setOngoingEventListeners(this);
   	}
	
	public void setPicture(byte[] picture) {
		
		/* Display a picture in the gallery */
	}
}