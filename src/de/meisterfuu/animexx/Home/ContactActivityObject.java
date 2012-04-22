package de.meisterfuu.animexx.Home;

import android.util.Log;

public class ContactActivityObject {

	private String vonID, vonUsername, eventURL, eventID, eventTyp, time, text, beschreibung, imgURL;
	String s;
	
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

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}
	
}
