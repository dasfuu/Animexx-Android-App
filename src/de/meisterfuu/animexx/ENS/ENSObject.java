package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.other.UserObject;

public class ENSObject implements Comparable<Object> {

	private String Betreff, Signatur, Time, AnVon;
	private String Text = "";
	private int Flags, Konversation, id, Typ;
	private long ENS_id, Referenz, Ordner, TimeStamp;
	private UserObject von;
	private ArrayList<UserObject> an = new ArrayList<UserObject>();
	public boolean IsEmpty = false;


	public ENSObject() {

	}


	public boolean isFolder() {
		if (Typ == 99) return true;
		return false;
	}


	public String getTitle() {
		return Betreff;
	}


	public boolean isSystem() {
		// TODO Auto-generated method stub
		return (getTyp() == 2);
	}


	public boolean isUnread() {
		String flag = Integer.toBinaryString(getFlags());
		if (flag.length() >= 2 && flag.charAt(flag.length() - 2) == '1')
			return false;
		else
			return true;
	}


	@Override
	public boolean equals(Object obj) {
		return (this.getTimeStamp() == ((ENSObject) obj).getTimeStamp());
	}


	public int compareTo(Object arg0) {
		if (this.getTimeStamp() < ((ENSObject) arg0).getTimeStamp()) {
			return 1;
		} else if (this.getTimeStamp() > ((ENSObject) arg0).getTimeStamp()) {
			return -1;
		} else {
			return 0;
		}
	}


	public ENSObject parseJSON(JSONObject JSON) {
		try {
			this.setBetreff(JSON.getString("betreff"));
			this.setTime(JSON.getString("datum_server"));
			if (JSON.has("text_html")) this.setText(JSON.getString("text_html"));
			UserObject von = new UserObject();
			if (JSON.has("von")) von.ParseJSON(JSON.getJSONObject("von"));
			this.setVon(von);

			if (JSON.has("an"))
			for (int z = 0; z < JSON.getJSONArray("an").length(); z++) {
				UserObject an = new UserObject();
				an.ParseJSON(JSON.getJSONArray("an").getJSONObject(z));
				this.addAnUser(an);
			}

			if (JSON.has("an_flags")) {
				this.setFlags(JSON.getInt("an_flags"));
			} else {
				this.setFlags(JSON.getInt("von_flags"));
			}

			this.setENS_id(JSON.getLong("id"));

			this.setTyp(JSON.getInt("typ"));
			if (JSON.has("von_ordner")) {
				this.setOrdner(JSON.getInt("von_ordner"));
				this.setAnVon("von");
			} else {
				this.setOrdner(JSON.getInt("an_ordner"));
				this.setAnVon("an");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this;
	}


	public void addAnUser(UserObject user) {
		this.an.add(user);
	}


	public UserObject getAnUser(int i) {
		return this.an.get(i);
	}


	public UserObject[] getAnArray() {
		UserObject[] temp = new UserObject[this.an.size()];
		for (int i = 0; i < this.an.size(); i++) {
			temp[i] = this.an.get(i);
		}

		return temp;
	}


	public String getAnString() {
		String s = "";
		for (UserObject i : this.an) {
			s = i.getUsername() + ", ";
		}
		s = s.substring(0, s.length() - 3);
		return s;
	}


	public String getAnIDString() {
		String s = "";
		for (UserObject i : this.an) {
			s = i.getId() + ", ";
		}
		s = s.substring(0, s.length() - 3);
		return s;
	}


	public String getText() {
		return Text;
	}


	public void setText(String text) {
		Text = text;
	}


	public String getBetreff() {
		return Betreff;
	}


	public void setBetreff(String betreff) {
		Betreff = betreff;
	}


	public String getSignatur() {
		return Signatur;
	}


	public void setSignatur(String signatur) {
		Signatur = signatur;
	}


	public long getENS_id() {
		return ENS_id;
	}


	public void setENS_id(long eNS_id) {
		ENS_id = eNS_id;
	}


	public String getTime() {
		return Time;
	}


	public void setTime(String time) {
		TimeStamp = Helper.toTimestamp(time);
		Time = time;
	}


	public int getFlags() {
		return Flags;
	}


	public void setFlags(int flags) {
		Flags = flags;
	}


	public long getReferenz() {
		return Referenz;
	}


	public void setReferenz(long referenz) {
		Referenz = referenz;
	}


	public int getKonversation() {
		return Konversation;
	}


	public void setKonversation(int konversation) {
		Konversation = konversation;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public long getOrdner() {
		return Ordner;
	}


	public void setOrdner(long folder) {
		Ordner = folder;
	}


	public int getTyp() {
		return Typ;
	}


	public void setTyp(int typ) {
		Typ = typ;
	}


	public UserObject getVon() {
		return von;
	}


	public void setVon(UserObject von) {
		this.von = von;
	}


	public String getAnVon() {
		return AnVon;
	}


	public void setAnVon(String anVon) {
		AnVon = anVon;
	}


	public long getTimeStamp() {
		return TimeStamp;
	}


	public void setTimeStamp(long timeStamp) {
		TimeStamp = timeStamp;
	}

}
