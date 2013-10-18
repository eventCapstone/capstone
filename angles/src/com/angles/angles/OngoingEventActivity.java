/* The class OngoingEventActivity draws the screen after an event
 * has started. From here you can take a photo, view recently 
 * uploaded photos, vote up a photo, or go back to the Events list.
 */
package com.angles.angles;

import android.app.Activity;
import android.os.Bundle;

public class OngoingEventActivity extends Activity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
    	 super.onCreate(savedInstanceState);
    	    	 
    	 AnglesController.getInstance().getDisplayManager().displayOngoingEventActivity(this);
    	 
    	 AnglesController.getInstance().getTouchManager().setOngoingEventListeners(this);
	}
}
