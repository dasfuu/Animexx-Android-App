package de.meisterfuu.animexx.RPG;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class RPGPostObject {

	long id;
	String Post;
	String Chara;
	String Time;
	String avatar_url;
	long avatar_id;
	long Chara_id;
	boolean action, in_time;
	UserObject User;


	public RPGPostObject() {
		User = new UserObject();
		Chara = "Unbekannt";
	}


	public void parseJSON(JSONObject o) {
		/*
		 * - pos: int, Ordnungsnummer des Postings. Es gilt: from_pos <= pos < (from_pos + limit)
		 * - datum_server: MySQL-Datum / Datum des Postings, ME(S)Z
		 * - datum_utc: MySQL-Datum / Datum des Postings, UTC
		 * - text_raw, string / (gesetzt wenn text_format=="raw" oder text_format=="both")
		 * - text_html, string / (gesetzt wenn text_format=="html" oder text_format=="both")
		 * - charakter_id: int / referenziert den Charakter
		 * - charakter_name: string / Name des Charakters
		 * - mitglied: User-Objekt / Der Benutzer, der das Posting geschrieben hat
		 * - aktion: int / 0 = keine Aktion (Standard), 1 = Aktion (wird kursiv dargestellt)
		 * - intime: int / 0 = Nicht In-Time (wird grau dargestellt), 1 = In-time (Standard)
		 * - avatar_url: null | string / null oder die URL auf den Avatar. Avatare sind max. 80x80 Pixel groß
		 * - avatar_id: int / ID des Avatars. 0, wenn kein Avatar verwendet wird.
		 */
		
		try {
			
			this.setTime(o.optString("datum_server"));
			this.setId(o.getLong("pos"));
			this.setPost(o.getString("text_html"));
			this.setChara(o.getString("charakter_name"));
			this.setChara_id(o.getLong("charakter_id"));
			UserObject u = new UserObject();
			u.ParseJSON(o.getJSONObject("mitglied"));
			this.setUser(u);
			this.setIn_time(o.getInt("intime")==1);
			this.setAction(o.getInt("aktion")==1);
			this.setAvatar_url(o.getString("avatar_url"));
			this.setAvatar_id(o.getLong("avatar_id"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void fillRandom(long id, boolean in_time) {
		this.id = id;
		action = false;

		Chara = "Natsu";
		this.setPost("Joooooo\n :D \n\n Mehr!");
		Time = "Vor " + id + " Min";
		this.in_time = in_time;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getPost() {
		return Post;
	}


	public void setPost(String post) {
		Post = post;
	}


	public String getChara() {
		return Chara;
	}


	public void setChara(String chara) {
		Chara = chara;
	}


	public String getTime() {
		return Time;
	}


	public void setTime(String time) {
		Time = time;
	}


	public UserObject getUser() {
		return User;
	}


	public void setUser(UserObject user) {
		User = user;
	}


	public boolean isAction() {
		return action;
	}


	public void setAction(boolean action) {
		this.action = action;
	}


	public boolean isIn_time() {
		return in_time;
	}


	public void setIn_time(boolean in_time) {
		this.in_time = in_time;
	}


	
	public String getAvatar_url() {
		return avatar_url;
	}


	
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}


	
	public long getAvatar_id() {
		return avatar_id;
	}


	
	public void setAvatar_id(long avatar_id) {
		this.avatar_id = avatar_id;
	}


	
	public long getChara_id() {
		return Chara_id;
	}


	
	public void setChara_id(long chara_id) {
		Chara_id = chara_id;
	}

}
