package com.angles.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EventDbHelper extends SQLiteOpenHelper {
	 
	/* Name of this Activity for debugging in Logcat */
	private static final String DEBUG_TAG= "EventDbHelper";

	/* Name of the Database */
	private static final String DATABASE_NAME = "Angles.db";
	
	/* Version Number of the Database */
	private static final int DATABASE_VERSION = 1;
	
	/* Table Name */
	public static final String EVENTS_TABLE_NAME = "Events";
	
	/* Table Columns */
	public static final String EVENTS_COL_ID = "_id";
	
	public static final String EVENTS_COL_HOST_NAME = "Host_Name";
	
	public static final String EVENTS_COL_EVENT_NAME = "Event_Name";
	
	public static final String EVENTS_COL_EVENT_DESCRIPTION = "Description";
	
	public static final String EVENTS_COL_START_TIME = "Start_Time";
	
	public static final String EVENTS_COL_END_TIME = "End_Time";
	
	public static final String EVENTS_COL_EVENT_PICTURE = "Event_Picture";
			
	public EventDbHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* This is a SQLite String that generates the Events table. */
	public static final String CREATE_EVENTS_TABLE = "CREATE TABLE " +
			 EVENTS_TABLE_NAME + " ( " + 
			 EVENTS_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			 EVENTS_COL_HOST_NAME + " TEXT, " + 
			 EVENTS_COL_EVENT_NAME + "TEXT, " +
			 EVENTS_COL_START_TIME + " DATETIME, " +
			 EVENTS_COL_END_TIME + " DATETIME, " +
			 EVENTS_COL_EVENT_DESCRIPTION + " TEXT" +
			 EVENTS_COL_EVENT_PICTURE + " BLOB" +
			 ");";
		
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_EVENTS_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		Log.w(DEBUG_TAG, "Upgrading Database from Version " + oldVersion +
				" to Version " + newVersion + ". This will destroy all data.");
		
		db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
		
		onCreate(db);
	}
}
