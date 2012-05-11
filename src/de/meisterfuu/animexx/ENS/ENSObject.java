package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;

import de.meisterfuu.animexx.other.UserObject;

public class ENSObject {
	private String Betreff, Signatur, ENS_id, Time, Referenz, AnVon;
	private String Text = "";
	private int Flags, Konversation, id, Ordner, Typ;
	private UserObject von;
	private ArrayList<UserObject> an = new ArrayList<UserObject>();

	public ENSObject() {
		
	}
	
	public void addAnUser(UserObject user){
		this.an.add(user);
	}
	
	public UserObject getAnUser(int i){
		return this.an.get(i);
	}
	
	public UserObject[] getAnArray(){
		return (UserObject[]) this.an.toArray();
	}
	
	public String getAnString(){
		String s = "";
		for(UserObject i: this.an){
			 s = i.getUsername()+", ";
		}
		return s;
	}
	
	public String getAnIDString(){
		String s = "";
		for(UserObject i: this.an){
			 s = i.getId()+", ";
		}
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

	public String getENS_id() {
		return ENS_id;
	}

	public void setENS_id(String eNS_id) {
		ENS_id = eNS_id;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}

	public int getFlags() {
		return Flags;
	}

	public void setFlags(int flags) {
		Flags = flags;
	}

	public String getReferenz() {
		return Referenz;
	}

	public void setReferenz(String referenz) {
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

	public int getOrdner() {
		return Ordner;
	}

	public void setOrdner(int ordner) {
		Ordner = ordner;
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


}
