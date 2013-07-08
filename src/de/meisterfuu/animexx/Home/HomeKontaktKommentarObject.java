package de.meisterfuu.animexx.Home;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;


public class HomeKontaktKommentarObject {

	String comment;
	UserObject von;
	String id;
	String date;
	
	//- id: int: Die ID des Kommentars.
	//- mitglied: User-Object: Der Benutzer, von dem der Kommentar geschrieben wurde.
	//- datum_server: MySQL-Datum / ME(S)Z
	//- datum_utc: MySQL-Datum / UTC
	//- text: string: Der Text des Kommentars
	
	public HomeKontaktKommentarObject(JSONObject jsonObject) {
		parseJSON(jsonObject);
	}

	public void parseJSON(JSONObject obj){
		try {
			this.setId(obj.getString("id"));
			this.setDate(obj.getString("datum_server"));
			this.setComment(obj.getString("text"));
			this.setVon(new UserObject());
			this.getVon().ParseJSON(obj.getJSONObject("mitglied"));			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public UserObject getVon() {
		return von;
	}
	
	public void setVon(UserObject von) {
		this.von = von;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
}
