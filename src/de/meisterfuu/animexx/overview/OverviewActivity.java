package de.meisterfuu.animexx.overview;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.KaroTalerAlert;
import de.meisterfuu.animexx.other.MultiListAdapter;
import de.meisterfuu.animexx.other.MultiSherlockListActivity;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class OverviewActivity extends MultiSherlockListActivity {

	JSONObject tempDetail;

	Context context = this;

	ProgressDialog dialog;
	RelativeLayout Loading;
	Boolean loading;

	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	int KaroGuthaben, KaroAbholbar, newENS;
	boolean karo = false;
	boolean ENS = false;


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
		actionBar.setTitle("Dashboard");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		adapter = new MultiListAdapter(this, Array);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setDivider(null);
		lv.setDividerHeight(0);

		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lv.getLayoutParams();
		mlp.setMargins(30, 40, 30, 40);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (Array.get(position).getOnclicklistener() != null) Array.get(position).getOnclicklistener().onClick(view);
			}

		});

		refresh();
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
		getKaroTalerStats();
		getNewENSStats();
	}


	public void DoError() {
		Request.doToast("Fehler", this);
	}


	public void populate() {

		this.Array.clear();

		this.addText("Eingeloggt als: " + Request.config.getString("username", "Unbekannt"));

		if (this.karo) {
			this.addSpacer();
			if (KaroAbholbar > 0) {

				OnClickListener KatoralerClick = new OnClickListener() {

					public void onClick(View v) {
						Log.i("xx", "KaroTaler Receiver 2");
						Bundle bundle = new Bundle();
						bundle.putInt("abholbar", KaroAbholbar);
						bundle.putInt("guthaben", KaroGuthaben);
						Intent newIntent = new Intent(context, KaroTalerAlert.class);
						newIntent.putExtras(bundle);
						newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(newIntent);
					}
				};

				this.addText("Du kannst " + KaroAbholbar + " abholen. (Guthaben: " + KaroGuthaben + ")", KatoralerClick);
			} else {
				this.addText("Du hast " + KaroGuthaben + " Karotaler.");
			}
		}

		if (this.ENS) {
			this.addSpacer();
			OnClickListener ENSClick = new OnClickListener() {

				public void onClick(View v) {
					Intent newIntent = new Intent(context, ENSActivity.class);
					context.startActivity(newIntent);
				}
			};
			if (newENS > 0) {
				this.addText("Du hast " + newENS + " ungelesene ENS.", ENSClick);
			} else {
				this.addText("Du hast keine ungelesenen ENS.", ENSClick);
			}
		}

		adapter.notifyDataSetChanged();

	}


	public void getKaroTalerStats() {

		final OverviewActivity temp = this;
		Loading.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			public void run() {
				try {

					JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_statistik/?api=2"));
					JSONArray ab = a.getJSONObject("return").getJSONArray("kt_abholbar");
					if (ab.length() > 0) {
						KaroAbholbar = ab.getJSONObject(0).getInt("kt");
					} else {
						KaroAbholbar = 0;
					}
					KaroGuthaben = a.getJSONObject("return").getInt("kt_guthaben");

					temp.runOnUiThread(new Runnable() {

						public void run() {
							temp.karo = true;
							temp.populate();
							Loading.setVisibility(View.GONE);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
							DoError();
						}
					});
				}
			}
		}).start();

	}


	public void getNewENSStats() {

		final OverviewActivity temp = this;
		Loading.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			public void run() {
				try {

					newENS = Request.GetUnread();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							temp.ENS = true;
							temp.populate();
							Loading.setVisibility(View.GONE);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
							DoError();
						}
					});
				}
			}
		}).start();

	}

}
