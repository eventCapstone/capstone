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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.angles.angles.AnglesController;
import com.angles.angles.OngoingEventActivity;
import com.angles.angles.CreateEventActivity;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;
import com.angles.model.EventsManager;

/**
 * The touch manager is responsible for loading the click listeners for the different activities
 * @author Mike
 *
 */
public class AnglesTouchManager {
	public static final String KEY = "AnglesTouchManager"; 
	
	private AnglesController anglesController;
	
	/**
	 * CONSTRUCTOR
	 * @param inController The application controller
	 */
	public AnglesTouchManager(AnglesController inController) 
	{
		anglesController = inController;
	}
	
	/**
	 * LOGIN PAGE LISTENERS
	 * @param currentActivity The current activity
	 */
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
	
	/**
	 * INVITE LIST LISTENERS
	 * @param currentActivity The current activity
	 * @param event The event we want to invite guests to
	 */
	public void setInviteListListeners(Activity currentActivity, AnglesEvent event)
	{
		Button finishButton = (Button) currentActivity.findViewById(R.id.finishedInvitingButton);
		finishButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			public void onClick(View v) {
				anglesController.loadViewEventActivity(currentActivity, event);
			}
		});
	}
	
	/**
	 * EVENTS LIST LISTENERS
	 * @param currentActivity The current activity
	 */
	public void setEventsListListeners(Activity currentActivity) 
	{      
		Button createEventButton = (Button) currentActivity.findViewById(R.id.create_event_button);
		createEventButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadCreateEventActivity(currentActivity);
				
			}
		});
		
		Button logoutButton = (Button) currentActivity.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadLoginActivity(currentActivity);
			}
		});
		
		ImageView refreshImage = (ImageView) currentActivity.findViewById(R.id.refreshIcon);
		refreshImage.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				Toast.makeText(currentActivity, "Reloading Events...", Toast.LENGTH_LONG).show();
				anglesController.getEventsManager().loadEventsFromCloud(currentActivity);
			}
		});
	}
	
	/**
	 * FUTURE EVENT VIEW LISTENERS
	 * @param currentActivity The current activity
	 * @param event The event to view
	 */
	public void setFutureEventListeners(Activity currentActivity, AnglesEvent event)
	{
		Button otherEventsButton = (Button) currentActivity.findViewById(R.id.otherEventsButton);
		Button inviteGuestsButton = (Button) currentActivity.findViewById(R.id.inviteGuestsButton);
		Button viewGuestsButton = (Button) currentActivity.findViewById(R.id.viewGuestsButton);
		Button acceptButton = (Button) currentActivity.findViewById(R.id.acceptButton);
		Button declineButton = (Button) currentActivity.findViewById(R.id.declineButton);
		
		otherEventsButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadEventListActivity(currentActivity);
			}
		});
		
		inviteGuestsButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			public void onClick(View v) {
				anglesController.loadInviteListActivity(currentActivity, event);
			}
		});
		
		viewGuestsButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			public void onClick(View v) {
				anglesController.loadGuestListActivity(currentActivity, event);
			}
		});
		
		acceptButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			@Override
			public void onClick(View view) {
				event.acceptInvite(view.getContext());
				view.setVisibility(View.INVISIBLE);
				((Button)currentActivity.findViewById(R.id.declineButton))
					.setVisibility(View.INVISIBLE);
			}
		});
		
		declineButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			@Override
			public void onClick(View view) {
				event.declineInvite(view.getContext());
				view.setVisibility(View.INVISIBLE);
				((Button)currentActivity.findViewById(R.id.acceptButton))
					.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	public void setGuestListListeners(Activity currentActivity, AnglesEvent event)
	{
		Button backToEventButton = (Button)currentActivity.findViewById(R.id.backToEventButton);
		backToEventButton.setOnClickListener(new AnglesEventClickListener(currentActivity, event) {
			@Override
			public void onClick(View v) {
				anglesController.loadViewEventActivity(currentActivity, event);
			}
		});
	}
	
	public void setMainActivityListeners(Activity currentActivity) {
		Button enterButton = (Button) currentActivity.findViewById(R.id.enterButton);
		enterButton.setOnClickListener(new AnglesClickListener(currentActivity) {
			@Override
			public void onClick(View v) {
				anglesController.loadLoginActivity(currentActivity);
			}
		});
	}
	
	/**
	 * CREATE EVENT LISTENERS
	 * @param currentActivity The current activity
	 */
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
				anglesController.createEvent((CreateEventActivity)currentActivity);
			}
		});
	}
	
	/**
	 * ONGOING EVENT LISTENERS
	 * @param currentActivity The current activity
	 */
	public void setOngoingEventListeners(Activity currentActivity) {
		Button btnCapturePhoto = (Button) currentActivity.findViewById(R.id.btnCapturePhoto);
		
		Button btnEventList = (Button) currentActivity.findViewById(R.id.btnEventList);
		
		btnEventList.setOnClickListener(new AnglesClickListener(currentActivity) {
			public void onClick(View v) {
				anglesController.loadEventListActivity(currentActivity);
			}
		});
	}
	
	/**
	 * A click listener with a reference to the current activity
	 * @author Mike
	 *
	 */
	private abstract class AnglesClickListener implements OnClickListener {
		protected Activity currentActivity;
		
		public AnglesClickListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}
	
	/**
	 * A click listener with a reference to the current activity and a specific event
	 * @author Mike
	 *
	 */
	private abstract class AnglesEventClickListener implements OnClickListener {
		protected Activity currentActivity;
		AnglesEvent event;
		
		public AnglesEventClickListener(Activity currentActivity, AnglesEvent event) {
			this.currentActivity = currentActivity;
			this.event = event;
		}
	}
	
	/**
	 * An OnDateSetListener with a reference to the current activity
	 * @author Mike
	 *
	 */
	private abstract class AnglesDateListener implements DatePickerDialog.OnDateSetListener {
		protected Activity currentActivity;
		public AnglesDateListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}

	/**
	 * An OnTimeSetListener with a reference to the current activity
	 * @author Mike
	 *
	 */
	private abstract class AnglesTimeListener implements TimePickerDialog.OnTimeSetListener {
		protected Activity currentActivity;
		public AnglesTimeListener(Activity currentActivity) {
			this.currentActivity = currentActivity;
		}
	}
}
