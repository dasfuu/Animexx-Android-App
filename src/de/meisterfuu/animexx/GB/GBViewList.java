package de.meisterfuu.animexx.GB;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;

public class GBViewList extends ListActivity {

	String typ;
	JSONArray GBlist;
	GBObject[] temp;
	String id2, username;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		NotificationManager mManager;
		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(43);
		

		typ = "an";
		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id2 = bundle.getString("id");
		} else {
			id2 = Request.config.getString("id", "none");
			username = "Dich!";
		}

		final GBViewList tempAPP = this;
		final ProgressDialog dialog = ProgressDialog.show(tempAPP, "",
				Constants.LOADING, true);
		new Thread(new Runnable() {
			public void run() {
				final GBAdapter a;
				a = new GBAdapter(tempAPP, getGBlist());
				tempAPP.runOnUiThread(new Runnable() {
					public void run() {
						setlist(a);
						dialog.dismiss();
					}
				});
			}
		}).start();
	}

	private void setlist(GBAdapter a) {
		setListAdapter(a);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}

	private GBObject[] getGBlist() {
		try {
			JSONObject jsonResponse = new JSONObject(
					Request.makeSecuredReq("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id="
							+ id2 + "&text_format=html&api=2"));
			GBlist = jsonResponse.getJSONObject("return").getJSONArray(
					"eintraege");
			GBObject[] GBa = new GBObject[(GBlist.length())];

			if (GBlist.length() != 0) {
				for (int i = 0; i < GBlist.length(); i++) {
					JSONObject tempo = GBlist.getJSONObject(i);
					GBa[i] = new GBObject(tempo.getString("text_html"),
							tempo.getString("id"), tempo.getJSONObject("von")
									.getString("username"), tempo
									.getJSONObject("von").getString("id"),
							tempo.getString("datum_server"),
							tempo.getString("avatar"));
				}
			} else
				return new GBObject[] { new GBObject() };
			temp = GBa;
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GBObject[] { new GBObject() };
	}

}
