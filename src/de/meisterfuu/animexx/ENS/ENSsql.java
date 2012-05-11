package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.other.UserObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ENSsql {

	public static final String TABLE_ENS = "ENS";


	/*
		COLUMN_ENS_ID 1
		COLUMN_BETREFF 2
		COLUMN_TEXT 3
		COLUMN_TIME 4
		COLUMN_AN_VON 5
		COLUMN_VON 6
		COLUMN_AN 7
		COLUMN_VON_ID 8
		COLUMN_AN_ID 9
		COLUMN_ORDNER 10
		COLUMN_SIGNATUR 11 
		COLUMN_FLAGS 12	
		COLUMN_KONVERSATION 13
		COLUMN_TYP 14
		COLUMN_REFERENZ_ENS_ID 15
	 */
	
	SQLiteDatabase db;
	ENSSQLOpenHelper db_helper;

	public ENSsql(Context context) {			
		//this.context = context;
		db_helper = new ENSSQLOpenHelper(context);		
	}
	
	public void open() throws SQLException {
		db = db_helper.getWritableDatabase();
	}

	public void close() {
		db_helper.close();
	}

	public long createENS(ENSObject ENS) {
		
		ContentValues values = new ContentValues();
		values.put(ENSSQLOpenHelper.COLUMN_ENS_ID, ENS.getENS_id());
		values.put(ENSSQLOpenHelper.COLUMN_BETREFF, ENS.getBetreff());
		values.put(ENSSQLOpenHelper.COLUMN_TEXT, ENS.getText());
		values.put(ENSSQLOpenHelper.COLUMN_TIME, ENS.getTime());
		values.put(ENSSQLOpenHelper.COLUMN_AN_VON, ENS.getAnVon());
		values.put(ENSSQLOpenHelper.COLUMN_ORDNER, ENS.getOrdner());
		values.put(ENSSQLOpenHelper.COLUMN_FLAGS, ENS.getFlags());
		values.put(ENSSQLOpenHelper.COLUMN_KONVERSATION, ENS.getKonversation());
		values.put(ENSSQLOpenHelper.COLUMN_REFERENZ_ENS_ID, ENS.getReferenz());
		values.put(ENSSQLOpenHelper.COLUMN_SIGNATUR, ENS.getSignatur());
		values.put(ENSSQLOpenHelper.COLUMN_TYP, ENS.getTyp());
		
		values.put(ENSSQLOpenHelper.COLUMN_AN, ENS.getAnString());
		values.put(ENSSQLOpenHelper.COLUMN_VON, ENS.getVon().getUsername());
		
		values.put(ENSSQLOpenHelper.COLUMN_AN_ID, ENS.getAnIDString());
		values.put(ENSSQLOpenHelper.COLUMN_VON_ID, ENS.getVon().getId());
		
		//db.insert(table, nullColumnHack, values)		
		return db.insert(ENSSQLOpenHelper.TABLE_ENS, null,	values);
	}

	
	public void deleteComment(String ENS_id) {
		System.out.println("Comment deleted with ENS_id: " + ENS_id);
		db.delete(ENSSQLOpenHelper.TABLE_ENS, ENSSQLOpenHelper.COLUMN_ID
				+ " = " + ENS_id, null);
	}

	public List<ENSObject> getAllENS() {
		List<ENSObject> ENS = new ArrayList<ENSObject>();

		Cursor cursor = db.query(ENSSQLOpenHelper.TABLE_ENS,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ENSObject temp = cursorToENS(cursor);
			ENS.add(temp);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return ENS;
	}
	
	public List<ENSObject> getAllFolder() {
		List<ENSObject> ENS = new ArrayList<ENSObject>();

		Cursor cursor = db.query(ENSSQLOpenHelper.TABLE_ORDNER,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ENSObject temp = cursorToFolder(cursor);
			ENS.add(temp);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return ENS;
	}

	private ENSObject cursorToENS(Cursor cursor) {
		ENSObject ENS = new ENSObject();
		ENS.setENS_id(cursor.getString(1));
		ENS.setBetreff(cursor.getString(2));
		ENS.setText(cursor.getString(3));
		ENS.setAnVon(cursor.getString(5));
		ENS.setOrdner(cursor.getInt(10));
		ENS.setSignatur(cursor.getString(11));
		ENS.setFlags(cursor.getInt(12));
		ENS.setKonversation(cursor.getInt(13));
		ENS.setTyp(cursor.getInt(14));
		ENS.setReferenz(cursor.getString(15));
		
		UserObject temp = new UserObject();
		temp.setId(cursor.getString(8));
		temp.setUsername(cursor.getString(6));		
		ENS.setVon(temp);
		
		temp = new UserObject();
		temp.setId(cursor.getString(9));
		temp.setUsername(cursor.getString(7));
		ENS.addAnUser(temp);
		
		return ENS;
	}
	
	private ENSObject cursorToFolder(Cursor cursor) {
		ENSObject ENS = new ENSObject();
		ENS.setBetreff(cursor.getString(2));
		ENS.setENS_id(cursor.getString(1));
		ENS.setTyp(99);
		if(cursor.getString(3).equalsIgnoreCase("Von")){
			ENS.setOrdner(2);
		} else {
			ENS.setOrdner(1);
		}

		
		return ENS;
	}
	

}
