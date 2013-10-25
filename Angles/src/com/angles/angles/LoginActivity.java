package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends Activity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
    	 AnglesController.getInstance().getDisplayManager().displayLogin(this);
    	 AnglesController.getInstance().getTouchManager().setLoginPageListeners(this);
    }
}
