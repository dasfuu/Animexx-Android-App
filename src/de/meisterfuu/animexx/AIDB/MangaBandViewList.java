package de.meisterfuu.animexx.AIDB;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class MangaBandViewList extends SherlockListActivity {

		AlertDialog alertDialog;
		final Context con = this;
		MangaBandAdapter adapter;
		boolean error = false;
		RelativeLayout Loading;
		ArrayList<MangaBandObject> List;
		String name = "Manga";
		
		private SlideMenu slidemenu;
		private SlideMenuHelper slidemenuhelper;


		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Helper.isLoggedIn(this);
			setContentView(R.layout.activity_manga_band_view_list);

			// setup slide menu
			slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
			slidemenu = slidemenuhelper.getSlideMenu();
			// setup action bar
			ActionBar actionBar = getSupportActionBar();;
			actionBar.setHomeButtonEnabled(true);

			Loading = (RelativeLayout) findViewById(R.id.RPGloading);
			Loading.setVisibility(View.GONE);

			Request.config = PreferenceManager.getDefaultSharedPreferences(this);
			
			Bundle bundle = this.getIntent().getExtras();
			name = bundle.getString("name");
			String json = bundle.getString("baende");
			actionBar.setTitle(name);
			List = new ArrayList<MangaBandObject>();


			try {
				refresh(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void refresh(String JSON) throws JSONException {
						JSONArray liste = new JSONArray(JSON);
						// max = FolderList.getJSONObject(0).getInt("gesamt");
						for (int i = 0; i < liste.length(); i++) {
							MangaBandObject tempManga = new MangaBandObject();

							tempManga.parseJSON(liste.getJSONObject(i));
							List.add(tempManga);
						}


						adapter = new MangaBandAdapter(this, List);
						setlist(adapter);
		}
		

		private void setlist(MangaBandAdapter adapter2) {

			setListAdapter(adapter2);

		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getSupportMenuInflater().inflate(R.menu.manga_band_view_list, menu);
			return true;
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

	}

