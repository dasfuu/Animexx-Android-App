package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;

public class RPGViewList extends ListActivity implements UpDateUI {

	AlertDialog alertDialog;
	ArrayList<RPGObject> RPGArray = new ArrayList<RPGObject>();
	final Context context = this;
	RPGAdapter adapter;
	TaskRequest Task = null;
	boolean error = false;
	int mPrevTotalItemCount;
	RelativeLayout Loading;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.listview_loading_bot);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		adapter = new RPGAdapter(this, RPGArray);
		setlist(adapter);
	}


	private void setlist(RPGAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

				RPGPopUp Menu = new RPGPopUp(context, RPGArray.get(pos).getId(), RPGArray.get(pos).getName(), RPGArray.get(pos).getPostCount(), RPGArray.get(pos).isTofu());
				Menu.PopUp();
				return true;
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
					mPrevTotalItemCount = totalItemCount;
					if (!error) {
						refresh();
					}
				}
			}


			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// Useless Forced Method -.-
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				Bundle bundle = new Bundle();
				bundle.putLong("id", RPGArray.get(pos).getId());
				bundle.putLong("count", RPGArray.get(pos).getPostCount());
				bundle.putBoolean("tofu", RPGArray.get(pos).isTofu());
				Intent newIntent = new Intent(getApplicationContext(), RPGViewPostList.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);

			}
		});
	}


	private ArrayList<RPGObject> getRPGlist(String JSON) {

		ArrayList<RPGObject> RPGa = new ArrayList<RPGObject>();
		try {
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
				// Keine weiteren RPGs
				error = true;
			}

			if (RPGlist.length() < 30) {
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
		ArrayList<RPGObject> z = getRPGlist(s[0]);
		RPGArray.addAll(z);
		adapter.refill();
	}


	public void DoError() {
		Loading.setVisibility(View.GONE);
		error = true;
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
			}
		});
		alertDialog.show();
	}


	public void refresh() {

		Loading.setVisibility(View.VISIBLE);

		try {
			int finishedRPG = 0;
			if(Request.config.getBoolean("showRPGfinished", false)) {
				finishedRPG = 1;
			}
			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/meine_rpgs/?beendete="+finishedRPG+"&api=2&offset=" + RPGArray.size());
			Task = new TaskRequest(this);
			Task.execute(HTTPs);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		RPGArray.clear();
		refresh();		
	}

}
