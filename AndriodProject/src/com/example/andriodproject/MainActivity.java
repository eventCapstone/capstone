package com.example.andriodproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.TestBean;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView theList = (ListView) findViewById(R.id.listView1);
		ArrayList<String> theStrings = TestBean.getList();
		StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1 ,theStrings);
		theList.setAdapter(adapter);
		

    Button theButton = (Button) findViewById(R.id.button1);
    theButton.setOnClickListener(onClickListener) ;
	
		

    }
    private OnClickListener onClickListener = new OnClickListener() { 
    	public void onClick(final View v) {
		setContentView(R.layout.events_list);
		
		
    	

    }
 };
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
