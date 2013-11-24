package com.angles.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class EventTable {
	
	private static final String DEBUG_TAG = "EventTable";
	
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
		
	public static void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_EVENTS_TABLE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		Log.w(DEBUG_TAG, "Upgrading Database from Version " + oldVersion +
				" to Version " + newVersion + ". This will destroy all data.");
		
		db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE_NAME);
		
		onCreate(db);
	}
}
