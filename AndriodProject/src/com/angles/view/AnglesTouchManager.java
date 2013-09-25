package com.angles.view;


import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.angles.angles.AnglesController;
import com.angles.angles.R;

public class AnglesTouchManager {
	public static final String KEY = "AnglesTouchManager"; 
	
	private Activity itsActivity;
	private AnglesController itsAnglesController;
	
	
	
	public AnglesTouchManager(Activity inActivity, AnglesController inController){
		itsActivity = inActivity;
		itsAnglesController = inController;
	}
	
	public void refresh(){
		
			Button button = (Button) itsActivity.findViewById(R.id.homeEventsButton);
			button.setOnClickListener(goToEventsList);
		
	}

	private OnClickListener goToEventsList = new OnClickListener() {
		public void onClick(View v) {
			itsAnglesController.eventListHomeEvent();
		}
		
	};
	
	
	
	
	
//	 private OnClickListener onClickListener = new OnClickListener() { 
//	    	public void onClick(final View v) {
//			setContentView(R.layout.events_list);
//			
//			
//	    	
//
//	    }
//	 };
}
