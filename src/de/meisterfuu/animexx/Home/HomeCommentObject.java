package de.meisterfuu.animexx.Home;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.Helper;

public class HomeCommentObject extends HomeListObject {

	String Text = "";
	String URI = "";
	String Time = "";
	String type = "";
	boolean abo;
	int count;
	long Timestamp;
	int COMMENT_TYP;


	public HomeCommentObject(int typ) {
		COMMENT_TYP = typ;
	}


	@Override
	public String getFinishedText() {
		return Text;
	}


	@Override
	public int getTyp() {
		return COMMENT_TYP;
	}


	public void setTyp(int typ) {
		COMMENT_TYP = typ;
	}


	@Override
	public long getTimeStamp() {
		return Timestamp;
	}


	@Override
	public String getTime() {
		return Time;
	}


	@Override
	public void parseJSON(JSONObject o) {

		try {
			this.setTime(o.getString("letzt_datum_server"));
			this.setText(o.getString("beschreibung"));
			this.setURI("http://animexx.onlinewelten.com"+o.getString("link"));
			this.setType(o.getString("event_typ"));
			this.setCount(o.getInt("anz"));
			abo = this.type.contains("abo");
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}


	public String getText() {
		return Text;
	}


	public void setText(String text) {
		Text = text;
	}


	public String getURI() {
		return URI;
	}


	public void setURI(String uRI) {
		URI = uRI;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public void setTime(String time) {
		this.Time = time;
		this.Timestamp = Helper.toTimestamp(time);
	}


	public void setType(String typ) {
		this.type = typ;
	}


	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return URI;
	}


	@Override
	public String getImgURL() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isAbo() {
		return abo;
	}


}
