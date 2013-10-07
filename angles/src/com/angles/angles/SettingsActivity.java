package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 AnglesController.getInstance().getDisplayManager().displaySettings(this);
    	 AnglesController.getInstance().getTouchManager().setSettingsListeners(this);
    }
}
