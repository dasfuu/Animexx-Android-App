package de.meisterfuu.animexx.Home;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.Helper;

import android.util.Log;

public class ContactActivityObject extends HomeListObject {

	private String vonID, vonUsername, eventURL, eventID, eventTyp, time, text, beschreibung, imgURL;
	String s;
	long timestamp;
	
	public ContactActivityObject(){
		vonID = "none";
		vonUsername = "Abgemeldet";
		eventURL = "";
		eventID = "none";
		eventTyp = "none";
		imgURL = "none";
		time = "";
		text = "";
		beschreibung = "";
		s = null;
	}
	
	public String getFinishedText() {
		if(s == null){
			s = new String(text);
			s = s.replace("%username%", vonUsername);
			s = s.replace("%detail%", beschreibung);
			Log.i("ContactActivity", s);
		}

		return s;
	}
	
	public void refresh(){
		s = new String(text);
		s.replace("%username%", vonUsername);
		s.replace("%detail%", beschreibung);
	}

	public String getVonID() {
		return vonID;
	}

	public void setVonID(String vonID) {
		this.vonID = vonID;
	}

	public String getVonUsername() {
		return vonUsername;
	}

	public void setVonUsername(String vonUsername) {
		this.vonUsername = vonUsername;
	}

	public String getEventURL() {
		return eventURL;
	}

	public void setEventURL(String eventURL) {
		this.eventURL = "http://animexx.onlinewelten.com"+eventURL;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public String getEventTyp() {
		return eventTyp;
	}

	public void setEventTyp(String eventTyp) {
		this.eventTyp = eventTyp;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
		this.timestamp = Helper.toTimestamp(time);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	@Override
	public int getTyp() {
		return HomeListObject.KONTAKTE;
	}

	@Override
	public long getTimeStamp() {
		return timestamp;
	}

	@Override
	public void parseJSON(JSONObject o) {
		
		try {
			this.setText(o.getString("std_text"));
			this.setBeschreibung(o.getString("beschreibung"));
			this.setEventID(o.getString("event_id"));
			this.setEventTyp(o.getString("event_typ"));
			this.setEventURL(o.getString("link"));
			if(o.has("img_url")) this.setImgURL(o.getString("img_url"));
			this.setTime(o.getString("datum"));
			this.setVonID(o.getString("event_von"));
			this.setVonUsername(o.getString("event_von_username"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return eventURL;
	}

	@Override
	public String getImgURL() {
		return imgURL;
	}

	

	
}
