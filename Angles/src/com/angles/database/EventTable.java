package com.angles.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.angles.model.AnglesEvent;
import com.angles.model.Attending;
import com.angles.model.EventsManager;
import com.angles.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventTable extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "EventTable";
	/* Events Table Name */
	public static final String EVENTS_TABLE_NAME = "AnglesEvents";
	/* Events Table Columns */
	public static final String EVENT_ID = "Event_Id";
	public static final String HOST_NAME = "Host_Name";
	public static final String EVENT_NAME = "Event_Name";
	public static final String EVENT_DESCRIPTION = "Description";
	public static final String START_TIME = "Start_Time";
	public static final String START_DATE = "Start_Date";
	public static final String END_TIME = "End_Time";
	public static final String END_DATE = "End_Date";
	/* Guests Table Name */
	public static final String GUESTS_TABLE_NAME = "Guests";
	/* Guests Table Columns */
	public static final String GUEST_NAME = "Guest_Name";
	public static final String GUEST_STATUS = "Guest_Status";
	/* Name of the Database */
	private static final String DATABASE_NAME = "AnglesEvent.db";
	/* Version Number of the Database */
	private static final int DATABASE_VERSION = 1;

	
	/* This is a SQLite String that generates the Events table. */
	public static final String CREATE_EVENTS_TABLE = "CREATE TABLE " +
			 EVENTS_TABLE_NAME + " ( " + 
			 EVENT_ID + " TEXT PRIMARY KEY, " + 
			 HOST_NAME + " TEXT, " +
			 EVENT_NAME + " TEXT, " +
			 EVENT_DESCRIPTION + " TEXT, " +
			 START_DATE + " TEXT, " +
			 START_TIME + " TEXT, " +
			 END_DATE + " TEXT, " +
			 END_TIME + " TEXT" + ")";
	
	public static final String CREATE_GUESTS_TABLE = "CREATE TABLE " +
			GUESTS_TABLE_NAME + " ( " +
			EVENT_ID + " TEXT," + 
			GUEST_NAME + " TEXT, " +
			GUEST_STATUS + " TEXT);";
		
	public EventTable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_EVENTS_TABLE);
		db.execSQL(CREATE_GUESTS_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		Log.w(DEBUG_TAG, "Upgrading Database from Version " + oldVersion +
				" to Version " + newVersion + ". This will destroy all data.");
		
		db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
		
		onCreate(db);
	}
	
	public List<AnglesEvent> getEvents() {
		List<AnglesEvent> events = new ArrayList();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + EVENTS_TABLE_NAME, new String[]{});
		
		int eventIDIndex = cursor.getColumnIndex(EVENT_ID);
		int hostNameIndex = cursor.getColumnIndex(HOST_NAME);
		int eventNameIndex = cursor.getColumnIndex(EVENT_NAME);
		int eventDescriptionIndex = cursor.getColumnIndex(EVENT_DESCRIPTION);
		int startDateIndex = cursor.getColumnIndex(START_DATE);
		int startTimeIndex = cursor.getColumnIndex(START_TIME);
		int endDateIndex = cursor.getColumnIndex(END_DATE);
		int endTimeIndex = cursor.getColumnIndex(END_TIME);
		
		while(cursor.moveToNext()) {
			Calendar startTime = EventsManager.parseDateTime(cursor.getString(startDateIndex), cursor.getString(startTimeIndex));
			Calendar endTime = EventsManager.parseDateTime(cursor.getString(endDateIndex), cursor.getString(endTimeIndex));
			
			Cursor guestCursor = db.rawQuery("select * from " + GUESTS_TABLE_NAME + " where " 
					+ EVENT_ID + "='" + cursor.getString(eventIDIndex) + "'", new String[]{});
			int userIndex = guestCursor.getColumnIndex(GUEST_NAME);
			int statusIndex = guestCursor.getColumnIndex(GUEST_STATUS);
			
			Map<User, Attending> guests = new HashMap();
			
			while (guestCursor.moveToNext()) {
				guests.put(new User(guestCursor.getString(userIndex), ""),
						EventsManager.parseAttending(guestCursor.getString(statusIndex)));
			}
			
			events.add(new AnglesEvent(
					cursor.getString(eventNameIndex),
					cursor.getString(eventDescriptionIndex),
					startTime,
					endTime,
					new User(cursor.getString(hostNameIndex), ""),
					UUID.fromString(cursor.getString(eventIDIndex)),
					guests)
			);
		}
		cursor.close();
		return events;
	}

	/**
	 * NOTE: Does not insert guests
	 * @param event
	 */
	public void addEvent(AnglesEvent event) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(EVENT_ID, event.getEventID().toString());
		values.put(HOST_NAME, event.getHost().userName);
		values.put(EVENT_NAME, event.eventTitle);
		values.put(EVENT_DESCRIPTION, event.eventDescription);
		values.put(START_DATE, EventsManager.getDisplayDate(event.startTime));
		values.put(START_TIME, EventsManager.getDisplayTime(event.startTime));
		values.put(END_DATE, EventsManager.getDisplayDate(event.endTime));
		values.put(END_TIME, EventsManager.getDisplayTime(event.endTime));
		
		long result = db.insert(EVENTS_TABLE_NAME, null, values);
	}
	
	public void addGuest(String eventID, String guestName) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(EVENT_ID, eventID);
		values.put(GUEST_NAME, guestName);
		values.put(GUEST_STATUS, "UNDECIDED");
		
		db.insert(GUESTS_TABLE_NAME, null, values);
	}
	
	public void addGuests(String eventID, Map<User, Attending> guests) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (User user: guests.keySet()) {
			ContentValues values = new ContentValues();
			values.put(EVENT_ID, eventID);
			values.put(GUEST_NAME, user.userName);
			values.put(GUEST_STATUS, "UNDECIDED");
			db.insert(GUESTS_TABLE_NAME, null, values);
		}
	}
	
	public void updateGuestStatus(String guestName, String eventID, String status) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(GUEST_STATUS, status);

		db.update(GUESTS_TABLE_NAME, values, GUEST_NAME + "=" + guestName + " and " + EVENT_ID + " = " + eventID, new String[0]);
	}
}
