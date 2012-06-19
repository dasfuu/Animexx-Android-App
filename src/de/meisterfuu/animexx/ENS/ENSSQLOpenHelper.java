package de.meisterfuu.animexx.ENS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ENSSQLOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_ENS = "ENS";
	
	public static final String TABLE_ORDNER = "FOLDER";
	public static final String COLUMN_F_ID = "_id";
	

	public static final String COLUMN_ENS_ID = "_id";
	public static final String COLUMN_BETREFF = "betreff";
	public static final String COLUMN_TEXT = "content";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_SIGNATUR = "signatur";
	public static final String COLUMN_REFERENZ_ENS_ID = "referenz";
	
	public static final String COLUMN_ORDNER = "ordner";
	public static final String COLUMN_TYP = "typ";
	public static final String COLUMN_FLAGS = "flags";
	public static final String COLUMN_KONVERSATION = "konversation";
	public static final String COLUMN_AN_VON = "anvon";
	
	public static final String COLUMN_VON = "von";
	public static final String COLUMN_AN = "an";
	
	public static final String COLUMN_VON_ID = "von_id";
	public static final String COLUMN_AN_ID = "an_id";
	
	private static final String DATABASE_NAME = "animexx.db";
	private static final int DATABASE_VERSION = 7;

	/*
	 * 	private String Betreff, Signatur, ENS_id, Time, Referenz;
	 *	private String Text = "";
	 *	private int Flags, Konversation, id, Ordner, Typ;
	 * 
	 */
	
	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_ENS 
			+ "( " 
			+ COLUMN_ENS_ID + " integer primary key, "
			+ COLUMN_BETREFF + " text not null, "
			+ COLUMN_TEXT + " text, "
			+ COLUMN_TIME + " text not null, "
			+ COLUMN_AN_VON + " text not null, "
			+ COLUMN_VON + " text not null, "
			+ COLUMN_AN + " text not null, "
			+ COLUMN_VON_ID + " text not null, "
			+ COLUMN_AN_ID + " text not null, "
			+ COLUMN_ORDNER + " integer not null, "
			+ COLUMN_SIGNATUR + " text, "
			+ COLUMN_FLAGS + " integer, "		
			+ COLUMN_KONVERSATION + " integer, "	
			+ COLUMN_TYP + " integer, "	
			+ COLUMN_REFERENZ_ENS_ID + " text "	
			+ ");";
	
	private static final String DATABASE_CREATE_FOLDER = "create table "
			+ TABLE_ORDNER 
			+ "( " 
			+ COLUMN_F_ID + " integer primary key, "
			+ COLUMN_BETREFF + " text not null, "
			+ COLUMN_AN_VON + " text not null "
			+ ");";

	public ENSSQLOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(DATABASE_CREATE_FOLDER);
		//Log.i("SQL_HELPER", "Datenbank ist leer: "+database.query(ENSSQLOpenHelper.TABLE_ENS, null, null, null, null, null, null).isAfterLast());
				
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("SQL", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDNER);
		
		onCreate(db);
	}

}