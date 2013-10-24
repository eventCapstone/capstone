package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

import com.angles.angles.AnglesController;

@SuppressWarnings("unused")
public class MainActivity extends Activity {
	//hello

	private AnglesController  itsMainController;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
   	 	super.onCreate(savedInstanceState);
   	 	
     	AnglesController.createInstance(this);
     	
     	AnglesController.getInstance().loadLoginActivity(this);
    }
   
}
