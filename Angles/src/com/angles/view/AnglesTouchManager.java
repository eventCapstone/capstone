package com.angles.view;

import java.util.Calendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog;
import android.hardware.Camera;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.angles.angles.AnglesController;
import com.angles.angles.OngoingEventActivity;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;

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
		Button createEventButton = (Button) currentActivity.findViewById(R.id.create_event_button);
		createEventButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadCreateEventActivity(currentActivity);
				
			}
		});
		
		Button settingsButton = (Button) currentActivity.findViewById(R.id.settingsButton);
		settingsButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadChangeSettingsActivity(currentActivity);
			}
		});
	}
	
	public void setSettingsListeners(Activity currentActivity)
	{
		Button saveSettings = (Button) currentActivity.findViewById(R.id.saveSettingsButton);
		saveSettings.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadEventListActivity(currentActivity);
			}
		});
	}
	
	public void setEventDisplayListeners(Activity currentActivity)
	{
		Button otherAnglesButton = (Button) currentActivity.findViewById(R.id.otherAnglesButton);
		otherAnglesButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadEventListActivity(currentActivity);
			}
		});
	}
	
	public void setCreateEventListeners(Activity currentActivity){
		Button startDateButton = (Button)currentActivity.findViewById(R.id.startDateButton);
		Button startTimeButton = (Button)currentActivity.findViewById(R.id.startTimeButton);
		Button endDateButton = (Button)currentActivity.findViewById(R.id.endDateButton);
		Button endTimeButton = (Button)currentActivity.findViewById(R.id.endTimeButton);
		Button submitNewEventButton = (Button)currentActivity.findViewById(R.id.submitNewEventButton);
		
		startDateButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				Calendar calendar = EventsManager.parseDate(((Button)v).getText().toString());
				AnglesDateListener startDateListener = new AnglesDateListener(currentActivity) {
					@Override
					public void onDateSet(DatePicker picker, int year, int month, int day) {
						Button button = (Button)currentActivity.findViewById(R.id.startDateButton);
						button.setText((month+1) + "/" + day + "/" + year);
					}
				};
				DatePickerDialog startDatePicker = new DatePickerDialog(
					currentActivity,
					startDateListener,
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
				startDatePicker.show();
			}
		});
		
		startTimeButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				Calendar calendar = EventsManager.parseTime(((Button)v).getText().toString());
				AnglesTimeListener startTimeListener = new AnglesTimeListener(currentActivity) {
					@Override
					public void onTimeSet(TimePicker picker, int hours, int minutes) {
						Button button = (Button)currentActivity.findViewById(R.id.startTimeButton);
						String ampm;
						if (hours == 24) {
							hours = 0;
						}
						if (hours >= 12)
						{
							ampm = " PM";
							hours -= 12;
						}
						else
						{
							ampm = " AM";
						}
						if (hours == 0) {
							hours = 12;
						}
						button.setText(hours + ":" + String.format("%02d", minutes) + ampm);
					}
				};
				TimePickerDialog startTimePicker = new TimePickerDialog(
					currentActivity,
					startTimeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					false);
				startTimePicker.show();
			}
		});
		
		endDateButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				Calendar calendar = EventsManager.parseDate(((Button)v).getText().toString());
				AnglesDateListener endDateListener = new AnglesDateListener(currentActivity) {
					@Override
					public void onDateSet(DatePicker picker, int year, int month, int day) {
						Button button = (Button)currentActivity.findViewById(R.id.endDateButton);
						button.setText((month+1) + "/" + day + "/" + year);
					}
				};
				DatePickerDialog endDatePicker = new DatePickerDialog(
					currentActivity,
					endDateListener,
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH));
				endDatePicker.show();
			}
		});

		endTimeButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				Calendar calendar = EventsManager.parseTime(((Button)v).getText().toString());
				AnglesTimeListener endTimeListener = new AnglesTimeListener(currentActivity) {
					@Override
					public void onTimeSet(TimePicker picker, int hours, int minutes) {
						Button button = (Button)currentActivity.findViewById(R.id.endTimeButton);
						String ampm;
						if (hours == 24) {
							hours = 0;
						}
						if (hours >= 12)
						{
							ampm = " PM";
							hours -= 12;
						}
						else
						{
							ampm = " AM";
						}
						if (hours == 0) {
							hours = 12;
						}
						button.setText(hours + ":" + String.format("%02d", minutes) + ampm);
					}
				};
				TimePickerDialog endTimePicker = new TimePickerDialog(
					currentActivity,
					endTimeListener,
					calendar.get(Calendar.HOUR_OF_DAY),
					calendar.get(Calendar.MINUTE),
					false);
				endTimePicker.show();
			}
		});
		
		submitNewEventButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.createEvent(currentActivity);
			}
		});
	}
	
	public void setAngleCompleteListeners(Activity currentActivity){
		Button gotIt = (Button) currentActivity.findViewById(R.id.completed_angle_button);
		gotIt.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadEventListActivity(currentActivity);
			}
		});
	}
	
	public void setOngoingEventListeners(Activity currentActivity) {
		
		Button btnCapturePhoto = (Button) currentActivity.findViewById(R.id.btnCapturePhoto);
		
		btnCapturePhoto.setOnClickListener(new AnglesClickListener(currentActivity) {
			
			public void onClick(View v) {
				
				anglesController.loadCameraActivity(currentActivity);
			}
		});
		
		Button btnEventList = (Button) currentActivity.findViewById(R.id.btnEventList);
		
		btnEventList.setOnClickListener(new AnglesClickListener(currentActivity) {
			
			public void onClick(View v) {
				
				anglesController.loadEventListActivity(currentActivity);
			}
		});
	}
	
	private abstract class AnglesClickListener implements OnClickListener {
		protected Activity currentActivity;
		
		public AnglesClickListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}
	
	private abstract class AnglesDateListener implements DatePickerDialog.OnDateSetListener {
		protected Activity currentActivity;
		public AnglesDateListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}

	private abstract class AnglesTimeListener implements TimePickerDialog.OnTimeSetListener {
		protected Activity currentActivity;
		public AnglesTimeListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}
}
