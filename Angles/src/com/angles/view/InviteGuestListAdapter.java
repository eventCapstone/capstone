package com.angles.view;

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
			inviteButton.setOnClickListener(new InviteClickListener(invitedStatus));
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
		
		public InviteClickListener(TextView inviteStatus) {
			this.inviteStatus = inviteStatus;
		}
		
		@Override
		public void onClick(View v) {
			v.setVisibility(View.INVISIBLE);
			inviteStatus.setVisibility(View.VISIBLE);
			inviteStatus.setText("Invited");
		}
	}
}
