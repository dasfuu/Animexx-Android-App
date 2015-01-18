package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.AvatarObject;
import de.meisterfuu.animexx.other.UserObject;

public class RPGCharaObject {


	private String name;
	private long id;
	private boolean admin;
	private UserObject user;
	private boolean free;
	private ArrayList<AvatarObject> AvatarArray = new ArrayList<AvatarObject>();


	public RPGCharaObject() {
		UserObject u = new UserObject();
		u.setUsername("Frei");
		this.setUser(u);
		this.setAdmin(false);
		this.setId(-1L);
		this.setName("");
		this.setFree(true);
	}


	public void parseJSON(JSONObject o) {

		try {
			this.setId(o.getLong("id"));
			this.setName(o.getString("name"));

			if (o.has("avatare")) {
				JSONArray temp = o.getJSONArray("avatare");
				AvatarArray.ensureCapacity(temp.length());
				for (int i = 0; i < temp.length(); i++) {
					AvatarObject b = new AvatarObject(temp.getJSONObject(i).getString("url"), temp.getJSONObject(i).getLong("id"));
					AvatarArray.add(b);
				}
			}

			if (o.has("mitglied")) {
				UserObject u = new UserObject();
				u.ParseJSON(o.getJSONObject("mitglied"));
				this.setUser(u);
				free = false;
				if (o.getInt("admin") == 1)
					this.setAdmin(true);
				else
					this.setAdmin(false);
			} else {
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


	public void parseJSONown(JSONObject o) {

		try {
			this.setId(o.getLong("id"));
			this.setName(o.getString("name"));

			if (o.has("avatare")) {
				JSONArray temp = o.getJSONArray("avatare");
				AvatarArray.ensureCapacity(temp.length());
				for (int i = 0; i < temp.length(); i++) {
					AvatarObject b = new AvatarObject(o.getString("url"), o.getLong("id"));
					AvatarArray.add(b);
				}
			}

			UserObject u = new UserObject();
			u.setId(Request.config.getString("id", "none"));
			u.setUsername(Request.config.getString("username", "Du!"));
			this.setUser(u);
			free = false;

		} catch (JSONException e) {
			UserObject u = new UserObject();
			u.setUsername("Error");
			this.setUser(u);
			e.printStackTrace();
		}

	}


	@Override
	public String toString() {
		return getName();
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


	public ArrayList<AvatarObject> getAvatarArray() {
		return AvatarArray;
	}

}
