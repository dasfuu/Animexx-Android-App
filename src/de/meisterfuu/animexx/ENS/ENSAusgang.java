package de.meisterfuu.animexx.ENS;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ENSAusgang extends ListActivity implements UpDateUI {

	String typ;
	AlertDialog alertDialog;
	JSONArray ENSlist, FolderList;
	ENSObject[] temp;
	String ordner = "2";
	Integer offset = 0;
	ProgressDialog dialog;
	Context con;

	static final String[] COUNTRIES = new String[] { "Afghanistan", "Albania",
			"Algeria", "American Samoa", "Andorra" };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typ = "von";
		con = this;
		if (this.getIntent().hasExtra("folder")) {
			Bundle bundle = this.getIntent().getExtras();
			ordner = bundle.getString("folder");
		}
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true);
		if (ordner == "2") {
			HttpGet[] HTTPs = new HttpGet[2];
			try {
				HTTPs[0] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="
								+ ordner + "&ordner_typ=" + typ + "&api=2");
				HTTPs[1] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_liste/?ordner_typ="
								+ typ + "&api=2");
				new TaskRequest(this).execute(HTTPs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			HttpGet[] HTTPs = new HttpGet[1];
			try {
				HTTPs[0] = Request
						.getHTTP("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="
								+ ordner + "&ordner_typ=" + typ + "&api=2");
				new TaskRequest(this).execute(HTTPs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setlist(ENSAdapter a) {

		setListAdapter(a);
		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
				if (pos >= offset) {
					ENSPopUp Menu = new ENSPopUp(con, temp[pos].von,
							temp[pos].von_id, temp[pos].ENS_id,
							temp[pos].betreff);
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
					i = temp[position].ENS_id;
					// Request.doToast(""+i, getApplicationContext());
					Bundle bundle = new Bundle();
					bundle.putString("folder", i);
					Intent newIntent = new Intent(getApplicationContext(),
							ENSAusgang.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				} else {
					i = temp[position].ENS_id;
					Bundle bundle = new Bundle();
					bundle.putString("id", i);
					Intent newIntent = new Intent(getApplicationContext(),
							ENSSingle.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				}

			}

		});
	}

	private ENSObject[] getENSlist(String[] JSON, int folder) {
		try {
			// JSONObject jsonResponse = new
			// JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/ens/ordner_ens_liste/?ordner_id="+folder+"&ordner_typ="+typ+"&api=2"));
			JSONObject jsonResponse = new JSONObject(JSON[0]);
			ENSlist = jsonResponse.getJSONArray("return");

			if (JSON.length > 1) {
				// JSONObject jsonResponse2 = new
				// JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/ens/ordner_liste/?ordner_typ="+typ+"&api=2"));
				JSONObject jsonResponse2 = new JSONObject(JSON[1]);
				FolderList = jsonResponse2.getJSONObject("return")
						.getJSONArray("von");
				offset = FolderList.length() - 2;
			} else {
				offset = 0;
			}

			ENSObject[] ENSa;
			if ((ENSlist.length() + offset) > 0) {
				ENSa = new ENSObject[(ENSlist.length() + offset)];
			} else {
				ENSa = new ENSObject[1];
				ENSa[0] = new ENSObject("Leer :/", "" + folder, 99, 0);
				offset += 1;
			}

			if (JSON.length > 1) {
				if (FolderList.length() != 0) {
					for (int i = 0; i < FolderList.length() - 2; i++) {
						ENSa[i] = new ENSObject(FolderList.getJSONObject(i + 2)
								.getString("name"), FolderList.getJSONObject(
								i + 2).getString("ordner_id"), 99, 0);
					}
				}
			}

			if (ENSlist.length() != 0) {
				for (int i = 0; i < ENSlist.length(); i++) {
					ENSa[i + offset] = new ENSObject(
							"//",
							ENSlist.getJSONObject(i).getString("betreff"),
							ENSlist.getJSONObject(i).getJSONObject("von")
									.getString("username"),
							ENSlist.getJSONObject(i).getJSONObject("von")
									.getString("id"),
							"//",
							ENSlist.getJSONObject(i).getString("datum_server")/*
																			 * <--
																			 * TIME
																			 */,
							3, 0, 0, 0, ENSlist.getJSONObject(i)
									.getString("id"), ENSlist.getJSONObject(i)
									.getInt("typ"), folder);
				}
			}

			temp = ENSa;
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ENSObject[] { new ENSObject("", "Fehler beim Abrufen", "",
				"", "", "", 10, 0, 0, 0, "0", -1, 0) };
	}

	public void UpDateUi(String[] s) {
		dialog.dismiss();
		setlist(new ENSAdapter(this, getENSlist(s, Integer.parseInt(ordner))));
	}

	public void DoError() {
		// TODO Auto-generated method stub

	}

}
