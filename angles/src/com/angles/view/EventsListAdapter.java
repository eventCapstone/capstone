package com.angles.view;

import java.util.List;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventsListAdapter extends BaseAdapter {
	private List<AnglesEvent> eventsList;
	private AnglesController anglesController;
	private Activity eventsListActivity;
	
	public EventsListAdapter(List<AnglesEvent> eventsList, AnglesController anglesController, 
			Activity eventsListActivity)
	{
		this.eventsList = eventsList;
		this.anglesController = anglesController;
		this.eventsListActivity = eventsListActivity;
	}

	@Override
	public int getCount() {
		return eventsList.size();
	}

	@Override
	public Object getItem(int index) {
		return eventsList.get(index);
	}

	@Override
	public long getItemId(int index) {
		return eventsList.get(index).getEventID().getLeastSignificantBits();
	}

	@Override
	public View getView(int index, View reuse, ViewGroup parent) {
		ViewGroup item = getViewGroup(reuse, parent);
		TextView eventName = (TextView)item.findViewById(R.id.eventName);
		TextView eventStatus = (TextView)item.findViewById(R.id.inviteStatus);
		
		eventName.setText(eventsList.get(index).eventTitle);
		//TODO: Implement getting this user's invite status
		eventStatus.setText("Going");
		
		item.setOnClickListener(new EventButtonListener(eventsListActivity, eventsList.get(index)));
				
		return item;
	}
	
	private ViewGroup getViewGroup(View reuse, ViewGroup parent) {
		if(reuse instanceof ViewGroup) {
			return (ViewGroup)reuse;
		}
	
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup item = (ViewGroup)inflater.inflate(R.layout.event_item, null);
		
		return item;
	}
	
	private class EventButtonListener implements OnClickListener
	{
		AnglesEvent anglesEvent;
		Activity anglesListActivity;
		
		public EventButtonListener(Activity currentActivity, AnglesEvent anglesEvent)
		{
			this.anglesEvent = anglesEvent;
			this.anglesListActivity = currentActivity;
		}
		
		@Override
		public void onClick(View view) {
			anglesController.viewEvent(anglesListActivity, anglesEvent);
		}
	}
}
