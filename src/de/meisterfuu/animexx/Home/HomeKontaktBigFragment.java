package de.meisterfuu.animexx.Home;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.app.ProgressDialog;
import android.content.Context;

public class HomeKontaktBigFragment extends SherlockFragment implements Refreshable {

	ArrayList<HomeKontaktObject> Array = new ArrayList<HomeKontaktObject>();

	Context context;
	ProgressDialog dialog;
	Boolean loading, loaded = false;
	HomeKontaktBigAdapter adapter;
	ListView lv;
	ImageDownloaderCustom Images = new ImageDownloaderCustom("kontakt_big");
	int loadCount = 0;
	RelativeLayout Loading;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_home_kontakt_big, null);
		super.onCreate(savedInstanceState);

		Loading = (RelativeLayout) view.findViewById(R.id.RPGloading);

		
		adapter = new HomeKontaktBigAdapter((HomeKontaktActivity)this.getSherlockActivity(), Array);
		adapter.Images = Images;
		lv = (ListView) view.findViewById(R.id.kontakt_list);
		lv.setAdapter(adapter);
		
		if (!loaded)refresh();
		
		return view;
	}
	
	public static HomeKontaktBigFragment newInstance() {
		HomeKontaktBigFragment myFragment = new HomeKontaktBigFragment();

		return myFragment;
	}




	
	public void DoError() {
		Request.doToast("Fehler",this.getSherlockActivity());
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
		Array = new ArrayList<HomeKontaktObject>();
		loaded = true;
		getKontakt(Array);

	}


	public void getKontakt(final ArrayList<HomeKontaktObject> Liste) {
		LoadPlus();
		final SherlockFragmentActivity temp = this.getSherlockActivity();
		new Thread(new Runnable() {

			public void run() {
				try {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_YEAR, -2);
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart5/get_widget_data/?api=2&widget_id=kontakte&img_max_x=800&img_max_y=700&img_format=jpg&img_quality=70&return_typ=app&zeit_von=" + (cal.getTimeInMillis()/1000));
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeKontaktObject tempObject = new HomeKontaktObject(list.getJSONObject(i));
							if(tempObject.isMulti_item()){
								for(int j = 0; j < tempObject.getItems().length; j++){
									Liste.add(tempObject.getItems()[j]);
								}
							} else {
								Liste.add(tempObject);
							}
						}
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							Collections.sort(Array);
							adapter = new HomeKontaktBigAdapter((HomeKontaktActivity)temp, Array);
							adapter.Images = Images;
							lv.setAdapter(adapter);
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
