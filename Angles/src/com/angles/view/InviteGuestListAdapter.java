package com.angles.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.angles.angles.AnglesController;
import com.angles.angles.R;
import com.angles.database.ContactTable;
import com.angles.model.AnglesEvent;
import com.angles.model.Attending;
import com.angles.model.User;
import com.google.cloud.backend.android.CloudBackend;
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
import android.widget.TextView;
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
		TextView userName = (TextView)item.findViewById(R.id.inviteUserName);
		TextView invitedStatus = (TextView)item.findViewById(R.id.invitedStatus);
		Button inviteButton = (Button)item.findViewById(R.id.inviteButton);
		User contact = contacts.get(position);
		Attending attending = guests.get(contact); 
		
		userName.setText(contact.userName);		
		
		if (attending == null) {
			invitedStatus.setVisibility(View.INVISIBLE);
			inviteButton.setVisibility(View.VISIBLE);
			inviteButton.setOnClickListener(new InviteClickListener(invitedStatus, event, contact));
		}
		else {
			inviteButton.setVisibility(View.INVISIBLE);
			invitedStatus.setVisibility(View.VISIBLE);
			switch (attending) {
				case UNDECIDED:
					invitedStatus.setText("Invited");
					break;
				case ATTENDING:
					invitedStatus.setText("Going");
					break;
				case MAYBE:
					invitedStatus.setText("Maybe");
					break;
				case NOT_ATTENDING:
					invitedStatus.setText("Not going");
					break;
			}
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
		TextView inviteStatus;
		AnglesEvent event;
		User user;
		
		public InviteClickListener(TextView inviteStatus, AnglesEvent event, User user) {
			this.inviteStatus = inviteStatus;
			this.event = event;
			this.user = user;
		}
		
		@Override
		public void onClick(View v) {
			//update guest table on cloud
			CloudBackend cb = new CloudBackend();
			CloudQuery cq = new CloudQuery(DBTableConstants.DB_TABLE_GUESTS);
			
			CloudEntity invite = new CloudEntity(DBTableConstants.DB_TABLE_GUESTS);
			invite.put(DBTableConstants.DB_GUESTS_EVENT_ID, event.getEventID().toString());
			invite.put(DBTableConstants.DB_GUESTS_USERNAME, user.userName);
			invite.put(DBTableConstants.DB_GUESTS_ATTENDING_STATUS, "UNDECIDED");
			
			try {
				cb.insert(invite);
			}
			catch (IOException e) {
				//don't add to map or update view if we encountered an error
				return;
			}

			//update guest map
			guests.put(user, Attending.ATTENDING);
			
			//update view
			v.setVisibility(View.INVISIBLE);
			inviteStatus.setVisibility(View.VISIBLE);
			inviteStatus.setText("Invited");
		}
	}
}
