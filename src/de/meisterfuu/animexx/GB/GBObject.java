package de.meisterfuu.animexx.GB;

import de.meisterfuu.animexx.other.UserObject;
import android.text.Html;

public class GBObject {
	private String text, entry_id, time, avatar;
	private int typ;
	private UserObject An,Von;
	
	public GBObject() {
		this.text = "";
		this.entry_id = "";
		this.time = "";
		this.avatar = "";
		An = new UserObject();
		Von = new UserObject();
	}
	
	public GBObject(boolean i) {
		this();
		this.text = "Leer :/";
	}

	public String getEinleitung() {
		if (text.length() >= 32)
			return Html.fromHtml(text.substring(0, 32)).toString();
		else
			return Html.fromHtml(text).toString();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getEntry_id() {
		return entry_id;
	}

	public void setEntry_id(String entry_id) {
		this.entry_id = entry_id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {		
		if (avatar.startsWith("http"))
			this.avatar = avatar;
		else
			this.avatar = "";
	}

	public int getTyp() {
		return typ;
	}

	public void setTyp(int typ) {
		this.typ = typ;
	}

	public void setAn(UserObject an) {
		An = an;
	}

	public void setVon(UserObject von) {
		Von = von;
	}

	public UserObject getAn() {
		return An;
	}

	public UserObject getVon() {
		return Von;
	}
}
