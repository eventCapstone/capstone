package com.angles.angles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.angles.database.ContactDbHelper;
import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;
import com.angles.model.User;
import com.angles.view.AnglesDisplayManager;
import com.angles.view.AnglesTouchManager;
import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery;
import com.google.cloud.backend.android.DBTableConstants;
import com.google.cloud.backend.android.F;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AnglesController {
	private static AnglesController controllerInstance;
	public static final String TAG = "AnglesController";
	
	private AnglesDisplayManager itsDisplayManager;
	private AnglesTouchManager  itsTouchManager;
	private EventsManager eventsManager;
	
	private User anglesUser;
	private Set<User> contacts;
	private List<CloudEntity> result;
	
	/**
	 * Constructor that creates the touch manager, display manager, and events manager, and loads the
	 * anglesUser instance.
	 * 
	 * Also populates the set of contacts
	 * @param inActivity the activity responsible for creating the controller
	 */
	private AnglesController(Activity inActivity){
		anglesUser = new User("Walter White", "walter@breakingbad.com");
		eventsManager = new EventsManager(anglesUser);
		itsDisplayManager = new AnglesDisplayManager(this);
		itsTouchManager = new AnglesTouchManager(this);
		
//		ContactDbHelper helper = new ContactDbHelper(inActivity);
//		contacts = helper.getContacts();
		contacts = new HashSet();
		contacts.add(new User("Joey Ramone", "joey@ramone.com", "6127536755"));
		contacts.add(new User("Edward Snowden", "wiki@leaks.com", ""));
		contacts.add(new User("Santa Clause", "santa@northpole.com", ""));
		contacts.add(new User("Katy Perry", "roar@ikissedagirl.com", "8082224444"));
	}
	
	/**
	 * Creates the anglesController singleton
	 * @param inActivity the activity that wants access to the controller
	 */
	public static void createInstance(Activity inActivity)
	{
		controllerInstance = new AnglesController(inActivity);
	}
	
	/**
	 * Returns the anglesController singleton
	 * @return the singleton instance
	 */
	public static AnglesController getInstance()
	{
		return controllerInstance;
	}
	
	/*****************************************************************************
	 * GETTERS && SETTERS
	 *****************************************************************************/
	public User getAnglesUser() {
		return anglesUser;
	}
	
	public AnglesDisplayManager getDisplayManager() {
		return itsDisplayManager;
	}
	
	public AnglesTouchManager getTouchManager() {
		return itsTouchManager;
	}
	
	public EventsManager getEventsManager() {
		return eventsManager;
	}
	
	public Set<User> getContacts() {
		return contacts;
	}

	/*****************************************************************************
	 * LOGIN Business Logic
	 *****************************************************************************/
	public void loginUser(Activity currentActivity)
	{
		String UserName = ((EditText) currentActivity.findViewById(R.id.loginUserName)).getText().toString();
		String PW = ((EditText) currentActivity.findViewById(R.id.loginPassword)).getText().toString();
		
	
		final CloudBackend cb = new CloudBackend();
		final CloudQuery cq = new CloudQuery(DBTableConstants.DB_USERS_USERSTABLENAME);
		
		cq.setFilter(F.and(F.eq(DBTableConstants.DB_USERS_USERNAME,UserName), F.eq(DBTableConstants.DB_USERS_PASSWORD, PW)));
		cq.setLimit(1);

		Thread theThread = new Thread() {
			@Override
			public void run() {
				try {
					result = cb.list(cq);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		theThread.start();
		
		try {
			theThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (result.isEmpty()) {
			Toast.makeText(currentActivity,"Wrong UserName Or Password", Toast.LENGTH_LONG).show();
		} else {
			loadEventListActivity(currentActivity);
		}
	}
	
	/**
	 * TODO: Implement register new user
	 */
	public void registerUser(Activity currentActivity)
	{
		EditText UserName = (EditText) currentActivity.findViewById(R.id.signupUserName);
		EditText Email = (EditText) currentActivity.findViewById(R.id.signupEmail);
		
		final CloudBackend cb = new CloudBackend();
		final CloudQuery cq = new CloudQuery(DBTableConstants.DB_USERS_USERSTABLENAME);
		
		cq.setFilter(F.or(F.eq(DBTableConstants.DB_USERS_USERNAME,UserName.getText().toString()), F.eq(DBTableConstants.DB_USERS_EMAIL, Email.getText().toString().toLowerCase())));
		cq.setLimit(1);
Thread theThread = new Thread() {
			
		@Override
		public void run() {
			try {
				result = cb.list(cq);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		};
		theThread.start();
		try {
			theThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.isEmpty()){
			
			Button createNewUser = (Button) currentActivity.findViewById(R.id.signup_button);
			((LoginActivity) currentActivity).createNewUser(createNewUser);
			Toast.makeText(currentActivity,"Account created,Welcome to Angles!", Toast.LENGTH_LONG).show();
			loadEventListActivity(currentActivity);	
		}else{
			
			Toast.makeText(currentActivity,"Username is taken or Email already registered", Toast.LENGTH_LONG).show();
		}
	}
	
	
	
	/*****************************************************************************
	 * CREATE EVENT Business Logic
	 *****************************************************************************/
	public void createEvent(CreateEventActivity currentActivity)
	{
		String eventName = 
			((TextView)currentActivity.findViewById(R.id.eventNameInput)).getText().toString();
		String eventDescription = 
			((TextView)currentActivity.findViewById(R.id.eventDescriptionInput)).getText().toString();
		Calendar startDate = EventsManager.parseDate(
				((Button)currentActivity.findViewById(R.id.startDateButton)).getText().toString());
		Calendar startTime = EventsManager.parseTime(
			((Button)currentActivity.findViewById(R.id.startTimeButton)).getText().toString());
		Calendar endDate = EventsManager.parseDate(
				((Button)currentActivity.findViewById(R.id.endDateButton)).getText().toString());
		Calendar endTime = EventsManager.parseTime(
			((Button)currentActivity.findViewById(R.id.endTimeButton)).getText().toString());
		Button submitButton = (Button)currentActivity.findViewById(R.id.submitNewEventButton);
		startDate.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
		startDate.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
		
		String result = EventsManager.verifyNewEventData(eventName, eventDescription, startDate, endDate);
		if (result.equals("")) {
			AnglesEvent event = new AnglesEvent(eventName, eventDescription,
				startDate, endDate, anglesUser, UUID.randomUUID());
			eventsManager.addEvent(event);
			
			Toast.makeText(currentActivity, "Event created!", Toast.LENGTH_SHORT).show();
			currentActivity.sendEventToCloud(submitButton);
			loadViewEventActivity(currentActivity, event);
		} else {
			Toast.makeText(currentActivity, result, Toast.LENGTH_LONG).show();
		}
	}
	
	/*****************************************************************************
	 * ACTIVITY LOADERS
	 *****************************************************************************/
	public void loadLoginActivity(Activity currentActivity){
		Intent intent = new Intent(currentActivity, LoginActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void loadEventListActivity(Activity currentActivity){
		Intent intent = new Intent(currentActivity, EventsListActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void loadInviteListActivity(Activity currentActivity, AnglesEvent event) {
		Intent intent = new Intent(currentActivity, InviteListActivity.class);
		intent.putExtra("event", event);
		currentActivity.startActivity(intent);
	}

	public void loadGuestListActivity(Activity currentActivity, AnglesEvent event) {
		Intent intent = new Intent(currentActivity, GuestListActivity.class);
		intent.putExtra("event", event);
		currentActivity.startActivity(intent);
	}
	
	public void loadViewEventActivity(Activity currentActivity, AnglesEvent event)
	{
		Intent intent = new Intent(currentActivity, FutureEventActivity.class);
		intent.putExtra("event", event);
		currentActivity.startActivity(intent);
	}
	
	public void loadChangeSettingsActivity(Activity currentActivity) {
		Intent intent = new Intent(currentActivity, SettingsActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void loadCreateEventActivity(Activity currentActivity){
		Intent intent = new Intent(currentActivity, CreateEventActivity.class);
		currentActivity.startActivity(intent);
	}

	public void loadOngoingEventActivity(Activity currentActivity) {
		Intent intent = new Intent(currentActivity, OngoingEventActivity.class);
		currentActivity.startActivity(intent);
	}
}
