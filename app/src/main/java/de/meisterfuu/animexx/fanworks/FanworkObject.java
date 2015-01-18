package de.meisterfuu.animexx.fanworks;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.other.UserObject;

public class FanworkObject {

	/*
	 * Fanwork-Objekt
	 * - id: int
	 * - title: string
	 * - mitglied: User-Objekt
	 * - date_server, date_server_ts: string / int, Datum des Werks (nach dem auch in der Zeichner/Autoren/etc.-Galerie sortiert wird; meist Datum
	 * der letzten größeren Aktualisierung)
	 * - item_image: string / null: Absolute URL einer Abbildung, oder "null", falls keine vorhanden ist. Bei Fanarts/Basteleien das Hauptwerk, bei
	 * FF/DJ/Fotos/Cosplays die Coverillustration.
	 * - item_image_big: string / null: Absolute URL einer Abbildung, oder "null", falls keines vorhanden ist. Wird nur zurckgeliefert, wenn beim
	 * Aufruf img_max_x, img_max_y, img_quality und img_format angegeben wird.
	 * - beschreibung: string: HTML-Formatierte Beschreibung des Fanworks. Wird nur bei /fanworks/get_fanwork_data/ zurckgeliefert.
	 * - adult: boolean: Ist das Werk adult? Achtung: bei Doujinshis und Fanfics knnen einzelne Kapitel auch Adult sein, wenn das Hauptwerk
	 * Nicht-Adult ist.
	 * - hat_kommentare: boolean
	 * - kommentare_display: int (Wird nur zurckgegegeben, wenn hat_kommentare === true). Die Gesamtzahl der Kommentare abzglich der Kommentare des
	 * Autors. Die Zahl, die auf der Website als Zahl angegeben wird.
	 * - kommentare_anz: int (Wird nur zurckgegegeben, wenn hat_kommentare === true). Anzahl der Kommentare auf das Werk, inkl. der Kommentare des
	 * Autors, aber ohne die Antworten.
	 * - kommentare_anz_antworten: int (Wird nur zurckgegegeben, wenn hat_kommentare === true). Anzahl der Antworten auf Kommentare zum Werk, inkl.
	 * der Antworten des Autors.
	 * - kommentare: Array von Kommentar-Objekten: Wird nur bei /fanworks/get_fanwork_data/ mit hat_kommentare === true und bei gesetzten
	 * kommentar_*-Parametern zurckgeliefert.
	 */

	long id;
	String title;
	UserObject von;
	String date_server;
	long date_server_ts;
	String item_image_big = null, item_image = null;
	String beschreibung = null;
	boolean adult;
	boolean hat_kommentare;
	int kommentare_display;
	ArrayList<KommentarObject> kommentare;


	public static FanworkObject parseJSON(JSONObject obj) throws JSONException{
		FanworkObject temp = new FanworkObject();
		temp.setId(obj.getLong("id"));
		temp.setTitle(obj.getString("titel"));
		temp.setDate_server(obj.getString("datum_server"));
		temp.setDate_server_ts(obj.getLong("datum_server_ts"));
		if(obj.has("beschreibung") && !obj.isNull("beschreibung")) temp.setBeschreibung(obj.getString("beschreibung"));
		if(obj.has("item_image_big") && !obj.isNull("item_image_big")) temp.setItem_image_big(obj.getString("item_image_big"));
		if(obj.has("item_image") && !obj.isNull("item_image")) temp.setItem_image(obj.getString("item_image"));
		temp.setHat_kommentare(obj.getBoolean("hat_kommentare"));
		temp.setAdult(obj.getBoolean("adult"));
		if(temp.hat_kommentare && obj.has("kommentare")){
			JSONArray komm = obj.getJSONArray("kommentare");	
			for(int i = 0; i < komm.length(); i++){
				//Add Kommentare
			}
		}
		temp.setVon(new UserObject(obj.getJSONObject("mitglied")));
		
		return temp;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public UserObject getVon() {
		return von;
	}


	public void setVon(UserObject von) {
		this.von = von;
	}


	public String getDate_server() {
		return date_server;
	}


	public void setDate_server(String date_server) {
		this.date_server = date_server;
	}


	public long getDate_server_ts() {
		return date_server_ts;
	}


	public void setDate_server_ts(long date_server_ts) {
		this.date_server_ts = date_server_ts;
	}


	public String getItem_image_big() {
		return item_image_big;
	}


	public void setItem_image_big(String item_image_big) {
		this.item_image_big = item_image_big;
	}


	public String getItem_image() {
		return item_image;
	}


	public void setItem_image(String item_image) {
		this.item_image = item_image;
	}


	public String getBeschreibung() {
		return beschreibung;
	}


	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}


	public boolean isAdult() {
		return adult;
	}


	public void setAdult(boolean adult) {
		this.adult = adult;
	}


	public boolean isHat_kommentare() {
		return hat_kommentare;
	}


	public void setHat_kommentare(boolean hat_kommentare) {
		this.hat_kommentare = hat_kommentare;
	}


	public int getKommentare_display() {
		return kommentare_display;
	}


	public void setKommentare_display(int kommentare_display) {
		this.kommentare_display = kommentare_display;
	}


	public ArrayList<KommentarObject> getKommentare() {
		return kommentare;
	}


	public void setKommentare(ArrayList<KommentarObject> kommentare) {
		this.kommentare = kommentare;
	}

}
