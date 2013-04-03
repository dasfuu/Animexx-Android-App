package de.meisterfuu.animexx.events;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class EventSQL extends SQLiteOpenHelper {

	public static final String TABLE_EVENTS = "EVENT";
	public static final String TABLE_EVENTS_NOTES = "EVENT_NOTES";
	public static final String COLUMN_EVENT_ID = "_id";


	public static final String COLUMN_JSON = "JSON";
	public static final String COLUMN_NOTE = "note";
	public static final String COLUMN_DATE = "date";

	private static final String DATABASE_NAME = "animexx_events.db";
	private static final int DATABASE_VERSION = 9;


	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_EVENTS
			+ "( "
			+ COLUMN_EVENT_ID + " integer primary key, "
			+ COLUMN_JSON + " text not null "
			+ ");";

	private static final String DATABASE_CREATE_NOTES = "create table "
			+ TABLE_EVENTS_NOTES
			+ "( "
			+ COLUMN_EVENT_ID + " integer primary key, "
			+ COLUMN_NOTE + " text"
			+ ");";

	public EventSQL(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE_NOTES);
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("SQL", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_NOTES);

		onCreate(db);
	}

}
