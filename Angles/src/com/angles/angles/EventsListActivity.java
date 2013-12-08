package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class EventsListActivity extends Activity {
		
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 
    	 AnglesController.getInstance().getDisplayManager().displayEventList(this,
    			 AnglesController.getInstance().getEventsManager());
    	 
    	 AnglesController.getInstance().getTouchManager().setEventsListListeners(this);
    }
}
