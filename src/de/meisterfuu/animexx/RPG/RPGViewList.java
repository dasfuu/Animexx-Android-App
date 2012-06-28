package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;

/*
- id: int, ID des RPGs
- name: string, Name des RPGs
- postings: int, Anzahl der Postings im RPG
- letzt_datum_server: MySQL-Datum / Datum des aktuellsten Postings, ME(S)Z
- letzt_datum_utc: MySQL-Datum / Datum des aktuellsten Postings, UTC
- letzt_charakter: string, Name des Charakters des aktuellsten Postings
- letzt_spieler: string, Benutzername des Spielers des aktuellsten Postings
- tofu: 1 / 0, Handelt es sich um ein vertofuziertes RPG? (1 = ja, 0 = nein)
*/

public class RPGViewList extends ListActivity implements UpDateUI {

	AlertDialog alertDialog;
	ArrayList<RPGObject> RPGArray = new ArrayList<RPGObject>();
	ProgressDialog dialog;
	final Context context = this;
	RPGAdapter adapter;
	TaskRequest Task = null;
	boolean error = false;
	int mPrevTotalItemCount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		adapter = new RPGAdapter(this, RPGArray);
		setlist(adapter);
	    refresh();	 
	}	
	
	private void setlist(RPGAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {

				
				return true;
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
			        mPrevTotalItemCount = totalItemCount;
			        if(!error) {
			        	refresh();
			        }
				}
			}

			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
				//Useless Forced Method -.-
			}								
		});
		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {

				
				
			}
		});
	}

	private ArrayList<RPGObject> getRPGlist(String JSON) {
		
			ArrayList<RPGObject> RPGa = new ArrayList<RPGObject>();
			try{
				JSONObject jsonResponse = new JSONObject(JSON);
				JSONArray RPGlist = jsonResponse.getJSONArray("return");
				RPGa = new ArrayList<RPGObject>(RPGlist.length());
		
				if (RPGlist.length() != 0) {
					for (int i = 0; i < RPGlist.length(); i++) {
						JSONObject tp = RPGlist.getJSONObject(i);	
						RPGObject RPG = new RPGObject();
						RPG.parseJSON(tp);
						RPGa.add(RPG);
					}
				} else {
					//Keine weiteren RPGs
					error = true;
				}
				
				if(RPGlist.length() < 30){
					//Keine weiteren RPGs
					error = true;
				}
				
				return RPGa;
		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return RPGa;
	
	}


	public void UpDateUi(final String[] s) {		
		dialog.dismiss();
		final ArrayList<RPGObject> z = getRPGlist(s[0]);
		RPGArray.addAll(z);
		adapter.refill();
	}

	public void DoError() {
		dialog.dismiss();
		error = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      ((Activity) context).finish();
		   }
		});
		alertDialog.show();
	}

	public void refresh() {
		
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true, true,
		        new OnCancelListener() {
            public void onCancel(DialogInterface pd) {
            	Task.cancel(true);  
            	((Activity) context).finish();
            }
        });   
		
		try {			

			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/meine_rpgs/?beendete=1&api=2&offset="+RPGArray.size());
			Task = new TaskRequest(this);
			Task.execute(HTTPs);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	
	
	
	
	
}
