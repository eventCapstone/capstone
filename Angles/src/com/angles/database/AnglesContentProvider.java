package com.angles.database;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class AnglesContentProvider extends ContentProvider {
	
	private static final String DEBUG_TAG = "AnglesContentProvider";

	/* An instance of our database, via a helper */
	private DatabaseHelper database;
	
	/* Result codes for URI matcher */
	private static final int EVENTS= 10;
	private static final int EVENTS_ID = 20;
	
	private static final String AUTHORITY = "com.angles.database.contentprovider";
	
	private static final String BASE_PATH = "events";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
	      + "/" + BASE_PATH);
	
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
	      + "/events";
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
	      + "/events";
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  
		static {
			sURIMatcher.addURI(AUTHORITY, BASE_PATH, EVENTS);
			
			sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EVENTS_ID);
		}
	
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		int uriType = sURIMatcher.match(uri);
		
	    SQLiteDatabase db = database.getWritableDatabase();
	    
	    int rowsDeleted = 0;
	    
	    switch (uriType) {
	    
	    	case EVENTS:
	    		rowsDeleted = db.delete(EventTable.CREATE_EVENTS_TABLE, selection, selectionArgs);
	    		break;
	    		
	    	case EVENTS_ID:
	    		
	    		String id = uri.getLastPathSegment();
	    		
	    		if (TextUtils.isEmpty(selection)) {
	    			rowsDeleted = db.delete(
	    					EventTable.CREATE_EVENTS_TABLE, EventTable.EVENTS_COL_ID + "=" + id, null);
	    		}
	    		else {
	    			rowsDeleted = db.delete(EventTable.EVENTS_TABLE_NAME, 
	    					EventTable.EVENTS_COL_ID + "=" + id + " and " + selection, 
	    					selectionArgs);
	    		}
	    		break;
	    		
	    default:
	    	throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(uri, null);
	    
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		int uriType = sURIMatcher.match(uri);
		
	    SQLiteDatabase db = database.getWritableDatabase();
	    	    
	    long id = 0;
	    
	    switch (uriType) {
	    
	    	case EVENTS:
	    		id = db.insert(EventTable.EVENTS_TABLE_NAME, null, values);
	    		break;
	    		
	    	default:
	    		throw new IllegalArgumentException("Unknown URI: " + uri);
	    }
	    
	    getContext().getContentResolver().notifyChange(uri, null);
	    
	    return Uri.parse(BASE_PATH + "/" + id);
	}

	public boolean onCreate() {

		database = new DatabaseHelper(getContext());
		
		return false;
	}

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		/* Check if a query is being requested with a column that 
		 * does not exist. */
		checkColumns(projection);
		
		/* Set the Table */
		queryBuilder.setTables(EventTable.EVENTS_TABLE_NAME);
		
		int uriType = sURIMatcher.match(uri);
		
		switch(uriType) {
		
			case EVENTS: break;
			
			case EVENTS_ID:
				queryBuilder.appendWhere(EventTable.EVENTS_COL_ID + "=" + uri.getLastPathSegment());
				
				break;
				
			default:
				throw new IllegalArgumentException(DEBUG_TAG + ", Unknown URI: " + uri);
		}
		
		SQLiteDatabase db = database.getWritableDatabase();
		
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		int uriType = sURIMatcher.match(uri);
		
		SQLiteDatabase db = database.getWritableDatabase();
		
		int rowsUpdated = 0;
		
		switch (uriType) {
			case EVENTS:
				rowsUpdated = db.update(EventTable.EVENTS_TABLE_NAME, 
		        values, 
		        selection,
		        selectionArgs);
		      break;
		      
		    case EVENTS_ID:
		    	String id = uri.getLastPathSegment();
		    	
		    	if (TextUtils.isEmpty(selection)) {
		    		
		    		rowsUpdated = db.update(EventTable.EVENTS_TABLE_NAME, 
		            values,
		            EventTable.EVENTS_COL_ID + "=" + id, 
		            null);
		    	}
		    	else {
		    		
		    		rowsUpdated = db.update(EventTable.EVENTS_TABLE_NAME, 
		            values,
		            EventTable.EVENTS_COL_ID + "=" + id + " and " + selection,
		            selectionArgs);
		      }
		    	break;
		    	
		    default:
		    	throw new IllegalArgumentException("Unknown URI: " + uri);
		    }
		
		    getContext().getContentResolver().notifyChange(uri, null);
		    
		    return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
		
		String[] available = { EventTable.EVENTS_COL_EVENT_NAME,
				EventTable.EVENTS_COL_HOST_NAME,
				EventTable.EVENTS_COL_START_TIME,
				EventTable.EVENTS_COL_END_TIME,
				EventTable.EVENTS_COL_EVENT_DESCRIPTION,
				EventTable.EVENTS_COL_EVENT_PICTURE };
		
		if (projection != null) {
			
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			
		    HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
		    
		    // check if all columns which are requested are available
		    if (!availableColumns.containsAll(requestedColumns)) {
		    	
		        throw new IllegalArgumentException("Unknown columns in projection");
		    }
		}
	}
}