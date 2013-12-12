package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

import com.angles.angles.AnglesController;
import com.angles.database.EventTable;

/**
 * First activity that loads when the app loads. Initializes the controller then loads
 * the login activity
 * @author Mike
 *
 */
@SuppressWarnings("unused")
public class MainActivity extends Activity {
	private AnglesController  itsMainController;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   	 	super.onCreate(savedInstanceState);
   	 	setContentView(R.layout.activity_main);
   	 	
//   	EventTable te = new EventTable(this);
// 	 	te.emptyTables();
//   	 	
     	AnglesController.createInstance(this);
     	AnglesController.getInstance().init(this);
     	AnglesController.getInstance().getTouchManager().setMainActivityListeners(this);
   	 	AnglesController.getInstance().loadLoginActivity(this);
    }
   
}
