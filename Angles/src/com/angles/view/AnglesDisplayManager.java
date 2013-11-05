package com.angles.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;
import com.angles.model.Attending;
import com.angles.model.EventsManager;
import com.angles.model.User;

public class AnglesDisplayManager {
	public static final String KEY = "AnglesDisplayManager"; 
	
	private AnglesController anglesController;
	
	/**
	 * CONSTRUCTOR
	 * @param anglesController
	 */
	public AnglesDisplayManager(AnglesController anglesController){
		this.anglesController = anglesController;
	}
	
	/**
	 * INVITE LIST DISPLAY
	 * @param currentActivity
	 * @param event
	 */
	public void displayInviteList(Activity currentActivity, AnglesEvent event) {
		currentActivity.setContentView(R.layout.invite_list);
		ListView theList = (ListView)currentActivity.findViewById(R.id.contactList);
		theList.setAdapter(new InviteGuestListAdapter(event, anglesController, currentActivity));
	}
	
	/**
	 * GUEST LIST DISPLAY
	 * @param currentActivity
	 * @param event
	 */
	public void displayGuestList(Activity currentActivity, AnglesEvent event) {
		currentActivity.setContentView(R.layout.guest_list);
		
		List<String> attending = new ArrayList();
		List<String> invited = new ArrayList();
		Map<User, Attending> guests = event.getGuests();
		
		for (User user: guests.keySet()) {
			if (guests.get(user) == Attending.ATTENDING) {
				attending.add(user.userName);
			}
			else {
				invited.add(user.userName);
			}
		}
		
		ListView attendingList = (ListView)currentActivity.findViewById(R.id.attendingList);
		ListView invitedList = (ListView)currentActivity.findViewById(R.id.invitedList);
		
		attendingList.setAdapter(new ViewGuestListAdapter(currentActivity, attending));
		invitedList.setAdapter(new ViewGuestListAdapter(currentActivity, invited));
	}
	
	/**
	 * SETTINGS DISPLAY
	 * @param currentActivity
	 */
	public void displaySettings(Activity currentActivity) {
		currentActivity.setContentView(R.layout.angles_settings);
	}
	
	/**
	 * EVENT LIST DISPLAY
	 * @param currentActivity
	 * @param eventsManager
	 */
	public void displayEventList(Activity currentActivity, EventsManager eventsManager) {
		currentActivity.setContentView(R.layout.events_list);
		ListView theList = (ListView) currentActivity.findViewById(R.id.listOfEvents);
		theList.setAdapter(new EventsListAdapter(eventsManager.getEventList(), anglesController, currentActivity));
	}
	
	/**
	 * CREATE EVENT DISPLAY
	 * @param currentActivity
	 */
	public void displayCreateEvent(Activity currentActivity){
		currentActivity.setContentView(R.layout.create_event);
	}
	
	/**
	 * FUTURE EVENT DISPLAY
	 * @param currentActivity
	 * @param event
	 */
	public void displayFutureEvent(Activity currentActivity, AnglesEvent event)
	{
		currentActivity.setContentView(R.layout.events_view);
		
		User user = anglesController.getAnglesUser();
		if (!event.getHost().equals(user)) {
			currentActivity.findViewById(R.id.inviteGuestsButton).setVisibility(View.INVISIBLE);
		}
		
		Attending status = event.getGuests().get(user);
		if (status != Attending.UNDECIDED) {
			currentActivity.findViewById(R.id.acceptButton).setVisibility(View.INVISIBLE);
			currentActivity.findViewById(R.id.declineButton).setVisibility(View.INVISIBLE);
		}
		
		TextView eventName = (TextView) currentActivity.findViewById(R.id.eventName);
		eventName.setText(event.eventTitle);
		
		TextView host = (TextView) currentActivity.findViewById(R.id.host);
		host.setText("Hosted By: " + event.getHost().userName);
		
		TextView startTime = (TextView) currentActivity.findViewById(R.id.startTime);
		startTime.setText("Start Time: " + EventsManager.getDisplayDateTime(event.startTime));
		
		TextView description = (TextView) currentActivity.findViewById(R.id.eventDescription);
		description.setText(event.eventDescription);
	}
	
	/**
	 * ONGOING EVENT DISPLAY
	 * @param currentActivity
	 */
	public void displayOngoingEventActivity(Activity currentActivity) {
		currentActivity.setContentView(R.layout.ongoing_event_activity_main);
	}
	
	
	/**
	 * LOGIN OR REGISTER DISPLAY
	 * @param currentActivity
	 */
	public void displayLogin(Activity currentActivity){
		//Activity currentActivity = anglesController.getCurrentActivity();
		currentActivity.setContentView(R.layout.login_screen);

		((LinearLayout) currentActivity.findViewById(R.id.loginUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.loginPasswordGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupUserNameGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupEmailGroup)).setVisibility(View.INVISIBLE);
		((LinearLayout) currentActivity.findViewById(R.id.signupPasswordGroup)).setVisibility(View.INVISIBLE);
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
