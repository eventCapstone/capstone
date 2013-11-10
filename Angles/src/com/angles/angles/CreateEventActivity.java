package com.angles.angles;

import java.io.IOException;
import java.util.Calendar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.angles.model.EventsManager;
import com.angles.view.AnglesDisplayManager;
import com.angles.view.ConfirmPictureDialog;
import com.google.cloud.backend.android.CloudBackendActivity;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.DBTableConstants;
public class CreateEventActivity extends CloudBackendActivity {
	
	EditText eventTitle;
	EditText eventDescription;
	Button startDateButton;
	Button startTimeButton;
	Button endDateButton;
	Button endTimeButton;
	Calendar startTime;
	Button createButton;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AnglesController.getInstance().getDisplayManager()
				.displayCreateEvent(this);
		createButton = (Button)findViewById(R.id.submitNewEventButton);
		eventTitle = (EditText) findViewById(R.id.eventNameInput);
		eventDescription = (EditText) findViewById(R.id.eventDescriptionInput);
		// set default start time tomorrow if it's after 4pm already
		startTime = Calendar.getInstance();
		if (startTime.get(Calendar.HOUR_OF_DAY) >= 16) {
			startTime.add(Calendar.DATE, 1);
		}
		startTime.set(Calendar.HOUR_OF_DAY, 18);
		startTime.clear(Calendar.MINUTE);
		startTime.clear(Calendar.SECOND);
		startTime.clear(Calendar.MILLISECOND);

		startDateButton = (Button) findViewById(R.id.startDateButton);
		startDateButton.setText(EventsManager.getDisplayDate(startTime));
		startTimeButton = (Button) findViewById(R.id.startTimeButton);
		startTimeButton.setText(EventsManager.getDisplayTime(startTime));

		startTime.add(Calendar.HOUR, 4);
		endDateButton = (Button) findViewById(R.id.endDateButton);
		endDateButton.setText(EventsManager.getDisplayDate(startTime));
		endTimeButton = (Button) findViewById(R.id.endTimeButton);
		endTimeButton.setText(EventsManager.getDisplayTime(startTime));

		AnglesController.getInstance().getTouchManager()
				.setCreateEventListeners(this);
	}

	public void sendEventToCloud(View view) {
		CloudEntity createNewEvent = new CloudEntity(DBTableConstants.DB_TABLE_ANGLES_EVENT);
		createNewEvent.put(DBTableConstants.DB_EVENT_START_DATE,startDateButton.getText().toString());
		createNewEvent.put(DBTableConstants.DB_EVENT_START_TIME,startTimeButton.getText().toString() );
		createNewEvent.put(DBTableConstants.DB_EVENT_END_DATE, endDateButton.getText().toString());
		createNewEvent.put(DBTableConstants.DB_EVENT_END_TIME, endTimeButton.getText().toString());
		createNewEvent.put(DBTableConstants.DB_EVENT_DESCRIPTION, eventDescription.getText().toString());
		createNewEvent.put(DBTableConstants.DB_EVENT_HOST_USERNAME,
				AnglesController.getInstance().getAnglesUser().userName);
		createNewEvent.put(DBTableConstants.DB_EVENT_TITLE, eventTitle.getText().toString());

		
		 CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
		      @Override
		      public void onComplete(final CloudEntity result) {
		        //EventList.add(0, result);  returns what we just put in if successful,  add this to the users event list.
		    	  Toast.makeText(getApplicationContext(),"Event Succesfully Added", Toast.LENGTH_LONG).show();

		      }

		      @Override
		      public void onError(final IOException exception) {
		        handleEndpointException(exception);
		      }
		};

		    // execute the insertion with the handler
		getCloudBackend().insert(createNewEvent, handler);
	    createButton.setEnabled(false);
	}
 
	private void handleEndpointException(IOException e) {
	    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
	    createButton.setEnabled(true);
	}
	 
}


