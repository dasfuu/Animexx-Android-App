package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.other.UserObject;

public class ENSSync extends Activity {

	String typ;
	ArrayList<ENSObject> ENSArray = new ArrayList<ENSObject>();
	ArrayList<ENSObject> OrdnerArray = new ArrayList<ENSObject>();
	long ordner;
	ProgressDialog dialog;
	final Activity con = this;
	int page;
	int mPrevTotalItemCount;
	TaskRequest Task = null;
	int i = -1;
	Thread t;
	boolean stop = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);		
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true, true,
		        new OnCancelListener() {
            public void onCancel(DialogInterface pd) {
            	DoError();
            }
        });   
		
		t = new Thread(new Runnable() {
			public void run() {
				start();
			}
		});
		
		t.start();
    	
	}
	
	private void start(){
		
			try{
				ENSsql SQL = new ENSsql(con);
				SQL.open();
				SQL.clearFolder();
		
				
		
				HttpGet HTTPs;
				HTTPs = Request.getHTTP("https://ws.animexx.de/json/ens/ordner_liste/?api=2");	
				String s;
				s = Request.SignSend(HTTPs);
				getFolderlist(s);
				
				
				for(int i = 0; i < OrdnerArray.size(); i++){
					if(OrdnerArray.get(i).getENS_id() > 3) SQL.createFolder(OrdnerArray.get(i));
				}
				SQL.close();
				
				next();
				refreshENS();
				
			} catch (Exception e) {
				DoError();
				e.printStackTrace();
			}
			
	}
	

	private void getFolderlist(String JSON) {
		
		try{
			
				JSONArray FolderList = null;
				JSONObject jsonResponse = new JSONObject(JSON);	
			
				jsonResponse = new JSONObject(JSON);
				final ArrayList<ENSObject> ENSa = new ArrayList<ENSObject>();
				
				FolderList = jsonResponse.getJSONObject("return").getJSONArray("an");	
	
					for (int i = 0; i < FolderList.length(); i++) {	
						ENSa.add(new ENSObject());
						ENSa.get(ENSa.size()-1).setBetreff(FolderList.getJSONObject(i).getString("name"));
						ENSa.get(ENSa.size()-1).setENS_id(FolderList.getJSONObject(i).getLong("ordner_id"));
						ENSa.get(ENSa.size()-1).setSignatur("0");
						ENSa.get(ENSa.size()-1).setTyp(99);
						ENSa.get(ENSa.size()-1).setAnVon("an");
						ENSa.get(ENSa.size()-1).setOrdner(1);
					}
					
				FolderList = jsonResponse.getJSONObject("return").getJSONArray("von");	
					
					for (int i = 0; i < FolderList.length(); i++) {	
						ENSa.add(new ENSObject());
						ENSa.get(ENSa.size()-1).setBetreff(FolderList.getJSONObject(i).getString("name"));
						ENSa.get(ENSa.size()-1).setENS_id(FolderList.getJSONObject(i).getLong("ordner_id"));
						ENSa.get(ENSa.size()-1).setSignatur("0");
						ENSa.get(ENSa.size()-1).setTyp(99);
						ENSa.get(ENSa.size()-1).setAnVon("von");
						ENSa.get(ENSa.size()-1).setOrdner(2);
					}
					
				OrdnerArray.addAll(ENSa);				
			
		} catch(Exception e) {
			e.printStackTrace();
			DoError();
		}
	}


	private ArrayList<ENSObject> getENSlist(String JSON, long folder) {

		try {
			JSONArray ENSlist = null;
			
			JSONObject jsonResponse = new JSONObject(JSON);
			ENSlist = jsonResponse.getJSONArray("return");
			
	

			//ENSObject[] ENSa;
			final ArrayList<ENSObject> ENSa = new ArrayList<ENSObject>();




			for (int i = 0; i < ENSlist.length(); i++) {
					ENSObject tempENS  = new ENSObject();					

					tempENS.setBetreff(ENSlist.getJSONObject(i).getString("betreff"));
					tempENS.setTime(ENSlist.getJSONObject(i).getString("datum_server"));
					
					UserObject von = new UserObject();
					von.ParseJSON(ENSlist.getJSONObject(i).getJSONObject("von"));
					tempENS.setVon(von);
					
					for(int z = 0; z < ENSlist.getJSONObject(i).getJSONArray("an").length(); z++){
						UserObject an = new UserObject();
						an.ParseJSON(ENSlist.getJSONObject(i).getJSONArray("an").getJSONObject(z));
						tempENS.addAnUser(an);
					}
					
					if(typ.equals("an")) tempENS.setFlags(ENSlist.getJSONObject(i).getInt("an_flags"));
					else tempENS.setFlags(ENSlist.getJSONObject(i).getInt("von_flags"));
					
					tempENS.setENS_id(ENSlist.getJSONObject(i).getLong("id"));
					tempENS.setTyp(ENSlist.getJSONObject(i).getInt("typ"));
					tempENS.setOrdner(folder);
					tempENS.setAnVon(typ);
					
					ENSa.add(tempENS);
			}

			ENSArray.addAll(ENSa);	
			return ENSa;
			
		} catch (Exception e) {
			e.printStackTrace();
			DoError();
		}

		return new ArrayList<ENSObject>();
	}

	public void next(){
		i++;
		if(i < OrdnerArray.size()){
			ordner = OrdnerArray.get(i).getENS_id();
			typ = OrdnerArray.get(i).getAnVon();
			page = 0;
			if(ordner == 3)next();
		} else {
			DoError();
		}
	}
		
	public void UpDateENS(String s) {
		
		final ArrayList<ENSObject> z = getENSlist(s, ordner);
		
		final int yo = ENSArray.size();
		con.runOnUiThread(new Runnable() {
			public void run() {
				dialog.setMessage(yo+" ENS geladen.");
			}
		});
		
		Log.i("Sync",yo+" ENS geladen.");
		Log.i("Sync","Ordner("+typ+"): "+OrdnerArray.get(i).getBetreff()+"("+ordner+")");
		Log.i("Sync","Seite: "+page);
		Log.i("Sync","SQL Start");

		ENSsql SQL = new ENSsql(con);
		SQL.open();
		for(int i = 0; i < z.size(); i++) SQL.updateENS(z.get(i), false);
		SQL.close();
		Log.i("Sync","SQL End");
		
		if(z.size() < 100) {
			next();
		}

		if(!stop) refreshENS();

	}

	public void DoError() {
		stop = true;
		dialog.dismiss();
		this.finish();
	}

	public void refreshENS() {
		
		try {
			
			//HTTPs[1] = Request.getHTTP("https://ws.animexx.de/json/ens/ordner_liste/?ordner_typ="+ typ + "&api=2");

			HttpGet HTTPs;
			HTTPs = Request.getHTTP("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="
								+ ordner + "&ordner_typ=" + typ + "&seite="+page+"&anzahl=100&api=2");
			page += 1;			
			String s;
			s = Request.SignSend(HTTPs);
			UpDateENS(s);
	
		} catch (Exception e) {
			DoError();
			e.printStackTrace();
		}
	}

}
