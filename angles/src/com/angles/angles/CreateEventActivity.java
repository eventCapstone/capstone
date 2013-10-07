package com.angles.angles;
import android.app.Activity;
import android.os.Bundle;

import com.angles.angles.AnglesController;


public class CreateEventActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 AnglesController.getInstance().getDisplayManager().displayCreateAngle(this);
    	 AnglesController.getInstance().getTouchManager().setCreateAngleListeners(this);
    }
}
