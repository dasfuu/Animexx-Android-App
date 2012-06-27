package de.meisterfuu.animexx.other;

import org.json.JSONException;
import org.json.JSONObject;

public class UserObject implements Comparable<Object> {

	private String id, username, picture;
	private boolean SteckbriefFreigabe;
	
	public void ParseJSON(JSONObject user){
		setPicture(null);
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
		setPicture(null);
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

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int compareTo(Object o2) {

		return this.getUsername().compareTo(((UserObject) o2).getUsername());
			
	}
	
	
}
