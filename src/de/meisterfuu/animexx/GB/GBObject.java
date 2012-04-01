package de.meisterfuu.animexx.GB;

public class GBObject {
	public String text, an, von, von_id, an_id, entry_id, time, avatar;
	public int typ;
	
	public GBObject(){
		
	}
	
	public GBObject(String text, String id, String von, String von_id, String time, String avatar){
		this.text = text;
		this.entry_id = id;
		this.von = von;
		this.von_id = von_id;	
		this.time = time;
		if (avatar.startsWith("http")) this.avatar = ""; else this.avatar = "";
	}
	
	public String getEinleitung(){
		if (text.length() >= 32 ) return text.substring(0, 31);		
		else return text;
	}
}
