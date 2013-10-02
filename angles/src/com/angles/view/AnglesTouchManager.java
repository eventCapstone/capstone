package com.angles.view;


import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;

public class AnglesTouchManager {
	public static final String KEY = "AnglesTouchManager"; 
	
	private Activity itsActivity;
	private AnglesController itsAnglesController;
	
	
	
	public AnglesTouchManager(Activity inActivity, AnglesController inController) 
	{
		itsActivity = inActivity;
		itsAnglesController = inController;
	}
	
	public void setHomePageListeners() 
	{
		Button button = (Button) itsActivity.findViewById(R.id.homeEventsButton);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
	}
	
	public void setEventsHomeListeners() 
	{
	}
	
	public void setEventDisplayListeners()
	{
		Button otherAnglesButton = (Button) itsActivity.findViewById(R.id.otherAnglesButton);
		otherAnglesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
	}
}
