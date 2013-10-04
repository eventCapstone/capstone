package com.angles.view;


import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
				((LinearLayout) itsActivity.findViewById(R.id.loginUserNameGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) itsActivity.findViewById(R.id.loginPasswordGroup)).setVisibility(View.VISIBLE);
				v.setOnClickListener(new OnClickListener() {
					public void onClick(View v)
					{
						itsAnglesController.loginUser();	
					}
				});
				
				((Button) v).setText("Tap Again to Login!");
			}
		});
		
		Button newUser = (Button) itsActivity.findViewById(R.id.signup_button);
		newUser.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				((LinearLayout) itsActivity.findViewById(R.id.signupUserNameGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) itsActivity.findViewById(R.id.signupEmailGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) itsActivity.findViewById(R.id.signupPasswordGroup)).setVisibility(View.VISIBLE);

				v.setOnClickListener(new OnClickListener() {
					public void onClick(View v)
					{
						itsAnglesController.registerUser();
					}
				});
				
				((Button) v).setText("Tap Again to Signup!");
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
