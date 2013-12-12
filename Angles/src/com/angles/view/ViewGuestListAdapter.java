package com.angles.view;

import java.util.List;

import com.angles.model.User;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The guest list adapter inflates the views for the guest list and defines
 * their onClickListeners
 * @author Mike
 *
 */
public class ViewGuestListAdapter extends BaseAdapter {
	private Activity activity;
	private List<String> guests;
	
	public ViewGuestListAdapter(Activity activity, List<String> guests) {
		this.activity = activity;
		this.guests = guests;
	}
	
	@Override
	public int getCount() {
		return guests.size();
	}

	@Override
	public Object getItem(int position) {
		return guests.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView guest = new AlpineTextView(activity);
		guest.setText(guests.get(position));
		guest.setTextSize(20);
		return guest;
	}

}
