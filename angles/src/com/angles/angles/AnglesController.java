package com.angles.angles;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;
import com.angles.model.User;
import com.angles.view.AnglesDisplayManager;
import com.angles.view.AnglesTouchManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AnglesController {
	
	private static AnglesController controllerInstance;
	
	public static final String TAG = "AnglesController";
	
	AnglesDisplayManager itsDisplayManager;
	
	AnglesTouchManager  itsTouchManager;
	
	EventsManager eventsManager;
	
	private AnglesController(Activity inActivity){
		itsDisplayManager = new AnglesDisplayManager(this);
		itsTouchManager = new AnglesTouchManager(this);
		eventsManager = new EventsManager();
	}
	
	public static void createInstance(Activity inActivity)
	{
		controllerInstance = new AnglesController(inActivity);
	}
	
	public static AnglesController getInstance()
	{
		return controllerInstance;
	}
	
	public AnglesDisplayManager getDisplayManager()
	{
		return itsDisplayManager;
	}
	
	public AnglesTouchManager getTouchManager()
	{
		return itsTouchManager;
	}
	
	public EventsManager getEventsManager()
	{
		return eventsManager;
	}
	
	/**
	 * TODO: Implement login
	 */
	public void loginUser(Activity currentActivity)
	{
		//eventListHomeEvent(currentActivity);
		
		ongoingEvent(currentActivity);
	}
	
	/**
	 * TODO: Implement register new user
	 */
	public void registerUser(Activity currentActivity)
	{
		eventListHomeEvent(currentActivity);	
	}
	
	public void createEvent(Activity currentActivity)
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
		
		startDate.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY));
		startDate.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		endDate.set(Calendar.HOUR_OF_DAY, endTime.get(Calendar.HOUR_OF_DAY));
		endDate.set(Calendar.MINUTE, endTime.get(Calendar.MINUTE));
		
		User user = new User("Pennywise");
		
		String result = EventsManager.verifyNewEventData(eventName, eventDescription, startDate, endDate);
		if (result.equals("")) {
			AnglesEvent event = new AnglesEvent(eventName, eventDescription,
				startDate, endDate, user, UUID.randomUUID());
			eventsManager.addEvent(event);
			
			Toast.makeText(currentActivity, "Event created!", Toast.LENGTH_SHORT).show();
			viewEvent(currentActivity, event);
		} else {
			Toast.makeText(currentActivity, result, Toast.LENGTH_LONG).show();
		}
	}
	
	public void loginEvent(Activity currentActivity){
		Intent intent = new Intent(currentActivity, LoginActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void eventListHomeEvent(Activity currentActivity){
		Intent intent = new Intent(currentActivity, EventsListActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void viewEvent(Activity currentActivity, AnglesEvent event)
	{
		Intent intent = new Intent(currentActivity, EventItemActivity.class);
		intent.putExtra("event", event);
		currentActivity.startActivity(intent);
	}
	
	public void changeSettings(Activity currentActivity) {
		Intent intent = new Intent(currentActivity, SettingsActivity.class);
		currentActivity.startActivity(intent);
	}
	
	public void eventCreateAnglesEvent(Activity currentActivity){
		Intent intent = new Intent(currentActivity, CreateEventActivity.class);
		currentActivity.startActivity(intent);
	}

	public void newAccountEvent(Activity currentActivity) {
		//makeNewActivity();
		itsDisplayManager.displayNewAccount(currentActivity);
		itsTouchManager.setCreateNewAccountListeners(currentActivity);
	}

	public void eventAngleCreateCompleted(Activity currentActivity) {
		///makeNewActivity();
		itsDisplayManager.displayCreateAngleCompleted(currentActivity);
		itsTouchManager.setAngleCompleteListeners(currentActivity);
	} 
	
	public void ongoingEvent(Activity currentActivity) {
		
		Intent intent = new Intent(currentActivity, OngoingEventActivity.class);
				
		currentActivity.startActivity(intent);
	}
	
	public void cameraEvent(Activity currentActivity) {
		
		Intent intent = new Intent(currentActivity, CameraActivity.class);
		
		currentActivity.startActivity(intent);
	}
}
