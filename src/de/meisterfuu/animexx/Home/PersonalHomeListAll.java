package de.meisterfuu.animexx.Home;

import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

public class PersonalHomeListAll extends SherlockListActivity {

	ArrayList<HomeListObject> Array = new ArrayList<HomeListObject>();
	// ArrayList<HomeListObject> All = new ArrayList<HomeListObject>();

	ArrayList<HomeListObject> Cosplay = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> Kontakte = new ArrayList<HomeListObject>();

	ArrayList<HomeListObject> WeblogCom = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> FanficCom = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> FanartCom = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> FanworkCom = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> UmfrageCom = new ArrayList<HomeListObject>();
	ArrayList<HomeListObject> DojinshiCom = new ArrayList<HomeListObject>();

	Context context = this;
	ProgressDialog dialog;
	Boolean loading;
	HomeListAdapter adapter;
	ImageDownloader Images = new ImageDownloader();
	int loadCount = 0;
	RelativeLayout Loading;
	SlidingDrawer Filter;
	CheckBox fCosplay, fWeblogCom, fFanficCom, fFanartCom, fFanworkCom, fKontakte, fUmfrageCom, fDojinshiCom;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.home_list);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Filter = (SlidingDrawer) findViewById(R.id.HomeListFilter);
		Loading.setVisibility(View.GONE);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Home");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		fCosplay = (CheckBox) findViewById(R.id.fCosplay);
		fWeblogCom = (CheckBox) findViewById(R.id.fWeblogCom);
		fFanficCom = (CheckBox) findViewById(R.id.fFanficCom);
		fFanartCom = (CheckBox) findViewById(R.id.fFanartCom);
		fFanworkCom = (CheckBox) findViewById(R.id.fFanworkCom);
		fKontakte = (CheckBox) findViewById(R.id.fKontakte);
		fUmfrageCom = (CheckBox) findViewById(R.id.fUmfrageCom);
		fDojinshiCom = (CheckBox) findViewById(R.id.fDojinshiCom);

		fCosplay.setChecked(Request.config.getBoolean("fCosplay", true));
		fWeblogCom.setChecked(Request.config.getBoolean("fWeblogCom", true));
		fFanficCom.setChecked(Request.config.getBoolean("fFanficCom", true));
		fFanartCom.setChecked(Request.config.getBoolean("fFanartCom", true));
		fFanworkCom.setChecked(Request.config.getBoolean("fFanworkCom", true));
		fKontakte.setChecked(Request.config.getBoolean("fKontakte", true));
		fUmfrageCom.setChecked(Request.config.getBoolean("fUmfrageCom", true));
		fDojinshiCom.setChecked(Request.config.getBoolean("fDojinshiCom", true));

		setClick(fCosplay);
		setClick(fWeblogCom);
		setClick(fFanficCom);
		setClick(fFanartCom);
		setClick(fFanworkCom);
		setClick(fKontakte);
		setClick(fUmfrageCom);
		setClick(fDojinshiCom);

		adapter = new HomeListAdapter(this, Array);
		adapter.Images = Images;
		setListAdapter(adapter);
		refresh();
	}


	private void setClick(CheckBox b) {
		b.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Filter();
			}

		});
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
	public void onBackPressed() {
		if (Filter.isOpened()) {
			Filter.animateClose();
		} else {
			super.onBackPressed();
		}

	}


	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			Filter.animateToggle();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}


	public void DoError() {
		Request.doToast("Fehler", this);
	}


	public void LoadPlus() {
		loadCount++;
		Loading.setVisibility(View.VISIBLE);
		Filter.setVisibility(View.GONE);
	}


	public void LoadMinus() {
		loadCount--;
		if (loadCount == 0) {
			Loading.setVisibility(View.GONE);
			Filter.setVisibility(View.VISIBLE);
			Collections.sort(Array);
			Filter();
		}
	}


	public void Filter() {
		Array = new ArrayList<HomeListObject>();

		if (fKontakte.isChecked()) Array.addAll(Kontakte);
		if (fWeblogCom.isChecked()) Array.addAll(WeblogCom);
		if (fFanartCom.isChecked()) Array.addAll(FanartCom);
		if (fDojinshiCom.isChecked()) Array.addAll(DojinshiCom);
		if (fFanficCom.isChecked()) Array.addAll(FanficCom);
		if (fFanworkCom.isChecked()) Array.addAll(FanworkCom);
		if (fUmfrageCom.isChecked()) Array.addAll(UmfrageCom);
		if (fCosplay.isChecked()) Array.addAll(Cosplay);

		Collections.sort(Array);
		adapter.refill(Array);

		Editor edit = Request.config.edit();
		edit.putBoolean("fKontakte", fKontakte.isChecked());
		edit.putBoolean("fCosplay", fCosplay.isChecked());
		edit.putBoolean("fWeblogCom", fWeblogCom.isChecked());
		edit.putBoolean("fUmfrageCom", fUmfrageCom.isChecked());
		edit.putBoolean("fDojinshiCom", fDojinshiCom.isChecked());
		edit.putBoolean("fFanworkCom", fFanworkCom.isChecked());
		edit.putBoolean("fFanficCom", fFanficCom.isChecked());
		edit.putBoolean("fFanartCom", fFanartCom.isChecked());
		edit.commit();
	}


	@SuppressWarnings("unused")
	public void AddKommentare(ArrayList<HomeListObject> Source, ArrayList<HomeListObject> Target) {
		if (!false) {
			for (int i = 0; i < Source.size(); i++) {
				if (!((HomeCommentObject) Source.get(i)).isAbo()) {
					Target.add(Source.get(i));
				}
			}

		} else {
			Target.addAll(Source);
		}
	}


	public void refresh() {

		getKontakte(Kontakte);
		getTopCosplay(Cosplay);
		getKommentar(WeblogCom, "2_6", HomeListObject.WEBLOGKOMMENTAR, fWeblogCom);
		getKommentar(FanworkCom, "2_27", HomeListObject.FANWORKKOMMENTAR, fFanworkCom);
		getKommentar(WeblogCom, "2_25", HomeListObject.UMFRAGEKOMMENTAR, fUmfrageCom);
		getKommentar(WeblogCom, "2_9", HomeListObject.DOJINSHIKOMMENTAR, fDojinshiCom);
		getKommentar(WeblogCom, "2_8", HomeListObject.FANFICKOMMENTAR, fFanficCom);
		getKommentar(WeblogCom, "2_7", HomeListObject.FANARTKOMMENTAR, fFanartCom);

	}


	public void getKommentar(final ArrayList<HomeListObject> Liste, final String app, final int typ, final CheckBox Check) {
		LoadPlus();
		final PersonalHomeListAll temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart/get_widget_data/?api=2&widget_id=" + app + "&return_typ=app");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeListObject tempObject = new HomeCommentObject(typ);
							tempObject.parseJSON(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (Check.isChecked()) Array.addAll(Liste);
							Collections.sort(Array);
							adapter.notifyDataSetChanged();
							LoadMinus();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}


	public void getKontakte(final ArrayList<HomeListObject> Liste) {
		LoadPlus();
		final PersonalHomeListAll temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart/get_widget_data/?api=2&widget_id=2_19&return_typ=app");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONObject("return").getJSONArray("typ1");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeListObject tempObject = new ContactActivityObject();
							tempObject.parseJSON(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (fKontakte.isChecked()) Array.addAll(Liste);
							Collections.sort(Array);
							adapter.notifyDataSetChanged();
							LoadMinus();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}


	public void getTopCosplay(final ArrayList<HomeListObject> Liste) {
		LoadPlus();
		final PersonalHomeListAll temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart/get_widget_data/?api=2&widget_id=1_8&return_typ=app");
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					Liste.ensureCapacity(4);

					if (list.length() != 0) {
						for (int i = 3; i >= 0; i--) {
							HomeListObject tempObject = new HomeCosplayObject(HomeListObject.COSPLAY);
							tempObject.parseJSON(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (fCosplay.isChecked()) Array.addAll(Liste);
							Collections.sort(Array);
							adapter.notifyDataSetChanged();
							LoadMinus();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

}
