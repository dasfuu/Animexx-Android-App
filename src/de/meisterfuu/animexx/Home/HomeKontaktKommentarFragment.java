package de.meisterfuu.animexx.Home;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloader;


public class HomeKontaktKommentarFragment extends SherlockFragment {

	ArrayList<HomeKontaktKommentarObject> Array = new ArrayList<HomeKontaktKommentarObject>();

	Context context;
	ProgressDialog dialog;
	Boolean loading, loaded = false;
	HomeKontaktKommentarAdapter adapter;
	ImageDownloader Images = new ImageDownloader();
	int loadCount = 0;
	RelativeLayout Loading;
	String id;


	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_list, null);
		Loading = (RelativeLayout) view.findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		context = this.getActivity();

		adapter = new HomeKontaktKommentarAdapter(this, Array);
		adapter.Images = Images;
		ListView lv = (ListView) view.findViewById(R.id.HomeListView);
		lv.setAdapter(adapter);

		if (!loaded) {
			loaded = true;
			if (getArguments() != null && !getArguments().isEmpty()) {
				id = new String(getArguments().getCharArray("data"));			
			} 
			refresh();
			
		}

		return view;
	}


	public static HomeKontaktKommentarFragment newInstance(String data) {
		HomeKontaktKommentarFragment myFragment = new HomeKontaktKommentarFragment();

		Bundle args = new Bundle();
		args.putCharArray("data", data.toCharArray());
		myFragment.setArguments(args);

		return myFragment;
	}


	public static HomeKontaktKommentarFragment newInstance() {
		HomeKontaktKommentarFragment myFragment = new HomeKontaktKommentarFragment();

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
			//Collections.sort(Array);
		}
	}


	public void refresh() {

		getKontakt(Array);

	}


	public void getKontakt(final ArrayList<HomeKontaktKommentarObject> Liste) {
		LoadPlus();
		new Thread(new Runnable() {

			public void run() {
				try {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.WEEK_OF_YEAR, -1);
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart5/get_widget_kommentare/?api=2&widget_id=kontakte&item_id="+id);
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeKontaktKommentarObject tempObject = new HomeKontaktKommentarObject(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					HomeKontaktKommentarFragment.this.getActivity().runOnUiThread(new Runnable() {

						public void run() {
							//Collections.sort(Array);
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

