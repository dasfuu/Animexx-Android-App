package de.meisterfuu.animexx.GB;

import android.text.Html;

public class GBObject {
	private String text, an, von, von_id, an_id, entry_id, time, avatar;
	private int typ;
	
	public GBObject() {
		this.text = "";
		this.entry_id = "";
		this.von = "";
		this.von_id = "";
		this.time = "";
		this.avatar = "";
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

	public String getAn() {
		return an;
	}

	public void setAn(String an) {
		this.an = an;
	}

	public String getVon() {
		return von;
	}

	public void setVon(String von) {
		this.von = von;
	}

	public String getVon_id() {
		return von_id;
	}

	public void setVon_id(String von_id) {
		this.von_id = von_id;
	}

	public String getAn_id() {
		return an_id;
	}

	public void setAn_id(String an_id) {
		this.an_id = an_id;
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
}
