package com.angles.view;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.model.AnglesEvent;
import com.angles.model.Attending;
import com.angles.model.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
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
		Button goButton = (Button)item.findViewById(R.id.goButton);
		Button noGoButton = (Button)item.findViewById(R.id.noGoButton);
		AnglesEvent event = eventsList.get(index);
		
		User user = anglesController.getAnglesUser();
		switch (event.getStatus(user))
		{
			case ATTENDING:
				eventStatus.setText("     Going");
				eventStatus.setVisibility(View.VISIBLE);
				goButton.setVisibility(View.INVISIBLE);
				noGoButton.setVisibility(View.INVISIBLE);
				break;
			case NOT_ATTENDING:
				eventStatus.setText("     Declined");
				eventStatus.setVisibility(View.VISIBLE);
				goButton.setVisibility(View.INVISIBLE);
				noGoButton.setVisibility(View.INVISIBLE);
				break;
			case MAYBE:
				eventStatus.setText("     Maybe");
				eventStatus.setVisibility(View.VISIBLE);
				goButton.setVisibility(View.INVISIBLE);
				noGoButton.setVisibility(View.INVISIBLE);
				break;
			case UNDECIDED:
				goButton.setOnClickListener(new SelectStatusListener(eventsList.get(index), goButton,
						noGoButton, eventStatus) {
					@Override
					public void onClick(View view) {
						anglesEvent.acceptInvite(anglesController.getAnglesUser());
						status.setText("Going");
						goButton.setVisibility(View.INVISIBLE);
						noGoButton.setVisibility(View.INVISIBLE);
						status.setVisibility(View.VISIBLE);
					}
				});
				noGoButton.setOnClickListener(new SelectStatusListener(eventsList.get(index), goButton,
						noGoButton, eventStatus) {
					@Override
					public void onClick(View view) {
						anglesEvent.declineInvite(anglesController.getAnglesUser());
						status.setText("Declined");
						goButton.setVisibility(View.INVISIBLE);
						noGoButton.setVisibility(View.INVISIBLE);
						status.setVisibility(View.VISIBLE);
					}
				});
				eventStatus.setVisibility(View.INVISIBLE);
				goButton.setVisibility(View.VISIBLE);
				noGoButton.setVisibility(View.VISIBLE);
		}
		eventName.setText(event.eventTitle);
		eventName.setOnClickListener(new SelectEventListener(eventsListActivity, eventsList.get(index)) {
			
		});
				
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
	
	private class SelectEventListener implements OnClickListener
	{
		AnglesEvent anglesEvent;
		Activity anglesListActivity;
		
		public SelectEventListener(Activity currentActivity, AnglesEvent anglesEvent)
		{
			this.anglesEvent = anglesEvent;
			this.anglesListActivity = currentActivity;
		}
		
		@Override
		public void onClick(View view) {
			
			if (isOngoing(anglesEvent)){
				
				anglesController.loadOngoingEventActivity(anglesListActivity);
			}
			else
				anglesController.loadViewEventActivity(anglesListActivity, anglesEvent);
		}
	}
	
	private abstract class SelectStatusListener implements OnClickListener
	{
		AnglesEvent anglesEvent;
		Button goButton;
		Button noGoButton;
		TextView status;
		
		public SelectStatusListener(AnglesEvent anglesEvent, Button goButton, Button noGoButton,
			TextView status)
		{
			this.anglesEvent = anglesEvent;
			this.goButton = goButton;
			this.noGoButton = noGoButton;
			this.status = status;
		}
	}
	
	private boolean isOngoing(AnglesEvent event) {
		
		Calendar now = Calendar.getInstance();
		
		if (now.after(event.startTime) && now.before(event.endTime)){
			
			return true;
		}
		else
			return false;
	}
}
