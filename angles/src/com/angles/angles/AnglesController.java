package com.angles.angles;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;
import com.angles.view.AnglesDisplayManager;
import com.angles.view.AnglesTouchManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
