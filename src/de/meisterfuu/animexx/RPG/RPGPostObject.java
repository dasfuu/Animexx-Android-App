package de.meisterfuu.animexx.RPG;

import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class RPGPostObject {

	long id;
	String Post;
	String Chara;
	String Time;
	boolean action, in_time;
	UserObject User;
	

	
	public RPGPostObject(){
		User = new UserObject();
		Chara = "Unbekannt";
	}
	
	public void parseJSON(JSONObject o){

	}
	
	public void fillRandom(long id, boolean in_time){		
			this.id = id;
			action = false;
			
			Chara = "Natsu";
			this.setPost("Joooooo\n :D \n\n Mehr!");
			Time = "Vor "+id+" Min";
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
	
}
