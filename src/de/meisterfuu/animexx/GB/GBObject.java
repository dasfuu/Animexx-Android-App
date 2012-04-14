package de.meisterfuu.animexx.GB;

import android.text.Html;

public class GBObject {
	public String text, an, von, von_id, an_id, entry_id, time, avatar;
	public int typ;



	public GBObject(String text, String id, String von, String von_id,
			String time, String avatar) {
		this.text = text;
		this.entry_id = id;
		this.von = von;
		this.von_id = von_id;
		this.time = time;
		if (avatar.startsWith("http"))
			this.avatar = avatar;
		else
			this.avatar = "";
	}
	
	public GBObject() {
		this.text = "Leer";
		this.entry_id = "-1";
		this.von = "";
		this.von_id = "";
		this.time = "";
		this.avatar = "";
	}

	public String getEinleitung() {
		if (text.length() >= 32)
			return Html.fromHtml(text.substring(0, 32)).toString();
		else
			return Html.fromHtml(text).toString();
	}
}
