package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.UserObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ENSAusgang extends ListActivity implements UpDateUI {

	String typ;
	AlertDialog alertDialog;
	//JSONArray ENSlist, FolderList;
	ArrayList<ENSObject> ENSArray = new ArrayList<ENSObject>();
	//ENSObject[] temp;
	String ordner;
	Integer offset = 0;
	ProgressDialog dialog;
	Context con;
	int max,maxf;
	int page = 0;
	int mPrevTotalItemCount = 0;
	Boolean loading;
	ENSAdapter adapter;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typ = "von";
		con = this;
		if (this.getIntent().hasExtra("folder")) {
			Bundle bundle = this.getIntent().getExtras();
			ordner = bundle.getString("folder");
		} else ordner ="2";


		adapter = new ENSAdapter(this, ENSArray);
		setlist(adapter);
		refresh();
	}

	private void setlist(ENSAdapter a) {

		setListAdapter(a);
		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int position,
					long id) {
				if (position >= offset) {
					ENSPopUp Menu = new ENSPopUp(con, ENSArray.get(position).getVon().getUsername(),
							ENSArray.get(position).getVon().getId(), ENSArray.get(position).getENS_id(),
							ENSArray.get(position).getBetreff(), typ, 1);
					Menu.PopUp();
				}

				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String i = "-1";
				if (position < offset) {
					i = "2";
					i = ENSArray.get(position).getENS_id();
					// Request.doToast(""+i, getApplicationContext());
					Bundle bundle = new Bundle();
					bundle.putString("folder", i);
					Intent newIntent = new Intent(getApplicationContext(),
							ENSAusgang.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				} else {
					i = ENSArray.get(position).getENS_id();
					Bundle bundle = new Bundle();
					bundle.putString("id", i);
					Intent newIntent = new Intent(getApplicationContext(),
							ENSSingle.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				}

			}

		});
		
		lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			    if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
			        mPrevTotalItemCount = totalItemCount;
			        refresh();
			    }
			}

			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
				//Useless Forced Method -.-
			}								
	});
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ENSObject> getENSlist(String[] JSON, int folder) {

		try {
			JSONArray ENSlist, FolderList = null;
			
			JSONObject jsonResponse = new JSONObject(JSON[0]);
			ENSlist = jsonResponse.getJSONArray("return");

			if (JSON.length > 1) {
				jsonResponse = new JSONObject(JSON[1]);
				FolderList = jsonResponse.getJSONObject("return").getJSONArray("von");
				//max = FolderList.getJSONObject(0).getInt("gesamt");
				offset = FolderList.length() - 2;
			} else {
				offset = 0;
			}
			
			if(ENSlist.length() <= 0) return (ArrayList<ENSObject>)ENSArray.clone();

			final ArrayList<ENSObject> ENSa = new ArrayList<ENSObject>();
			if ((ENSlist.length() + offset) <= 0) {
				Request.doToast("Ordner leer!", getApplicationContext());
				return ENSa;
			}

			if (JSON.length > 1) {
				if (FolderList.length() != 0) {
					for (int i = 0; i < FolderList.length() - 2; i++) {	
						ENSa.add(i, new ENSObject());
						ENSa.get(i).setBetreff(FolderList.getJSONObject(i+2).getString("name"));
						ENSa.get(i).setENS_id(FolderList.getJSONObject(i+2).getString("ordner_id"));
						ENSa.get(i).setTyp(99);
						ENSa.get(i).setOrdner(folder);
					}
				}
			}

			if (ENSlist.length() != 0) {
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
					
					tempENS.setFlags(2);
					tempENS.setENS_id(ENSlist.getJSONObject(i).getString("id"));
					tempENS.setTyp(ENSlist.getJSONObject(i).getInt("typ"));
					tempENS.setOrdner(folder);
					
					ENSa.add(tempENS);
				}
			}

			ENSArray.addAll(ENSa);

			return (ArrayList<ENSObject>)ENSArray.clone();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ENSObject>();
	}

	public void UpDateUi(String[] s) {
		dialog.dismiss();
		adapter.refill(getENSlist(s, Integer.parseInt(ordner)));
	}

	public void DoError() {
		dialog.dismiss();
		Request.doToast("Fehler", this);

	}

	public void refresh() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true);
		try {
			
		
		if (ordner == "2" && page == 0) {
			HttpGet[] HTTPs = new HttpGet[2];

				HTTPs[0] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="
								+ ordner + "&ordner_typ=" + typ + "&api=2");
				HTTPs[1] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_liste/?ordner_typ="
								+ typ + "&api=2");
				new TaskRequest(this).execute(HTTPs);
				page += 1;
		} else {
			HttpGet[] HTTPs = new HttpGet[1];

				HTTPs[0] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="
								+ ordner + "&ordner_typ=" + typ + "&seite="+page+"&api=2");
				new TaskRequest(this).execute(HTTPs);
				page += 1;
		}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onNewIntent(Intent intent){
		refresh();
	}
}
