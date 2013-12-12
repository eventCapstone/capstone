package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

/**
 * ReloadEventsList activity. Before we reload the events list we need to make
 * sure nobody's trying to access it, so we switch to this activity
 * @author Mike
 *
 */
public class ReloadEventsListActivity extends Activity {
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	   	 	super.onCreate(savedInstanceState);
	   	 	setContentView(R.layout.activity_main);
	     	AnglesController.getInstance().reloadEvents(this);
	    }

}
