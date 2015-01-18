package de.meisterfuu.animexx.AIDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MangaObject {

	private long id = -1L;
	private String name = "";
	private JSONArray baende;
	
	public MangaObject parseJSON(JSONObject obj){
		try {
			this.setId(obj.getLong("id"));
			this.setName(obj.getString("name"));
			this.setBaende(obj.getJSONArray("baende"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
		
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
	
	public JSONArray getBaende() {
		return baende;
	}
	
	public void setBaende(JSONArray baende) {
		this.baende = baende;
	}
	
}
