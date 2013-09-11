package de.meisterfuu.animexx.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.Home.HomeKontaktNewFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.webkit.WebView;
public class EventViewPage extends SherlockActivity implements com.actionbarsherlock.app.ActionBar.TabListener {

	WebView web;
	JSONArray arr;
	long event_id; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_event_view_page);
		
		web = (WebView) this.findViewById(R.id.webView1);
	
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Event");
		actionBar.setHomeButtonEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		try {
			event_id = this.getIntent().getExtras().getLong("event_id");
			arr = new JSONArray(this.getIntent().getExtras().getString("pages"));
			for(int i = 0; i < arr.length(); i++){
				Tab tab = actionBar.newTab().setText(arr.getJSONObject(i).getString("seitenname")).setTabListener(this);
				actionBar.addTab(tab);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			this.finish();
		}
		


		
	}
	
	public void refresh(final long id){
		final EventViewPage temp = this;

		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/events/event/beschreibung_get/?api=2&beschreibung_id=" + id +"&event_id="+event_id);

					JSONObject jsonResponse = new JSONObject(JSON);
					JSONObject data = jsonResponse.getJSONObject("return");
					final String page = data.getString("text_html");

					temp.runOnUiThread(new Runnable() {

						public void run() {
							web.loadDataWithBaseURL("http://http://animexx.onlinewelten.com", page, "text/html", "UTF-8", null);
						}
					});

				} catch (Exception e) {

					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							
						}
					});
				}

			}
		}).start();
		
	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		try {
			refresh(arr.getJSONObject(tab.getPosition()).getLong("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	


}
