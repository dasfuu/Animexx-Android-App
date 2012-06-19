package de.meisterfuu.animexx.GB;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.UserObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;

public class GBViewList extends ListActivity implements UpDateUI {

	AlertDialog alertDialog;

	ArrayList<GBObject> GBArray = new ArrayList<GBObject>();
	ProgressDialog dialog;
	final Context con = this;
	int page;
	int mPrevTotalItemCount;
	GBAdapter adapter;
	TaskRequest Task = null;
	boolean error = false;
	String id, username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
				
		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getString("id");
		} else {
			id = Request.config.getString("id", "none");
			username = "Du!";
		}
		
				
		adapter = new GBAdapter(this, GBArray);
		setlist(adapter);
	    refresh();	    	
	}
	

	private void setlist(GBAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
					GBPopUp Menu = new GBPopUp(con, GBArray.get(pos).getVon().getUsername(),
							GBArray.get(pos).getVon().getId(), GBArray.get(pos).getEntry_id(),
							GBArray.get(pos).getEinleitung());
					Menu.PopUp();
				return true;
			}
		});

		
		lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			    if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
			        mPrevTotalItemCount = totalItemCount;
			        if(!error)refresh();
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
				Bundle bundle = new Bundle();
				bundle.putString("von", GBArray.get(pos).getVon().getUsername());
				bundle.putString("von_id", GBArray.get(pos).getVon().getId());
				bundle.putString("id", GBArray.get(pos).getEntry_id());
				bundle.putString("text", GBArray.get(pos).getText());
				Intent newIntent = new Intent(con.getApplicationContext(),
					GBViewSingle.class);
				newIntent.putExtras(bundle);
				con.startActivity(newIntent);
			}
		});
	}

	private ArrayList<GBObject> getENSlist(String JSON) {
		
		
			try{
				JSONObject jsonResponse = new JSONObject(JSON);
				JSONArray GBlist = jsonResponse.getJSONObject("return").getJSONArray("eintraege");
				ArrayList<GBObject> GBa = new ArrayList<GBObject>(GBlist.length());
		
				if (GBlist.length() != 0) {
					for (int i = 0; i < GBlist.length(); i++) {
						JSONObject tp = GBlist.getJSONObject(i);	
						GBObject GB = new GBObject();
						
						UserObject von = new UserObject();
						von.ParseJSON(tp.getJSONObject("von"));
						GB.setVon(von);
						
						GB.setText(tp.getString("text_html"));
						GB.setEntry_id(tp.getString("id"));
						GB.setTime(tp.getString("datum_server"));
						GB.setAvatar(tp.getString("avatar"));
						GBa.add(GB);
					}
				} else {
					//Keine Einträge im Gästebuch
					error = true;
					return GBa;
				}
				
				return GBa;
		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	
	}


	public void UpDateUi(final String[] s) {
		
		dialog.dismiss();
		final ArrayList<GBObject> z = getENSlist(s[0]);
		GBArray.addAll(z);
		adapter.refill();
		page++;
	}

	public void DoError() {
		dialog.dismiss();
		error = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      ((Activity) con).finish();
		   }
		});
		alertDialog.show();
	}

	public void refresh() {
		
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true, true,
		        new OnCancelListener() {
            public void onCancel(DialogInterface pd) {
            	Task.cancel(true);  
            	((Activity) con).finish();
            }
        });   
		
		try {			

			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id="+id+"&text_format=html&api=2&seite="+page);
	
			Task = new TaskRequest(this);
			Task.execute(HTTPs);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
