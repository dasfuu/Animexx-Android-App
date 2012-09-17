package de.meisterfuu.animexx.Home;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;

public class ContactsActivityList extends ListActivity implements UpDateUI {

	ArrayList<ContactActivityObject> Array = new ArrayList<ContactActivityObject>();
	Context con;
	ProgressDialog dialog;
	Boolean loading;
	ContactActivityAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		con = this;


		NotificationManager mManager;
		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(42);

		adapter = new ContactActivityAdapter(this, Array);
		refresh();
	}

	private void setlist(ContactActivityAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Array.get(position).getEventURL()));
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP	| Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
				startActivity(intent);
			}

		});
	}


	private ArrayList<ContactActivityObject> getActivitylist(String JSON) {

		try {
			JSONArray list = null;

			JSONObject jsonResponse = new JSONObject(JSON);
			list = jsonResponse.getJSONObject("return").getJSONArray("typ1");




			final ArrayList<ContactActivityObject> Array = new ArrayList<ContactActivityObject>();
			if ((list.length()) <= 0) {
				Request.doToast("Ordner leer!", getApplicationContext());
				return Array;
			}


			if (list.length() != 0) {
				for (int i = 0; i < list.length(); i++) {
					ContactActivityObject tempObject  = new ContactActivityObject();

					tempObject.setText(list.getJSONObject(i).getString("std_text"));
					tempObject.setBeschreibung(list.getJSONObject(i).getString("beschreibung"));
					tempObject.setEventID(list.getJSONObject(i).getString("event_id"));
					tempObject.setEventTyp(list.getJSONObject(i).getString("event_typ"));
					tempObject.setEventURL(list.getJSONObject(i).getString("link"));
					if(list.getJSONObject(i).has("img_url")) tempObject.setImgURL(list.getJSONObject(i).getString("img_url"));
					tempObject.setTime(list.getJSONObject(i).getString("datum"));
					tempObject.setVonID(list.getJSONObject(i).getString("event_von"));
					tempObject.setVonUsername(list.getJSONObject(i).getString("event_von_username"));

					Array.add(tempObject);
				}
			}

			this.Array = Array;
			return Array;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ArrayList<ContactActivityObject>();
	}


	public void UpDateUi(String[] s) {
		dialog.dismiss();
		setlist(new ContactActivityAdapter(this, getActivitylist(s[0])));
	}

	public void DoError() {
		dialog.dismiss();
		Request.doToast("Fehler", this);
	}

	public void refresh() {
		// TODO Auto-generated method stub
		dialog = ProgressDialog.show(this, "", Constants.LOADING, true);
		try {
			HttpGet[] HTTPs = new HttpGet[1];
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/persstart/get_widget_data/?api=2&widget_id=2_19&return_typ=app");
			new TaskRequest(this).execute(HTTPs);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onNewIntent(Intent intent){
		refresh();
	}

}
