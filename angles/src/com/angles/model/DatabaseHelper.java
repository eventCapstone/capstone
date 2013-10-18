package com.angles.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	 
	/* Name of this Activity for debugging in Logcat */
	private static final String DEBUG_TAG= "DatabaseHelper";

	/* Name of the Database */
	private static final String DATABASE_NAME = "Angles";
	
	/* Version Number of the Database */
	private static final int DATABASE_VERSION = 1;
	
	/* Table Names */
	private static final String TABLE_EVENTS = "Events";
	
	/* Event Table Columns */
	private static final String KEY_EVENT_ID = "key_event_id";
	
	private static final String EVENT_TITLE = "event_title";
	
	private static final String HOST_ID = "host_id";
	
	private static final String START_TIME = "start_time";
	
	private static final String END_TIME = "end_time";
		
	private static final String BANNER_PICTURE = "banner_picture";
	
	private static final String DESCRIPTION = "description";
	
	/* Guest Table Columns 
	private static final String KEY_GUEST_ID = "key_invitee_id";
	
	private static final String EVENT_ID = "event_id";
	*/
			
	public DatabaseHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* This is a SQLite String that generates the Events table.
	 * Below is a sample row in this format.
	 * | EVENT_ID | EVENT_NAME | HOST_ID | START_TIME | END_TIME | LIST_OF_INVITEES | BANNER_PICTURE | DESCRIPTION |
	 */
	public static final String CREATE_EVENTS_TABLE = "CREATE TABLE " +
			 TABLE_EVENTS + " ( " + 
			 KEY_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 EVENT_TITLE + " TEXT, " + 
			 HOST_ID + "TEXT, " +
			 START_TIME + " INTEGER, " +
			 END_TIME + " INTEGER, " +
			 BANNER_PICTURE + " BLOB, " +
			 DESCRIPTION + " TEXT";
		
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_EVENTS_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		Log.w("LOG_TAG", "Upgrading Database from Version " + oldVersion +
				" to Version " + newVersion + ". This will destroy all data.");
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		
		onCreate(db);
	}
	
	public void createEvent(AnglesEvent event) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put(KEY_EVENT_ID, event.getEventID());
		
		values.put(EVENT_TITLE, event.getEventTitle());
		
		/* TODO: HOST_ID must be unique identifier of a User within
		 * the system. For now it's just "Joe".
		 */
		values.put(HOST_ID, "Joe");
		
		values.put(START_TIME, event.getStartTime());
		
		values.put(END_TIME, event.getEndTime());
	}
}
