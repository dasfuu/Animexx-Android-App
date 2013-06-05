package de.meisterfuu.animexx.Home;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class HomeKontaktObject implements Comparable<Object> {

	/*
	 * Einzelnes Objekt:
	 * - multi_item: bool: bei einzelnen Objekten immer "false"
	 * 
	 * - event_typ: string: der interne Name des Eventtyps. Einer von derzeit folgenden Werten: sb, geburtstag, autorneu, usernamechange, doujinshi,
	 * cosplayneu, fotografneu, autor, eventteilnahme, twitterconnect, stckvid, magdj, magwb, magfa, magbast, magab, magoe, magff, magfo, magfor,
	 * magev, magnews, magwtb, magart, magdvd, magmanga, magcd, magasia, maggame, magumf, magcos, magjfas, magfrt, magfr, magevvid, magfanvid,
	 * magshop, evmfgkd, evmfgknd, evmfgod, realk, projtxt, projwtb, projman, projvid, prjvid, prjrss, projnews, mb
	 * - event_name: string: menschenlesbarer Name des Eventtyps, z.B. "Selbstbeschreibungsänderung", "Geburtstag" ...
	 * 
	 * - date_server, date_server_ts, date_utc: Datum/Zeit, wann der Eintrag angelegt wurde
	 * 
	 * - kommentar: string / null: Kommentar des Benutzers, oder "null", falls es keinen Kommentar gibt. ACHTUNG: Kann manchmal aus
	 * abwärtskompatibilitätsgründen auch den Titel des Fanworks enthalten.
	 * kommentierbar: bool: Können andere Nutzer Kommentare zu diesem Item verfassen?
	 * - kommentare_anzahl: int: Anzahl der Kommentare zu diesem Item
	 * - kommentare_letzte: string: Datum (ME(S)Z) des letzten Kommentars)
	 * 
	 * - von: User-Objekt: Von wem das Ereignis ausgelöst wurde
	 * 
	 * - link: string: relative URL, meist auf das zugehörige Fanwork
	 * - item_image: string / null: Absolute URL einer Abbildung, oder "null", falls keines vorhanden ist
	 * - item_name: string / null: Name des Objekts/Fanworks, oder "null", falls keines vorhanden ist.
	 * - item_author: User-Object: Der Benutzer, von dem das verlinkte Fanwork stammt.
	 * Sammel-Objekt:
	 * 
	 * - multi_item: bool: bei Sammel-Objekten immer "true"
	 * - grouped_by: int: Anhand welchen Kriteriums die Objekte gesammelt sind. 1 = Alle Objekte stammen vom selben Kontakt und sind vom selben Typ
	 * (Kontakt xyz hat 5 Fanarts am Stück empfohlen). 2 = Verschiedene Kontakte haben das selbe Objekt empfohlen.
	 * - items: array: Ein Array, das mehrere Einzel-Objekte (siehe oben) enthält.
	 */

	public final static int SAME_OBJECT = 1;
	public final static int SAME_USER = 2;

	private boolean multi_item = false;

	// Einzel
	private String event_typ = null, event_name = null, kommentar = null, link = null, item_image = null, item_name = null, date_server = null;
	private long date_server_ts;
	private UserObject von, item_author;

	// Multi
	private int grouped_by;
	private HomeKontaktObject[] items;
	public String data;

	// Raw JSON
	public String json;


	public HomeKontaktObject() {

	}


	public HomeKontaktObject(boolean multi) {
		multi_item = multi;
	}


	public HomeKontaktObject(JSONObject obj) throws JSONException {
		parseJSON(obj);
	}


	public void parseJSON(JSONObject obj) throws JSONException {
		json = obj.toString();
		if (obj.getBoolean("multi_item")) {
			this.setMulti_item(true);
			this.setGrouped_by(obj.getInt("grouped_by"));
			JSONArray arr = obj.getJSONArray("items");
			data = arr.toString();
			items = new HomeKontaktObject[arr.length()];
			for (int i = 0; i < arr.length(); i++) {
				items[i] = new HomeKontaktObject(arr.getJSONObject(i));
			}
			Arrays.sort(items);
			this.setDate_server(items[0].getDate_server());
			this.setDate_server_ts(items[0].getDate_server_ts());
		} else {
			this.setEvent_typ(obj.getString("event_typ"));
			this.setEvent_name(obj.getString("event_typ_name"));
			this.setDate_server_ts(obj.getLong("date_server_ts"));
			this.setDate_server(obj.getString("date_server"));
			if (!obj.isNull("kommentar")) this.setKommentar(obj.getString("kommentar"));
			this.setVon(new UserObject(obj.getJSONObject("von")));
			this.setLink(obj.getString("link"));
			if (!obj.isNull("item_image")) this.setItem_image(obj.getString("item_image"));
			if (!obj.isNull("item_name")) this.setItem_name(obj.getString("item_name"));
			if (!obj.isNull("item_author")) {
				this.setItem_author(new UserObject(obj.getJSONObject("item_author")));
			}
			if (this.getEvent_typ().equals("magwb")) {
				this.setItem_image(null);
			}
		}
	}


	public boolean isMulti_item() {
		return multi_item;
	}


	public void setMulti_item(boolean multi_item) {
		this.multi_item = multi_item;
	}


	public String getEvent_typ() {
		return event_typ;
	}


	public void setEvent_typ(String event_typ) {
		this.event_typ = event_typ;
	}


	public String getEvent_name() {
		return event_name;
	}


	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}


	public String getKommentar() {
		return kommentar;
	}


	public void setKommentar(String kommentar) {
		this.kommentar = kommentar;
	}


	public String getLink() {
		return link;
	}


	public void setLink(String link) {
		this.link = link;
	}


	public String getDate_server() {
		return date_server;
	}


	public void setDate_server(String date_server) {
		this.date_server = date_server;
	}


	public String getItem_image() {
		return item_image;
	}


	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}


	public String getItem_name() {
		return item_name;
	}


	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}


	public UserObject getItem_author() {
		return item_author;
	}


	public void setItem_author(UserObject item_author) {
		this.item_author = item_author;
	}


	public long getDate_server_ts() {
		return date_server_ts;
	}


	public void setDate_server_ts(long date_server_ts) {
		this.date_server_ts = date_server_ts;
	}


	public UserObject getVon() {
		return von;
	}


	public void setVon(UserObject von) {
		this.von = von;
	}


	public int getGrouped_by() {
		return grouped_by;
	}


	public void setGrouped_by(int grouped_by) {
		this.grouped_by = grouped_by;
	}


	public HomeKontaktObject[] getItems() {
		return items;
	}


	@Override
	public boolean equals(Object obj) {
		return (this.getDate_server_ts() == ((HomeKontaktObject) obj).getDate_server_ts());
	}


	public int compareTo(Object arg0) {
		if (this.getDate_server_ts() < ((HomeKontaktObject) arg0).getDate_server_ts()) {
			return 1;
		} else if (this.getDate_server_ts() > ((HomeKontaktObject) arg0).getDate_server_ts()) {
			return -1;
		} else {
			return 0;
		}
	}

}
