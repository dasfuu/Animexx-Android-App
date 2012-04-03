package de.meisterfuu.animexx.ENS;

public class ENSObject {
	public String text, betreff, von, signatur, ENS_id, time, von_id;
	public int flag, referenz, konversation, id, typ, ordner;
	
	public ENSObject(){
		
	}
	
	public ENSObject(String text, String betreff, String von, String von_id, String signatur, String time, int flag, int referenz, int konversation, int id,String ENS_id, int typ, int ordner){
		this.text = text;
		this.betreff = betreff;
		this.von = von;
		this.von_id = von_id;
		this.signatur = signatur;
		this.time = time;
		this.flag = flag;
		this.referenz = referenz;
		this.konversation = konversation;
		this.id = id;
		this.ENS_id = ENS_id;
		this.typ = typ;
		this.ordner = ordner;
	}
	
	public ENSObject(String text, String id, int typ, int ordner){
		this.betreff = text;
		this.ENS_id = id;
		this.typ = typ;
		this.ordner = ordner;
	}
}
