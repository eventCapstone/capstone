package com.angles.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.database.ContactTable;
import com.angles.database.EventTable;
import com.angles.model.AnglesEvent;
import com.angles.model.Attending;
import com.angles.model.User;
import com.google.cloud.backend.android.CloudBackend;
import com.google.cloud.backend.android.CloudBackendMessaging;
import com.google.cloud.backend.android.CloudCallbackHandler;
import com.google.cloud.backend.android.CloudEntity;
import com.google.cloud.backend.android.CloudQuery;
import com.google.cloud.backend.android.DBTableConstants;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

public class InviteGuestListAdapter extends BaseAdapter {
	private AnglesEvent event;
	private AnglesController controller;
	private Activity inviteGuestsActivity;
	List<User> contacts;
	Map<User, Attending> guests;
	
	public InviteGuestListAdapter(AnglesEvent event, AnglesController controller, 
			Activity activity) {
		this.event = event;
		this.controller = controller;
		this.inviteGuestsActivity = activity;
		
		contacts = new ArrayList(controller.getContacts());
		guests = event.getGuests();
	}

	@Override
	public int getCount() {
		return contacts.size();
	}

	@Override
	public Object getItem(int index) {
		return contacts.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View reuse, ViewGroup parent) {
		ViewGroup item = getViewGroup(reuse, parent);
		AlpineTextView userName = (AlpineTextView)item.findViewById(R.id.inviteUserName);
		AlpineButton inviteButton = (AlpineButton)item.findViewById(R.id.inviteButton);
		User contact = contacts.get(position);
		Attending attending = guests.get(contact); 
		
		userName.setText(contact.userName);		
		
		if (attending == null) {
			inviteButton.setVisibility(View.VISIBLE);
			inviteButton.setOnClickListener(new InviteClickListener(event, contact, parent.getContext()));
		}
		else {
			inviteButton.setVisibility(View.INVISIBLE);
		}
		
		return item;
	}
	
	private ViewGroup getViewGroup(View reuse, ViewGroup parent) {
		if(reuse instanceof ViewGroup) {
			return (ViewGroup)reuse;
		}
	
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewGroup item = (ViewGroup)inflater.inflate(R.layout.invite_item, null);
		
		return item;
	}
	
	private class InviteClickListener implements OnClickListener {
		AnglesEvent event;
		User user;
		Context currentActivity;
		
		public InviteClickListener(AnglesEvent event, User user, Context currentActivity) {
			this.event = event;
			this.user = user;
			this.currentActivity = currentActivity;
		}
		
		@Override
		public void onClick(View v) {
			//update guest table on cloud
			CloudBackendMessaging cb = new CloudBackendMessaging(currentActivity);
			CloudQuery cq = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
			
			CloudEntity invite = new CloudEntity(DBTableConstants.DB_TABLE_GUESTS);
			invite.put(DBTableConstants.DB_GUESTS_EVENT_ID, event.getEventID().toString());
			invite.put(DBTableConstants.DB_GUESTS_USERNAME, user.userName);
			invite.put(DBTableConstants.DB_GUESTS_ATTENDING_STATUS, "UNDECIDED");
			 CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
			      @Override
			      public void onComplete(final CloudEntity result) {
						//update guest map
						guests.put(user, Attending.UNDECIDED);
						
						//update local guest table
						EventTable eventTable = new EventTable(currentActivity);
						eventTable.addGuest(event.getEventID().toString(), user.userName);
			      }

			      @Override
			      public void onError(final IOException exception) {
			    	  System.out.println(exception.getMessage());
			      }
			};

			cb.insert(invite, handler);
			//update view
			v.setVisibility(View.INVISIBLE);
		}
	}
}
