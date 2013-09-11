package de.meisterfuu.animexx.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventObject {

	private long id;
	private String name = "";
	private String time_start = null, time_stop = null;
	private String plz = null, ort = "";
	private String land_iso = null, land_name = null, bland_iso = null, bland_name = null;
	private String adress = null, veranstalter = null, contact = null;
	private String logo_url = null;
	private String size = null;
	private int animexx = 0;
	private int dabei_anzahl;
	private double geo_l, geo_b;

	private String detail_json = null;
	private JSONArray pages = null;

	public boolean isAnimexxInvolved() {

		return (animexx > 0);
	}
	

	public void parseJSON(JSONObject json) {
		try {
			// - id: int
			this.setId(json.getLong("id"));

			// - name: string
			this.setName(json.getString("name"));

			// - datum_von, datum_bis: YYYY-MM-DD
			this.setTime_start(json.getString("datum_von"));

			// - datum_bis, datum_bis: YYYY-MM-DD
			this.setTime_stop(json.getString("datum_bis"));

			// - plz, ort: string
			this.setPlz(json.getString("plz"));
			this.setOrt(json.getString("ort"));

			// - land_iso, land_name: Länderkürzel nach ISO-639 + ausgeschriebener Name
			this.setLand_iso(json.getString("land_iso"));
			this.setLand_name(json.getString("land_name"));

			// - bundesland_iso, bundesland_name: Bundesländerkürzel nach ISO 3166-2 + ausgeschriebener Name. Nur bei DE/AT/CH
			if (json.has("bundesland_iso")) {
				this.setBland_iso(json.getString("bundesland_iso"));
				this.setBland_name(json.getString("bundesland_name"));
			}

			// - groesse, groesse_str: int / string: Erwartete Besucherzahl. Interner Wert + Ausgeschriebener String.
			this.setSize(json.getString("groesse_str"));
			
			// - animexx, animexx_str: int / string: Animexx-Involvierung. Interner Wert + Ausgeschriebener String.
			this.setAnimexx(json.getInt("animexx"));
			
			// - dabei_anzahl: int: Die Anzahl an angemeldeten Mitgliedern.
			this.setDabei_anzahl(json.getInt("dabei_anzahl"));
			
			// - geo_laenge, geo_breite: float: Geo-Koordinaten des Treffpunkts.
			this.setGeo_b(json.getDouble("geo_breite"));
			this.setGeo_l(json.getDouble("geo_laenge"));
			
			// - detail
			if(json.has("logo")){
				this.setLogo_url(json.getString("logo"));
				this.setAdress(json.getString("adresse"));
				this.setVeranstalter(json.getString("veranstalter"));
				this.setContact(json.getString("kontakt"));
			}
			
			if(json.has("beschreibungsseiten")){
				this.setPages(json.getJSONArray("beschreibungsseiten"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
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


	public String getTime_start() {
		return time_start;
	}


	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}


	public String getTime_stop() {
		return time_stop;
	}


	public void setTime_stop(String time_stop) {
		this.time_stop = time_stop;
	}


	public String getPlz() {
		return plz;
	}


	public void setPlz(String plz) {
		this.plz = plz;
	}


	public String getOrt() {
		return ort;
	}


	public void setOrt(String ort) {
		this.ort = ort;
	}


	public String getLand_iso() {
		return land_iso;
	}


	public void setLand_iso(String land_iso) {
		this.land_iso = land_iso;
	}


	public String getLand_name() {
		return land_name;
	}


	public void setLand_name(String land_name) {
		this.land_name = land_name;
	}


	public String getBland_iso() {
		return bland_iso;
	}


	public void setBland_iso(String bland_iso) {
		this.bland_iso = bland_iso;
	}


	public String getBland_name() {
		return bland_name;
	}


	public void setBland_name(String bland_name) {
		this.bland_name = bland_name;
	}


	public String getAdress() {
		return adress;
	}


	public void setAdress(String adress) {
		this.adress = adress;
	}


	public String getVeranstalter() {
		return veranstalter;
	}


	public void setVeranstalter(String veranstalter) {
		this.veranstalter = veranstalter;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getLogo_url() {
		return logo_url;
	}


	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}


	public String getSize() {
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public int getAnimexx() {
		return animexx;
	}


	public void setAnimexx(int animexx) {
		this.animexx = animexx;
	}


	public int getDabei_anzahl() {
		return dabei_anzahl;
	}


	public void setDabei_anzahl(int dabei_anzahl) {
		this.dabei_anzahl = dabei_anzahl;
	}


	public double getGeo_l() {
		return geo_l;
	}


	public void setGeo_l(double geo_l) {
		this.geo_l = geo_l;
	}


	public double getGeo_b() {
		return geo_b;
	}

	public void setGeo_b(double d) {
		this.geo_b = d;
	}


	public JSONObject getDetail_json() throws JSONException {
		return new JSONObject(detail_json);
	}


	public void setDetail_json(String detail_json) {
		this.detail_json = detail_json;
	}


	public JSONArray getPages() {
		return pages;
	}


	public void setPages(JSONArray pages) {
		this.pages = pages;
	}

}
