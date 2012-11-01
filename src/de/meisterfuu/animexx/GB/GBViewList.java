package de.meisterfuu.animexx.GB;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.other.UserObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;

public class GBViewList extends SherlockListActivity implements UpDateUI {

	AlertDialog alertDialog;

	ArrayList<GBObject> GBArray = new ArrayList<GBObject>();
	final Context con = this;
	int page;
	int mPrevTotalItemCount;
	GBAdapter adapter;
	TaskRequest Task = null;
	boolean error = false;
	String id, username;
	RelativeLayout Loading;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.listview_loading_bot);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();;
		actionBar.setHomeButtonEnabled(true);

		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		username = "none";
		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getString("id");
			username = bundle.getString("username");
		} else {
			id = Request.config.getString("id", "none");
			username = Request.config.getString("username", "none");
		}
		
		actionBar.setTitle(username);

		adapter = new GBAdapter(this, GBArray);
		setlist(adapter);
		refresh();

	}


	private void setlist(GBAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
				GBPopUp Menu = new GBPopUp(con, GBArray.get(pos).getVon().getUsername(), GBArray.get(pos).getVon().getId(), GBArray.get(pos).getEntry_id(), GBArray.get(pos).getEinleitung());
				Menu.PopUp();
				return true;
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
					mPrevTotalItemCount = totalItemCount;
					if (!error) refresh();
				}
			}


			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Useless Forced Method -.-
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("von", GBArray.get(pos).getVon().getUsername());
				bundle.putString("an", username);
				bundle.putString("von_id", GBArray.get(pos).getVon().getId());
				bundle.putString("id", GBArray.get(pos).getEntry_id());
				bundle.putString("text", GBArray.get(pos).getText());
				Intent newIntent = new Intent(con.getApplicationContext(), GBViewSingle.class);
				newIntent.putExtras(bundle);
				con.startActivity(newIntent);
			}
		});
	}


	private ArrayList<GBObject> getENSlist(String JSON) {

		ArrayList<GBObject> GBa = new ArrayList<GBObject>();
		try {
			JSONObject jsonResponse = new JSONObject(JSON);
			JSONArray GBlist = jsonResponse.getJSONObject("return").getJSONArray("eintraege");
			GBa = new ArrayList<GBObject>(GBlist.length());

			if (GBlist.length() != 0) {
				for (int i = 0; i < GBlist.length(); i++) {
					JSONObject tp = GBlist.getJSONObject(i);
					GBObject GB = new GBObject();

					UserObject von = new UserObject();
					if (!tp.isNull("von")) von.ParseJSON(tp.getJSONObject("von"));
					GB.setVon(von);

					GB.setText(tp.getString("text_html"));
					GB.setEntry_id(tp.getString("id"));
					GB.setTime(tp.getString("datum_server"));
					GB.setAvatar(tp.getString("avatar"));
					GBa.add(GB);
				}
			} else {
				// Keine Einträge im Gästebuch
				error = true;
				return GBa;
			}

			return GBa;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return GBa;

	}


	public void UpDateUi(final String[] s) {

		Loading.setVisibility(View.GONE);
		final ArrayList<GBObject> z = getENSlist(s[0]);
		GBArray.addAll(z);
		adapter.refill();
		Helper.Tutorial("Eintrag antippen, um ihn komplett zu lesen!", "gblist", con);
		page++;
	}


	public void DoError() {
		Loading.setVisibility(View.GONE);
		error = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				((Activity) con).finish();
			}
		});
		alertDialog.show();
	}


	public void refresh() {

		Loading.setVisibility(View.VISIBLE);

		try {

			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id=" + id + "&text_format=html&api=2&seite=" + page);

			Task = new TaskRequest(this);
			Task.execute(HTTPs);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.ac_answer:
			Bundle bundle = new Bundle();
			bundle.putString("ID", id);
			bundle.putString("an", username);
			Intent newIntent = new Intent(con.getApplicationContext(),
					GBAnswer.class);
			newIntent.putExtras(bundle);
			con.startActivity(newIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_answer, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
