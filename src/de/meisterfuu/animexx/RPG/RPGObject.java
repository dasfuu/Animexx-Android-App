package de.meisterfuu.animexx.RPG;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import de.meisterfuu.animexx.other.UserObject;

/*
- id: int, ID des RPGs
- name: string, Name des RPGs
- postings: int, Anzahl der Postings im RPG
- letzt_datum_server: MySQL-Datum / Datum des aktuellsten Postings, ME(S)Z
- letzt_datum_utc: MySQL-Datum / Datum des aktuellsten Postings, UTC
- letzt_charakter: string, Name des Charakters des aktuellsten Postings
- letzt_spieler: string, Benutzername des Spielers des aktuellsten Postings
- tofu: 1 / 0, Handelt es sich um ein vertofuziertes RPG? (1 = ja, 0 = nein)
*/

public class RPGObject {
	
	long id;
	String name, lastUpdate, lastChar, description;
	UserObject lastUser;
	int PostCount;
	boolean tofu;
	
	public RPGObject(){
		id = -1L;
		name = "Unbekannt";
		lastUser = new UserObject();
		lastChar = "Unbekannt";
		tofu = false;
	}
	
	public void parseJSON(JSONObject o){
			
		try {
			this.setId(o.getLong("id"));
			this.setName(o.getString("name"));
			this.setPostCount(o.getInt("postings"));
			this.setLastChar(o.getString("letzt_charakter"));
			this.setLastUpdate(o.getString("letzt_datum_server"));
			this.getLastUser().setUsername(o.getString("letzt_spieler"));
			if(o.getInt("tofu") == 1) this.setTofu(true); else this.setTofu(false);
			
			Log.i("RPG",this.getName());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
	}	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public UserObject getLastUser() {
		return lastUser;
	}
	public void setLastUser(UserObject lastUser) {
		this.lastUser = lastUser;
	}
	public String getLastChar() {
		return lastChar;
	}
	public void setLastChar(String lastChar) {
		this.lastChar = lastChar;
	}
	public int getPostCount() {
		return PostCount;
	}
	public void setPostCount(int postCount) {
		PostCount = postCount;
	}
	public boolean isTofu() {
		return tofu;
	}
	public void setTofu(boolean tofu) {
		this.tofu = tofu;
	}
	
}
