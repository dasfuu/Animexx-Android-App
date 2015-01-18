package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import de.meisterfuu.animexx.other.UserObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		db.close();
		db_helper.close();
	}

	public long updateENS(ENSObject ENS, boolean text) {

		ContentValues values = new ContentValues();
		values.put(ENSSQLOpenHelper.COLUMN_ENS_ID, ENS.getENS_id());
		values.put(ENSSQLOpenHelper.COLUMN_BETREFF, ENS.getBetreff());

		if(text){
		values.put(ENSSQLOpenHelper.COLUMN_TEXT, ENS.getText());
		values.put(ENSSQLOpenHelper.COLUMN_SIGNATUR, ENS.getSignatur());
		}
		values.put(ENSSQLOpenHelper.COLUMN_TIME, ENS.getTime());
		values.put(ENSSQLOpenHelper.COLUMN_AN_VON, ENS.getAnVon());
		values.put(ENSSQLOpenHelper.COLUMN_ORDNER, ENS.getOrdner());
		values.put(ENSSQLOpenHelper.COLUMN_FLAGS, ENS.getFlags());
		values.put(ENSSQLOpenHelper.COLUMN_KONVERSATION, ENS.getKonversation());
		values.put(ENSSQLOpenHelper.COLUMN_REFERENZ_ENS_ID, ENS.getReferenz());
		values.put(ENSSQLOpenHelper.COLUMN_TYP, ENS.getTyp());

		Joiner joiner = Joiner.on(", ").skipNulls();
		values.put(ENSSQLOpenHelper.COLUMN_AN, joiner.join(ENS.getAnArray()));
		values.put(ENSSQLOpenHelper.COLUMN_VON, ENS.getVon().getUsername());

		String[] id = new String[ENS.getAnArray().length];
		for(int i = 0; i < ENS.getAnArray().length; i++)
		{
				id[i] = ENS.getAnArray()[i].getId();
		}

		joiner = Joiner.on(", ").skipNulls();
		values.put(ENSSQLOpenHelper.COLUMN_AN_ID, joiner.join(id));
		values.put(ENSSQLOpenHelper.COLUMN_VON_ID, ENS.getVon().getId());

		//db.insert(table, nullColumnHack, values)
		//return db.update(ENSSQLOpenHelper.TABLE_ENS, null,	values);
		long  a;

		a = db.update(ENSSQLOpenHelper.TABLE_ENS, values, ENSSQLOpenHelper.COLUMN_ENS_ID+"=?", new String[]{Long.toString(ENS.getENS_id())});
		if (a == 0) {
			a = db.insert(ENSSQLOpenHelper.TABLE_ENS, null,	values);
			Log.i("SQL","InsertENS: "+a);
		} else Log.i("SQL","updateENS: "+a);

		return a;
	}

	public long createFolder(ENSObject ENS) {

		ContentValues values = new ContentValues();
		values.put(ENSSQLOpenHelper.COLUMN_F_ID, ENS.getENS_id());
		values.put(ENSSQLOpenHelper.COLUMN_BETREFF, ENS.getBetreff());
		values.put(ENSSQLOpenHelper.COLUMN_AN_VON, ENS.getOrdner());

		//db.insert(table, nullColumnHack, values)
		return db.insert(ENSSQLOpenHelper.TABLE_ORDNER, null,	values);
	}


	public void deleteENS(Long ENS_id) {
		db.delete(ENSSQLOpenHelper.TABLE_ENS, ENSSQLOpenHelper.COLUMN_ENS_ID
				+ " = " + ENS_id, null);
	}

	public void clearFolder(String typ) {
		db.delete(ENSSQLOpenHelper.TABLE_ORDNER, ENSSQLOpenHelper.COLUMN_AN_VON
				+ " = " + typ, null);
	}

	public void clearFolder() {
		db.delete(ENSSQLOpenHelper.TABLE_ORDNER, null, null);
	}

	public ArrayList<ENSObject> getAllENS(long folder) {
		ArrayList<ENSObject> ENS = new ArrayList<ENSObject>();

		Cursor cursor = db.query(ENSSQLOpenHelper.TABLE_ENS,
				null, ENSSQLOpenHelper.COLUMN_ORDNER+"=?", new String[]{Long.toString(folder)}, null, null, ENSSQLOpenHelper.COLUMN_TIME+" DESC");

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ENSObject temp = cursorToENS(cursor);

			if(temp != null && temp.getText() != "" && temp.getText() != null)
				ENS.add(temp);


			cursor.moveToNext();
		}

		cursor.close();
		return ENS;
	}

	public ArrayList<ENSObject> getAllFolder(String AnVon) {
		ArrayList<ENSObject> ENS = new ArrayList<ENSObject>();

		Cursor cursor = db.query(ENSSQLOpenHelper.TABLE_ORDNER,
				null, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ENSObject temp = cursorToFolder(cursor);
			if(temp.getAnVon().equalsIgnoreCase(AnVon)) ENS.add(temp);
			cursor.moveToNext();
		}

		cursor.close();
		return ENS;
	}

	public ENSObject getSingleENS(Long ENS_ID){
		List<ENSObject> ENS = new ArrayList<ENSObject>();

		Cursor cursor = db.query(ENSSQLOpenHelper.TABLE_ENS, null,
				ENSSQLOpenHelper.COLUMN_ENS_ID+"=?", new String[]{Long.toString(ENS_ID)}, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ENSObject temp = cursorToENS(cursor);
			ENS.add(temp);
			cursor.moveToNext();
		}

		cursor.close();
		if (ENS.size() != 0) return ENS.get(0);

		return null;
	}

	private ENSObject cursorToENS(Cursor cursor) {
		ENSObject ENS = new ENSObject();
		//if(cursor.getString(3) != null || cursor.getString(3).equals("") == false || cursor.getString(3).equals("NULL")){
			ENS.setText(cursor.getString(2));
			ENS.setSignatur(cursor.getString(10));
		//} else {
		//	ENS.setText("");
		//	ENS.setSignatur("");
		//}
		ENS.setENS_id(cursor.getLong(0));
		ENS.setBetreff(cursor.getString(1));
		ENS.setTime(cursor.getString(3));
		ENS.setAnVon(cursor.getString(4));
		ENS.setOrdner(cursor.getInt(9));
		ENS.setFlags(cursor.getInt(11));
		ENS.setKonversation(cursor.getInt(12));
		ENS.setTyp(cursor.getInt(13));
		ENS.setReferenz(cursor.getLong(14));

		UserObject temp = new UserObject();
		temp.setId(cursor.getString(7));
		temp.setUsername(cursor.getString(5));
		ENS.setVon(temp);

		Iterable<String> user = Splitter.on(CharMatcher.anyOf(";,")).trimResults().omitEmptyStrings().split(cursor.getString(8));
		Iterable<String> username = Splitter.on(CharMatcher.anyOf(";,")).trimResults().omitEmptyStrings().split(cursor.getString(6));
		ArrayList<String> ID = new ArrayList<String>();
		ArrayList<String> NAME = new ArrayList<String>();

		for (String element : user) {
			ID.add(element);
		}

		for (String element : username) {
			NAME.add(element);
		}

		for (int i = 0; i < NAME.size(); i++) {
			UserObject tUserObject = new UserObject();
			tUserObject.setId("" + ID.get(i));
			tUserObject.setUsername(NAME.get(i));
			ENS.addAnUser(tUserObject);
		}

		return ENS;
	}

	private ENSObject cursorToFolder(Cursor cursor) {
		ENSObject ENS = new ENSObject();
		ENS.setBetreff(cursor.getString(1));
		ENS.setENS_id(cursor.getLong(0));
		ENS.setTyp(99);
		ENS.setOrdner(cursor.getInt(2));
		if(ENS.getOrdner() == 1) ENS.setAnVon("an");
		else ENS.setAnVon("von");
		ENS.setSignatur("0");


		return ENS;
	}


}
