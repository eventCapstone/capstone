package com.angles.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;

public class AnglesDisplayManager {
	public static final String KEY = "AnglesDisplayManager"; 
	
	private AnglesController anglesController;
	
	public AnglesDisplayManager(AnglesController anglesController){
		this.anglesController = anglesController;
	}
	
	public void displaySettings(Activity currentActivity) {
		currentActivity.setContentView(R.layout.angles_settings);
	}
	
	public void displayEventListHome(Activity currentActivity, EventsManager eventsManager) {
		currentActivity.setContentView(R.layout.events_list);
		ListView theList = (ListView) currentActivity.findViewById(R.id.listOfEvents);
		theList.setAdapter(new EventsListAdapter(eventsManager.getEventList(), anglesController, currentActivity));
	}
	
	public void displayCreateEvent(Activity currentActivity){
		currentActivity.setContentView(R.layout.create_event);
	}
	
	public void displayEvent(Activity currentActivity, AnglesEvent event)
	{
		currentActivity.setContentView(R.layout.events_view);
		TextView eventName = (TextView) currentActivity.findViewById(R.id.eventName);
		eventName.setText("Event Name: " + event.eventTitle);
		TextView host = (TextView) currentActivity.findViewById(R.id.host);
		host.setText("Hosted By: " + event.getHost().name);
		
		TextView startTime = (TextView) currentActivity.findViewById(R.id.startTime);
		startTime.setText("Start Time: " + EventsManager.getDisplayDateTime(event.startTime));
	}
	
	public void displayCreateAngleCompleted(Activity currentActivity){
		currentActivity.setContentView(R.layout.event_create_complete);
	}
	
	public void displayLogin(Activity currentActivity){
		//Activity currentActivity = anglesController.getCurrentActivity();
		currentActivity.setContentView(R.layout.login_screen);

		((LinearLayout) currentActivity.findViewById(R.id.loginUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.loginPasswordGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupEmailGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupPasswordGroup)).setVisibility(View.INVISIBLE);
	}
	
	public void displayNewAccount(Activity currentActivity){
		currentActivity.setContentView(R.layout.new_account_page);
	}
	
	public void displayOngoingEventActivity(Activity currentActivity) {
		
		currentActivity.setContentView(R.layout.ongoing_event_activity_main);
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {
	
	    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
	
	    public StableArrayAdapter(Context context, int textViewResourceId,
	        List<String> objects) {
	      super(context, textViewResourceId, objects);
	      for (int i = 0; i < objects.size(); ++i) {
	        mIdMap.put(objects.get(i), i);
	      }
	    }
	 
	}
}
