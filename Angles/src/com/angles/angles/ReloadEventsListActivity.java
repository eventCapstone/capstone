package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

public class ReloadEventsListActivity extends Activity {
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	   	 	super.onCreate(savedInstanceState);
	   	 	setContentView(R.layout.activity_main);
	     	AnglesController.getInstance().reloadEvents(this);
	    }

}
