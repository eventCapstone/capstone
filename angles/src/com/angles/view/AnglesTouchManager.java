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
	
	private AnglesController anglesController;
	
	public AnglesTouchManager(AnglesController inController) 
	{
		anglesController = inController;
	}
	
	public void setLoginPageListeners(Activity currentActivity){
		Button login = (Button) currentActivity.findViewById(R.id.login_button);
		login.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				((LinearLayout) currentActivity.findViewById(R.id.loginUserNameGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) currentActivity.findViewById(R.id.loginPasswordGroup)).setVisibility(View.VISIBLE);
				v.setOnClickListener(new AnglesClickListener(currentActivity) {
					public void onClick(View v)
					{
						anglesController.loginUser(currentActivity);	
					}
				});
				
				((Button) v).setText("Tap Again to Login!");
			}
		});
		
		Button newUser = (Button) currentActivity.findViewById(R.id.signup_button);
		newUser.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				((LinearLayout) currentActivity.findViewById(R.id.signupUserNameGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) currentActivity.findViewById(R.id.signupEmailGroup)).setVisibility(View.VISIBLE);
				((LinearLayout) currentActivity.findViewById(R.id.signupPasswordGroup)).setVisibility(View.VISIBLE);

				v.setOnClickListener(new OnClickListener() {
					public void onClick(View v)
					{
						anglesController.registerUser(currentActivity);
					}
				});
				
				((Button) v).setText("Tap Again to Signup!");
			}
		});
	}
	
	public void setEventsHomeListeners(Activity currentActivity) 
	{
		Button createAngleButton = (Button) currentActivity.findViewById(R.id.create_angle_button);
		createAngleButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventCreateAnglesEvent(currentActivity);
				
			}
		});
		
		Button settingsButton = (Button) currentActivity.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.changeSettings(currentActivity);
			}
		});
	}
	
	public void setSettingsListeners(Activity currentActivity)
	{
		Button saveSettings = (Button) currentActivity.findViewById(R.id.saveSettingsButton);
		saveSettings.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventListHomeEvent(currentActivity);
			}
		});
	}
	
	public void setEventDisplayListeners(Activity currentActivity)
	{
		Button otherAnglesButton = (Button) currentActivity.findViewById(R.id.otherAnglesButton);
		otherAnglesButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventListHomeEvent(currentActivity);
			}
		});
	}
	
	public void setCreateAngleListeners(Activity currentActivity){
		Button inviteGuests = (Button) currentActivity.findViewById(R.id.new_angle_invite_guests);
		inviteGuests.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventAngleCreateCompleted(currentActivity);
			}
		});
	}
	
	public void setCreateNewAccountListeners(Activity currentActivity){
		Button submit = (Button) currentActivity.findViewById(R.id.submit_button);
		submit.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventListHomeEvent(currentActivity);
			}
		});
	}
	
	public void setAngleCompleteListeners(Activity currentActivity){
		Button gotIt = (Button) currentActivity.findViewById(R.id.completed_angle_button);
		gotIt.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.eventListHomeEvent(currentActivity);
			}
		});
	}
	
	public void setOngoingEventListeners(Activity currentActivity) {
		
		Button btnCapturePhoto = (Button) currentActivity.findViewById(R.id.btnCapturePhoto);
		
		btnCapturePhoto.setOnClickListener(new AnglesClickListener(currentActivity) {
			
			public void onClick(View v) {
				
				anglesController.ongoingEvent(currentActivity);
			}
		});
		
		Button btnEventList = (Button) currentActivity.findViewById(R.id.btnEventList);
		
		btnEventList.setOnClickListener(new AnglesClickListener(currentActivity) {
			
			public void onClick(View v) {
				
				anglesController.eventListHomeEvent(currentActivity);
			}
		});
	}
	
	private abstract class AnglesClickListener implements OnClickListener {
		protected Activity currentActivity;
		
		public AnglesClickListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}
	
}
