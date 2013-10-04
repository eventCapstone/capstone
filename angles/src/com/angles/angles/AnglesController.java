package com.angles.angles;

import com.angles.model.AnglesEvent;
import com.angles.view.AnglesDisplayManager;
import com.angles.view.AnglesTouchManager;

import android.app.Activity;


public class AnglesController {
	public static final String TAG = "AnglesController";
	
	
	AnglesDisplayManager itsDisplayManager;
	AnglesTouchManager  itsTouchManager;
	
	
	public AnglesController(Activity inActivity){
		itsDisplayManager = new AnglesDisplayManager(inActivity, this);
		itsTouchManager = new AnglesTouchManager(inActivity, this);	
	}
	
	public void loginEvent(){
		itsDisplayManager.displayLogin();
		itsTouchManager.setLoginPageListeners();
	}
	
	public void eventListHomeEvent(){
		itsDisplayManager.displayEventListHome();
		itsTouchManager.setEventsHomeListeners();
		//touch listeners currently implemented in EventListAdapter
	}
	
	public void viewEvent(AnglesEvent event)
	{
		itsDisplayManager.displayEvent(event);
		itsTouchManager.setEventDisplayListeners();
	}
	
	public void changeSettings() {
		itsDisplayManager.displaySettings();
		itsTouchManager.setSettingsListeners();
	}
	
	public void eventCreateAnglesEvent(){
		itsDisplayManager.displayCreateAngle();
		itsTouchManager.setCreateAngleListenders();
	}

	public void newAccountEvent() {
		itsDisplayManager.displayNewAccount();
		itsTouchManager.setCreateNewAccountListeners();
		
	}

	public void eventAngleCreateCompleted() {
		itsDisplayManager.displayCreateAngleCompleted();
		itsTouchManager.setAngleCompleteListeners();
		
	}
	
}
