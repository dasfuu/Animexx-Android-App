package de.meisterfuu.animexx.Home;

import org.json.JSONObject;


public abstract class HomeListObject implements Comparable<Object> {
	

	public static int KONTAKTE = 1;
	
	public static int WEBLOGKOMMENTAR = 100;
	public static int UMFRAGEKOMMENTAR = 101;
	public static int FANWORKKOMMENTAR = 102;
	public static int DOJINSHIKOMMENTAR = 103;
	public static int FANARTKOMMENTAR = 104;
	public static int FANFICKOMMENTAR = 105;
	
	public static int COSPLAY = 200;

	public abstract String getFinishedText();
	public abstract int getTyp();
	public abstract long getTimeStamp();
	public abstract String getTime();
	public abstract String getURL();
	public abstract String getImgURL();
	public abstract void parseJSON(JSONObject o);
	
	@Override
	public String toString(){
		return getFinishedText();
	}	
	
	@Override
	public boolean equals(Object obj) {
		return (this.getTimeStamp() == ((HomeListObject) obj).getTimeStamp());
	}
	

	public int compareTo(Object arg0) {
		if(this.getTimeStamp() < ((HomeListObject)arg0).getTimeStamp()){
			return 1;
		} else if (this.getTimeStamp() > ((HomeListObject)arg0).getTimeStamp()) {
			return -1;
		} else {
			return 0;
		}
	}



	
}
