package com.angles.angles;

import com.angles.view.AnglesDisplayManager;
import com.angles.view.AnglesTouchManager;

import android.app.Activity;


public class AnglesController {
	public static final String TAG = "AnglesController";
	
	
	AnglesDisplayManager itsDisplayManager;
	AnglesTouchManager  itsTouchManager;
	
	
	public AnglesController(Activity inActivity){
		itsDisplayManager = new AnglesDisplayManager(inActivity);
		itsTouchManager = new AnglesTouchManager(inActivity, this);	
	}
	
	public void homeEvent(){
		itsDisplayManager.displayHome();
		itsTouchManager.refresh();
	}
	
	public void eventListHomeEvent(){
		itsDisplayManager.displayEventListHome();
		
	}
	
	
}
