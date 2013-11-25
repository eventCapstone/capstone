package com.angles.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	/* Name of this Activity for debugging in Logcat */
	private static final String DEBUG_TAG= "DatabaseHelper";

	/* Name of the Database */
	private static final String DATABASE_NAME = "Angles.db";
	
	/* Version Number of the Database */
	private static final int DATABASE_VERSION = 1;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public void onCreate(SQLiteDatabase db) {
		EventTable.onCreate(db);
		//ContactTable.onCreate(db);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		EventTable.onUpgrade(db, oldVersion, newVersion);
		//ContactTable.onCreate(db, oldVersion, newVersion);
	}
}
