package de.meisterfuu.animexx.events;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class EventSQLHelper{

	public static final String TABLE_EVENTS = "EVENT";
	public static final String TABLE_EVENTS_NOTES = "EVENT_NOTES";

	SQLiteDatabase db;
	EventSQL db_helper;

	public EventSQLHelper(Context context) {
		//this.context = context;
		db_helper = new EventSQL(context);
	}

	public void open() throws SQLException {
		db = db_helper.getWritableDatabase();
	}

	public void close() {
		db.close();
		db_helper.close();
	}

	public long updateEvent(EventObject Event) {
		ContentValues values = new ContentValues();
		values.put(EventSQL.COLUMN_EVENT_ID, Event.getId());
		try {
			values.put(EventSQL.COLUMN_JSON, Event.getDetail_json().toString());
		} catch (JSONException e) {
			Log.i("SQL","EVENT JSON PARSE ERROR");
			return 0L;			
		}

		//db.insert(table, nullColumnHack, values)		
		long  a;

		a = db.update(EventSQL.TABLE_EVENTS, values, EventSQL.COLUMN_EVENT_ID+"=?", new String[]{Long.toString(Event.getId())});
		if (a == 0) {
			a = db.insert(EventSQL.TABLE_EVENTS, null, values);
			Log.i("SQL","InsertEVENT: "+a);
		} else Log.i("SQL","updateEVENT: "+a);

		return a;
	}

	public long updateNote(String Note, long id) {

		ContentValues values = new ContentValues();
		values.put(EventSQL.COLUMN_EVENT_ID, id);
		values.put(EventSQL.COLUMN_NOTE, Note);

		//db.insert(table, nullColumnHack, values)		
		long  a;

		a = db.update(EventSQL.TABLE_EVENTS_NOTES, values, EventSQL.COLUMN_EVENT_ID+"=?", new String[]{Long.toString(id)});
		if (a == 0) {
			a = db.insert(EventSQL.TABLE_EVENTS_NOTES, null, values);
			Log.i("SQL","InsertEVENT: "+a);
		} else Log.i("SQL","updateEVENT: "+a);

		return a;
	}


	public void deleteNote(Long Event_id) {
		db.delete(EventSQL.TABLE_EVENTS_NOTES, EventSQL.COLUMN_EVENT_ID
				+ " = " + Event_id, null);
	}


	public ArrayList<EventObject> getAllEvents() {
		ArrayList<EventObject> EVENTS = new ArrayList<EventObject>();

		Cursor cursor = db.query(EventSQL.TABLE_EVENTS,
				null, null, null, null, null, EventSQL.COLUMN_EVENT_ID+" DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EventObject temp = cursorToEvent(cursor);

			if(temp != null)
				EVENTS.add(temp);

			cursor.moveToNext();
		}

		cursor.close();
		return EVENTS;
	}

	public EventObject getSingleEvent(Long EVENT_ID){
		
		ArrayList<EventObject> EVENTS = new ArrayList<EventObject>();

		
		Cursor cursor = db.query(EventSQL.TABLE_EVENTS, null,
				EventSQL.COLUMN_EVENT_ID+"=?", new String[]{Long.toString(EVENT_ID)}, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EventObject temp = cursorToEvent(cursor);

			if(temp != null)
				EVENTS.add(temp);

			cursor.moveToNext();
		}

		cursor.close();
		
		if (EVENTS.size() != 0) return EVENTS.get(0);

		return null;
	}
	
	public String getSingleNote(Long EVENT_ID){
		ArrayList<String> NOTES = new ArrayList<String>();

		
		Cursor cursor = db.query(EventSQL.TABLE_EVENTS_NOTES, null,
				EventSQL.COLUMN_EVENT_ID+"=?", new String[]{Long.toString(EVENT_ID)}, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String temp = cursorToNote(cursor);

			if(temp != null)
				NOTES.add(temp);

			cursor.moveToNext();
		}

		cursor.close();
		
		if (NOTES.size() != 0) return NOTES.get(0);

		return null;		
	}


	private EventObject cursorToEvent(Cursor cursor) {
		EventObject Event = new EventObject();
		try {
			Event.parseJSON((new JSONObject(cursor.getString(1))));
		} catch (JSONException e) {
			return null;
		}
		return Event;
	}
	
	
	private String cursorToNote(Cursor cursor) {
		String Note;
		Note = cursor.getString(1);
		return Note;
	}


}
