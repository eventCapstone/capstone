package com.angles.database;

import java.util.HashSet;
import java.util.Set;

import com.angles.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbHelper extends SQLiteOpenHelper {
	 
	/* Name of this Activity for debugging in Logcat */
	private static final String DEBUG_TAG= "DatabaseHelper";

	/* Name of the Database */
	private static final String DATABASE_NAME = "Angles.db";
	
	/* Version Number of the Database */
	private static final int DATABASE_VERSION = 1;
	
	/* Table Names */
	private static final String TABLE_CONTACTS = "Contacts";
	
	/* Contact Table Columns */
	private static final String USER_NAME = "user_name";
	
	private static final String EMAIL = "email";
	
	private static final String PHONE_NUMBER = "phone_number";
	
	/* Guest Table Columns 
	private static final String KEY_GUEST_ID = "key_invitee_id";
	
	private static final String EVENT_ID = "event_id";
	*/
			
	public ContactDbHelper(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* This is a SQLite String that generates the Events table.
	 * Below is a sample row in this format.
	 * | EVENT_ID | EVENT_NAME | HOST_ID | START_TIME | END_TIME | LIST_OF_INVITEES | BANNER_PICTURE | DESCRIPTION |
	 */
	public static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " +
			 TABLE_CONTACTS + " ( " + 
			 USER_NAME + " TEXT PRIMARY KEY, " + 
			 EMAIL + "TEXT, " +
			 PHONE_NUMBER + " TEXT";
		
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CONTACTS_TABLE);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		Log.w("LOG_TAG", "Upgrading Database from Version " + oldVersion +
				" to Version " + newVersion + ". This will destroy all data.");
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		
		onCreate(db);
	}
	
	public void addContact(User user) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();

		values.put(USER_NAME, user.userName);
		values.put(EMAIL, user.email);
		values.put(PHONE_NUMBER, user.phoneNumber);
		
		db.insert(TABLE_CONTACTS, null, values);
	}
	
	public Set<User> getContacts() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + TABLE_CONTACTS, new String[]{});
		
		int userNameIndex = cursor.getColumnIndex(USER_NAME);
		int emailIndex = cursor.getColumnIndex(EMAIL);
		int phoneNumberIndex = cursor.getColumnIndex(PHONE_NUMBER);
				
		Set<User> contacts = new HashSet();
		
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			contacts.add(new User(
					cursor.getString(userNameIndex),
					cursor.getString(emailIndex),
					cursor.getString(phoneNumberIndex)));
			cursor.moveToNext();
		}
		
		return contacts;
	}
}
