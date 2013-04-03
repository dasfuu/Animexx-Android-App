package de.meisterfuu.animexx.overview;

import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.events.EventObject;
import de.meisterfuu.animexx.events.EventViewDetail;
import de.meisterfuu.animexx.other.MultiListAdapter;
import de.meisterfuu.animexx.other.MultiSherlockListActivity;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class OverviewActivity extends MultiSherlockListActivity {

	EventObject Event;
	JSONObject tempDetail;

	Context context = this;

	ProgressDialog dialog;
	RelativeLayout Loading;
	Boolean loading;

	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


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
		actionBar.setTitle("Event");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		adapter = new MultiListAdapter(this, Array);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setDivider(null);
		lv.setDividerHeight(0);
		
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lv.getLayoutParams();
		mlp.setMargins(20, 0, 20, 0);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(Array.get(position).getOnclicklistener() != null)Array.get(position).getOnclicklistener().onClick(view);
			}

		});
		

		//refresh();
		populate();
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

		final OverviewActivity temp = this;
		Loading.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/events/event/details/?api=2&id=");

					JSONObject jsonResponse = new JSONObject(JSON);
					JSONObject data = jsonResponse.getJSONObject("return");

					Event = new EventObject();
					Event.parseJSON(data);
					Event.setDetail_json(data.toString());
					tempDetail = data;

					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
							populate();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
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
		Request.doToast("Fehler", this);
	}


	public void populate() {

		this.addText("Eingeloggt als: "+ Request.config.getString("username", "Unbekannt"));
		
		adapter.notifyDataSetChanged();


	}
	

}
