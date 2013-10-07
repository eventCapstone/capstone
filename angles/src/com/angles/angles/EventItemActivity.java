package com.angles.angles;

import com.angles.model.AnglesEvent;

import android.app.Activity;
import android.os.Bundle;

public class EventItemActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 Bundle bundle = getIntent().getExtras();
    	 AnglesEvent event = (AnglesEvent)bundle.getSerializable("event");
    	 
    	 AnglesController.getInstance().getDisplayManager().displayEvent(this, event);
    	 AnglesController.getInstance().getTouchManager().setEventDisplayListeners(this);
    }
}
