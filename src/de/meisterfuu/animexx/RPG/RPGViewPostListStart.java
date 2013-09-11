package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;
import java.util.Collections;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

public class RPGViewPostListStart extends SherlockListActivity implements UpDateUI {

	AlertDialog alertDialog;
	ArrayList<RPGPostObject> RPGArray = new ArrayList<RPGPostObject>();
	ProgressDialog dialog;
	final Context context = this;
	RPGPostAdapter adapter;
	TaskRequest Task = null;
	int mPrevTotalItemCount;
	// private BroadcastReceiver receiver;
	EditText edAnswer;
	boolean error;
	long id;
	long count = 1;
	RelativeLayout Loading;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;
	private ImageDownloaderCustom ImageLoader;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.rpg_post_list_slide);
		edAnswer = (EditText) findViewById(R.id.ed_answer);

		Loading = (RelativeLayout) findViewById(R.id.RPGloading);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("RPG");
		actionBar.setHomeButtonEnabled(true);
		
		ImageLoader = new ImageDownloaderCustom("RPG_Avatar");
		LinearLayout active_char = (LinearLayout) findViewById(R.id.active_char);
		active_char.setVisibility(View.GONE);

		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
		} else
			finish();

		adapter = new RPGPostAdapter(this, RPGArray, ImageLoader);
		setlist(adapter);
		refresh();
		Loading.setVisibility(View.GONE);
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


	@Override
	protected void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(receiver);
	}


	private void setlist(RPGPostAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

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

				refresh();

			}
		});
	}


	private ArrayList<RPGPostObject> getRPGlist(String JSON) {

		ArrayList<RPGPostObject> RPGa = new ArrayList<RPGPostObject>();
		try {
			JSONObject jsonResponse = new JSONObject(JSON);
			JSONArray RPGlist = jsonResponse.getJSONArray("return");
			RPGa = new ArrayList<RPGPostObject>(RPGlist.length());

			if (RPGlist.length() != 0) {
				for (int i = 0; i < RPGlist.length(); i++) {
					JSONObject tp = RPGlist.getJSONObject(i);
					RPGPostObject RPG = new RPGPostObject(id);
					RPG.parseJSON(tp);
					if ((RPG.getId() >= count) || RPGArray.isEmpty()) RPGa.add(RPG);
				}

			} else {
				// Keine weiteren Posts
				error = true;
			}

			return RPGa;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return RPGa;

	}


	public void UpDateUi(final String[] s) {

		final ArrayList<RPGPostObject> z = getRPGlist(s[0]);
		Loading.setVisibility(View.GONE);
		RPGArray.addAll(z);
		Collections.sort(RPGArray);
		adapter.refill();
		if (RPGArray.size() != 0) count = RPGArray.get(RPGArray.size() - 1).getId() + 1;
	}


	public void DoError() {

		error = true;
		Loading.setVisibility(View.GONE);
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
			Loading.setVisibility(View.VISIBLE);
			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/get_postings/?api=2&rpg=" + id + "&limit=30&text_format=html&from_pos=" + count);
			Task = new TaskRequest(this);
			Task.execute(HTTPs);

		} catch (Exception e) {
			DoError();
			e.printStackTrace();
		}

	}


	@Override
	public void onBackPressed() {
		Task.cancel(true);
		finish();
		return;
	}

}
