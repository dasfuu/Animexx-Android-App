package de.meisterfuu.animexx.Home;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class HomeImageObject extends HomeListObject {

	long timestamp = System.currentTimeMillis();
	String time = "Aktuell";
	int dimH, dimW;
	int IMAGE_TYP;
	String imgURL, galURL, URL;
	UserObject user = new UserObject();


	@Override
	public void parseJSON(JSONObject o) {
		try {
			setImgURL(o.getString("img"));
			setGalURL("http://animexx.onlinewelten.com" + o.getString("mitglied_galerie_link"));
			setURL("http://animexx.onlinewelten.com" + o.getString("link"));
			setDimH(o.getInt("img_h"));
			setDimW(o.getInt("img_w"));
			user.ParseJSON(o.getJSONObject("mitglied"));
			time = "Aktuell";
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	public HomeImageObject(int typ) {
		IMAGE_TYP = typ;
	}


	@Override
	public String getFinishedText() {
		return "Cosplay von "+user.getUsername();
	}


	@Override
	public int getTyp() {
		return IMAGE_TYP;
	}
	

	public void setTyp(int typ) {
		IMAGE_TYP = typ;
	}


	@Override
	public long getTimeStamp() {
		return timestamp;
	}


	@Override
	public String getTime() {
		return time;
	}


	@Override
	public String getURL() {
		return URL;
	}


	@Override
	public String getImgURL() {
		return imgURL;
	}


	public int getDimH() {
		return dimH;
	}


	public void setDimH(int dimH) {
		this.dimH = dimH;
	}


	public int getDimW() {
		return dimW;
	}


	public void setDimW(int dimW) {
		this.dimW = dimW;
	}


	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}


	public String getGalURL() {
		return galURL;
	}


	public void setGalURL(String galURL) {
		this.galURL = galURL;
	}


	public UserObject getUser() {
		return user;
	}


	public void setUser(UserObject user) {
		this.user = user;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public void setURL(String uRL) {
		URL = uRL;
	}

}
