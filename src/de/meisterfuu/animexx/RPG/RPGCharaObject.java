package de.meisterfuu.animexx.RPG;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class RPGCharaObject {

	/*
	 * - owner, User-Object / Der RPG-Admin
	 * - offen, array / Die offenen Rollen. Jedes Array-Element ist ein Objekt mit zwei Werten: - id / int / Die ID der Rolle
	 * - name / string / Der Name der Rolle
	 * - spieler, array / Die vergebenen Rollen
	 * - id / int / Die ID der Rolle
	 * - name / string / Der Name der Rolle
	 * - admin / int / 0 = Kein Admin , 1 = Admin
	 * - mitglied / User-Object / Spieler, der diese Rolle derzeit hat (über die Zeit können auch verschiedene Mitglieder eine Rolle übernehmen)
	 */

	String name;
	long id;
	boolean admin;
	UserObject user;
	boolean free;


	public RPGCharaObject() {
		UserObject u = new UserObject();
		u.setUsername("Frei");
		this.setUser(u);
		this.setAdmin(false);
		this.setId(-1L);
		this.setName("");
	}


	public void parseJSON(JSONObject o) {

		try {
			this.setId(o.getLong("id"));
			this.setName(o.getString("name"));
			if (o.has("user")) {
				UserObject u = new UserObject();
				u.ParseJSON(o.getJSONObject("mitglied"));
				this.setUser(u);
				free = false;
				if (o.getInt("admin") == 1)
					this.setAdmin(true);
				else
					this.setAdmin(false);
			}else{
				free = true;
				UserObject u = new UserObject();
				u.setUsername("Frei");
				this.setUser(u);
				this.setAdmin(false);
			}

		} catch (JSONException e) {
			UserObject u = new UserObject();
			u.setUsername("Error");
			this.setUser(u);
			e.printStackTrace();
		}

	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public boolean isAdmin() {
		return admin;
	}


	public void setAdmin(boolean admin) {
		this.admin = admin;
	}


	public UserObject getUser() {
		return user;
	}


	public void setUser(UserObject user) {
		this.user = user;
	}


	
	public boolean isFree() {
		return free;
	}


	
	public void setFree(boolean free) {
		this.free = free;
	}

}
