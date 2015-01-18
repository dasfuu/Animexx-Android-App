package de.meisterfuu.animexx.AIDB;

import org.json.JSONException;
import org.json.JSONObject;


public class MangaBandObject {

	private long id = -1L;
	private String name = "";
	private int band = -1;
	
	public MangaBandObject parseJSON(JSONObject obj){
		try {
			this.setId(obj.getLong("id"));
			this.setName(obj.getString("name"));
			this.setBand(obj.getInt("band"));
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
	
	public int getBand() {
		return band;
	}
	
	public void setBand(int band) {
		this.band = band;
	}
	
}

