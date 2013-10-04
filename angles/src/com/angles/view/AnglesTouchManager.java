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
	
	public void setLoginPageListeners(){
		Button login = (Button) itsActivity.findViewById(R.id.login_button);
		login.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
		Button newUser = (Button) itsActivity.findViewById(R.id.new_account_button);
		newUser.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.newAccountEvent();;
			}
		});
	}
	
	public void setEventsHomeListeners() 
	{
		Button createAngleButton = (Button) itsActivity.findViewById(R.id.create_angle_button);
		createAngleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventCreateAnglesEvent();
				
			}
		});
		
		Button settingsButton = (Button) itsActivity.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.changeSettings();
			}
		});
	}
	
	public void setSettingsListeners()
	{
		Button saveSettings = (Button) itsActivity.findViewById(R.id.saveSettingsButton);
		saveSettings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
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
	
	public void setCreateAngleListenders(){
		Button inviteGuests = (Button) itsActivity.findViewById(R.id.new_angle_invite_guests);
		inviteGuests.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventAngleCreateCompleted();
			}
		});
	}
	
	public void setCreateNewAccountListeners(){
		Button submit = (Button) itsActivity.findViewById(R.id.submit_button);
		submit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
	}
	
	public void setAngleCompleteListeners(){
		Button gotIt = (Button) itsActivity.findViewById(R.id.completed_angle_button);
		gotIt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				itsAnglesController.eventListHomeEvent();
			}
		});
	}
	
	
	
}
