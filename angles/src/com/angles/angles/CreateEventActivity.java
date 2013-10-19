package com.angles.angles;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.angles.angles.AnglesController;
import com.angles.model.EventsManager;


public class CreateEventActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		Calendar startTime;
		
		super.onCreate(savedInstanceState);
    	 AnglesController.getInstance().getDisplayManager().displayCreateEvent(this);
    	 
    	 //set default start time tomorrow if it's after 4pm already
    	 startTime = Calendar.getInstance();
    	 if (startTime.get(Calendar.HOUR_OF_DAY) >= 16) {
    		 startTime.add(Calendar.DATE, 1);
    	 }
    	 startTime.set(Calendar.HOUR_OF_DAY, 18);
    	 startTime.clear(Calendar.MINUTE);
    	 startTime.clear(Calendar.SECOND);
    	 startTime.clear(Calendar.MILLISECOND);
    	 
    	 Button startDateButton = (Button)findViewById(R.id.startDateButton);
    	 startDateButton.setText(EventsManager.getDisplayDate(startTime));
    	 Button startTimeButton = (Button)findViewById(R.id.startTimeButton);
    	 startTimeButton.setText(EventsManager.getDisplayTime(startTime));
    	 
    	 startTime.add(Calendar.HOUR,  4);
    	 Button endDateButton = (Button)findViewById(R.id.endDateButton);
    	 endDateButton.setText(EventsManager.getDisplayDate(startTime));
    	 Button endTimeButton = (Button)findViewById(R.id.endTimeButton);
    	 endTimeButton.setText(EventsManager.getDisplayTime(startTime));
    	 
    	 AnglesController.getInstance().getTouchManager().setCreateEventListeners(this);
    }
}
