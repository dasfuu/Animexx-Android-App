package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.other.UserObject;

public class RPGViewCharaList extends SherlockListActivity implements UpDateUI {

	AlertDialog alertDialog;
	ArrayList<RPGCharaObject> RPGArray = new ArrayList<RPGCharaObject>();
	ProgressDialog dialog;
	final Context context = this;
	RPGCharaAdapter adapter;
	TaskRequest Task = null;
	boolean error = false;
	int mPrevTotalItemCount;
	long id;
	RelativeLayout Loading;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.listview_loading_bot);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("RPG");
		actionBar.setHomeButtonEnabled(true);

		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
		} else
			finish();

		adapter = new RPGCharaAdapter(this, RPGArray);
		setlist(adapter);
		refresh();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void setlist(RPGCharaAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

			}
		});
	}


	private ArrayList<RPGCharaObject> getRPGlist(String JSON) {

		ArrayList<RPGCharaObject> RPGa = new ArrayList<RPGCharaObject>();
		try {
			JSONObject jsonResponse = new JSONObject(JSON);
			JSONObject temp = jsonResponse.getJSONObject("return");
			RPGa = new ArrayList<RPGCharaObject>();

			//ADD RPG-ADMIN
			JSONObject tp = temp.getJSONObject("owner");
			RPGCharaObject RPG = new RPGCharaObject();
			RPG.setAdmin(true);
			RPG.setName("RPG-Admin");
			UserObject u = new UserObject();
			u.ParseJSON(tp);
			RPG.setUser(u);
			RPG.setFree(false);
			RPGa.add(RPG);

			JSONArray RPGlist = temp.getJSONArray("spieler");

			if (RPGlist.length() != 0) {
				for (int i = 0; i < RPGlist.length(); i++) {
					tp = RPGlist.getJSONObject(i);
					RPG = new RPGCharaObject();
					RPG.parseJSON(tp);
					RPGa.add(RPG);
				}
			} else {
				// Keine weiteren RPGs
				error = true;
			}

			RPGlist = temp.getJSONArray("offen");

			if (RPGlist.length() != 0) {
				for (int i = 0; i < RPGlist.length(); i++) {
					tp = RPGlist.getJSONObject(i);
					RPG = new RPGCharaObject();
					RPG.parseJSON(tp);
					RPGa.add(RPG);
				}
			} else {
				// Keine weiteren RPGs
				error = true;
			}


			return RPGa;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return RPGa;

	}


	public void UpDateUi(final String[] s) {
		Loading.setVisibility(View.GONE);
		final ArrayList<RPGCharaObject> z = getRPGlist(s[0]);
		RPGArray.addAll(z);
		adapter.refill();
	}


	public void DoError() {
		Loading.setVisibility(View.GONE);
		error = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
			}
		});
		alertDialog.show();
	}


	public void refresh() {
		try {

			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/get_charaktere/?api=2&rpg=" + id);
			Task = new TaskRequest(this);
			Task.execute(HTTPs);
			Loading.setVisibility(View.VISIBLE);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
