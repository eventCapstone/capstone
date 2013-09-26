package com.angles.view;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.angles.angles.R;

public class AnglesDisplayManager {
public static final String KEY = "AnglesDisplayManager"; 

private Activity itsActivity;

public AnglesDisplayManager(Activity inActivity){
	itsActivity = inActivity;
	
	
}

public void displayHome(){
	itsActivity.setContentView(R.layout.home_page);
}

public void displayEventListHome(){
	itsActivity.setContentView(R.layout.events_list);
	  ListView theList = (ListView) itsActivity.findViewById(R.id.listOfEvents);
			ArrayList<String> theStrings = com.angles.view.EventsManager.getEventList();
			StableArrayAdapter adapter = new StableArrayAdapter(itsActivity,android.R.layout.simple_list_item_1 ,theStrings);
			theList.setAdapter(adapter);
			
			
}


private class StableArrayAdapter extends ArrayAdapter<String> {

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId,
        List<String> objects) {
      super(context, textViewResourceId, objects);
      for (int i = 0; i < objects.size(); ++i) {
        mIdMap.put(objects.get(i), i);
      }
    }
    


}
}
