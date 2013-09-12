package de.meisterfuu.animexx.AIDB;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

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
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class MangaViewList extends SherlockListActivity {

	AlertDialog alertDialog;
	final Context con = this;
	MangaAdapter adapter;
	boolean error = false;
	RelativeLayout Loading;
	ArrayList<MangaObject> List;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_manga_view_list);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();;
		actionBar.setHomeButtonEnabled(true);

		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		
		
		actionBar.setTitle("Meine Manga");
		List = new ArrayList<MangaObject>();


		refresh();
	}

	public void refresh() {
		Loading.setVisibility(View.VISIBLE);
		final MangaViewList temp = this;
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/aidb/mangas/meine_detailliert/?&api=2");
					JSONObject liste = null;

					JSONObject jsonResponse = new JSONObject(JSON);

					liste = jsonResponse.getJSONObject("return");
					// max = FolderList.getJSONObject(0).getInt("gesamt");
					@SuppressWarnings("unchecked")
					Iterator<String> it = liste.keys();
					while(it.hasNext()){
						MangaObject tempManga = new MangaObject();

						tempManga.parseJSON(liste.getJSONObject(it.next()));
						List.add(tempManga);
					}
		
					temp.runOnUiThread(new Runnable() {

						public void run() {
							adapter = new MangaAdapter(temp, List);
							Loading.setVisibility(View.GONE);
							setlist(adapter);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
						}
					});
				}

			}
		});
		
		t.start();
	}

	private void setlist(MangaAdapter adapter2) {

		setListAdapter(adapter2);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("name", List.get(pos).getName());
				bundle.putString("baende", List.get(pos).getBaende().toString());
				Intent newIntent = new Intent(con.getApplicationContext(), MangaBandViewList.class);
				newIntent.putExtras(bundle);
				con.startActivity(newIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.manga_view_list, menu);
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
