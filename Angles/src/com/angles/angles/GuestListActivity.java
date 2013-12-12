package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

import com.angles.model.AnglesEvent;

/**
 * Activity that loads the guest list
 * @author Mike
 *
 */
public class GuestListActivity  extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	   	Bundle bundle = getIntent().getExtras();
	   	AnglesEvent event = (AnglesEvent)bundle.getSerializable("event");
			     	 
		AnglesController.getInstance().getDisplayManager().displayGuestList(this, event);
		AnglesController.getInstance().getTouchManager().setGuestListListeners(this, event);
	}
}