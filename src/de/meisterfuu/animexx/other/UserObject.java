package de.meisterfuu.animexx.other;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UserObject {

	private String id, username;
	private boolean SteckbriefFreigabe;
	
	public void ParseJSON(JSONObject user){
		//Fehler durch abgemeldete User abfangen
		try {
			setId(user.getString("id"));
			setUsername(user.getString("username"));
			setSteckbriefFreigabe(user.getBoolean("steckbrief_freigabe"));
		} catch (JSONException e) {
			setId("none");
			setUsername("Abgemeldet");
			setSteckbriefFreigabe(false);
		}
	}
	
	public UserObject() {
		setId("none");
		setUsername("Abgemeldet");
		setSteckbriefFreigabe(false);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean getSteckbriefFreigabe() {
		return SteckbriefFreigabe;
	}

	public void setSteckbriefFreigabe(boolean steckbriefFreigabe) {
		SteckbriefFreigabe = steckbriefFreigabe;
	}
	
	
}
