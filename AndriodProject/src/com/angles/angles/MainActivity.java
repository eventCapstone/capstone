package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

import com.angles.angles.AnglesController;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

	private AnglesController  itsMainController = new AnglesController(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
        	 itsMainController.homeEvent();  
      
     
    }

    
   
   
}