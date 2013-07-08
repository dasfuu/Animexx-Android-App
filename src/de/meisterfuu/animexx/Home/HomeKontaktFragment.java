package de.meisterfuu.animexx.Home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloader;

public class HomeKontaktFragment extends SherlockFragment {

	ArrayList<HomeKontaktObject> Array = new ArrayList<HomeKontaktObject>();

	Context context;
	ProgressDialog dialog;
	Boolean loading, loaded = false;
	HomeKontaktAdapter adapter;
	ImageDownloader Images = new ImageDownloader();
	int loadCount = 0;
	RelativeLayout Loading;


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list, null);
		Loading = (RelativeLayout) view.findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		context = this.getActivity();

		adapter = new HomeKontaktAdapter(this, Array);
		adapter.Images = Images;
		ListView lv = (ListView) view.findViewById(R.id.HomeListView);
		lv.setAdapter(adapter);

		if (!loaded) {
			loaded = true;
			if (getArguments() != null && getArguments().getCharArray("data") != null) {
				JSONArray list = null;
				try {
					list = new JSONArray(new String(getArguments().getCharArray("data")));

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeKontaktObject tempObject = new HomeKontaktObject(list.getJSONObject(i));
							Array.add(tempObject);
						}
					}
					Collections.sort(Array);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				refresh();
			}
		}

		return view;
	}


	public static HomeKontaktFragment newInstance(String data) {
		HomeKontaktFragment myFragment = new HomeKontaktFragment();

		Bundle args = new Bundle();
		args.putCharArray("data", data.toCharArray());
		myFragment.setArguments(args);

		return myFragment;
	}


	public static HomeKontaktFragment newInstance() {
		HomeKontaktFragment myFragment = new HomeKontaktFragment();

		return myFragment;
	}


	/*
	 * public boolean onKeyUp(int keyCode, KeyEvent event) {
	 * if (keyCode == KeyEvent.KEYCODE_MENU) {
	 * Filter.animateToggle();
	 * return true;
	 * }
	 * return super.onKeyUp(keyCode, event);
	 * }
	 */

	public void DoError() {
		Request.doToast("Fehler", this.getActivity());
	}


	public void LoadPlus() {
		loadCount++;
		Loading.setVisibility(View.VISIBLE);
	}


	public void LoadMinus() {
		loadCount--;
		if (loadCount == 0) {
			Loading.setVisibility(View.GONE);
			Collections.sort(Array);
		}
	}


	public void refresh() {

		getKontakt(Array);

	}


	public void getKontakt(final ArrayList<HomeKontaktObject> Liste) {
		LoadPlus();
		final HomeKontaktActivity temp = (HomeKontaktActivity) this.getActivity();
		new Thread(new Runnable() {

			public void run() {
				try {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.WEEK_OF_YEAR, -1);
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart5/get_widget_data/?api=2&widget_id=kontakte&return_typ=app&zeit_von=" + cal.getTimeInMillis());
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeKontaktObject tempObject = new HomeKontaktObject(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
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
