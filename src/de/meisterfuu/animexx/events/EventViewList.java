package de.meisterfuu.animexx.events;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

public class EventViewList extends SherlockListActivity {

	ArrayList<EventObject> Array = new ArrayList<EventObject>();

	boolean first = false;

	Context context = this;

	ProgressDialog dialog;
	RelativeLayout Loading;
	Boolean loading;

	EventAdapter adapter;
	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.listview_loading_bot);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Events");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		adapter = new EventAdapter(this, Array);
		setListAdapter(adapter);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Bundle bundle = new Bundle();
				bundle.putLong("id", Array.get(position).getId());
				Intent newIntent = new Intent(context, EventViewDetail.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);

			}

		});

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


	private void refresh() {

		final EventViewList temp = this;
		Loading.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/events/event/dabei_events/?api=2&alle=1");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							EventObject tempObject = new EventObject();
							tempObject.parseJSON(list.getJSONObject(i));
							Array.add(tempObject);
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							adapter.notifyDataSetChanged();
							Loading.setVisibility(View.GONE);

						}
					});

				} catch (Exception e) {
					
					EventSQLHelper SQL = new EventSQLHelper(temp);
					SQL.open();
					Array.clear();
					Array.addAll(SQL.getAllEvents());					
					SQL.close();
					
					temp.runOnUiThread(new Runnable() {

						public void run() {
							adapter.notifyDataSetChanged();
							Loading.setVisibility(View.GONE);
							DoError();

						}
					});
				}

			}
		}).start();

	}


	public void DoError() {
		Request.doToast("Kein Internet. Zeige Offline-Daten.", this);
	}

}
