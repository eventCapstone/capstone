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
	
	private Activity itsActivity;
	private AnglesController anglesController;
	
	public AnglesDisplayManager(Activity inActivity, AnglesController anglesController){
		itsActivity = inActivity;
		this.anglesController = anglesController;
	}
	
	public void displaySettings() {
		itsActivity.setContentView(R.layout.angles_settings);
	}
	
	public void displayEventListHome(EventsManager eventsManager) {
		itsActivity.setContentView(R.layout.events_list);
		ListView theList = (ListView) itsActivity.findViewById(R.id.listOfEvents);
		theList.setAdapter(new EventsListAdapter(eventsManager.getEventList(), anglesController));
	}
	
	public void displayCreateAngle(){
		itsActivity.setContentView(R.layout.create_event);
		
	}
	
	public void displayEvent(AnglesEvent event)
	{
		itsActivity.setContentView(R.layout.events_view);
		TextView eventName = (TextView) itsActivity.findViewById(R.id.eventName);
		eventName.setText("Event Name: " + event.eventTitle);
		TextView host = (TextView) itsActivity.findViewById(R.id.host);
		host.setText("Hosted By: " + event.host.name);
		
		TextView startTime = (TextView) itsActivity.findViewById(R.id.startTime);
		startTime.setText("Start Time: " + EventsManager.getDisplayDateTime(event.startTime));
	}
	
	public void displayCreateAngleCompleted(){
		itsActivity.setContentView(R.layout.event_create_complete);
	}
	public void displayLogin(){
		itsActivity.setContentView(R.layout.login_screen);

		((LinearLayout) itsActivity.findViewById(R.id.loginUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) itsActivity.findViewById(R.id.loginPasswordGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) itsActivity.findViewById(R.id.signupUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) itsActivity.findViewById(R.id.signupEmailGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) itsActivity.findViewById(R.id.signupPasswordGroup)).setVisibility(View.INVISIBLE);
	}
	
	public void displayNewAccount(){
		itsActivity.setContentView(R.layout.new_account_page);
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
