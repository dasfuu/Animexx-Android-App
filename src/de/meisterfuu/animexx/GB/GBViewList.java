package de.meisterfuu.animexx.GB;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.UserObject;

public class GBViewList extends ListActivity {

	String typ = "an";
	JSONArray GBlist;
	GBObject[] List;
	String id, username;
	Context con;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		NotificationManager mManager;
		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(43);
		
		con = this;

		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getString("id");
		} else {
			id = Request.config.getString("id", "none");
			username = "Du!";
		}

		if(!id.equals("none")){
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
	}

	private void setlist(GBAdapter a) {
		setListAdapter(a);
		
		ListView lv = getListView();

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {
					GBPopUp Menu = new GBPopUp(con, List[pos].getVon().getUsername(),
							List[pos].getVon().getId(), List[pos].getEntry_id(),
							List[pos].getEinleitung());
					Menu.PopUp();
				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				Bundle bundle = new Bundle();
				bundle.putString("von", List[pos].getVon().getUsername());
				bundle.putString("von_id", List[pos].getVon().getId());
				bundle.putString("id", List[pos].getEntry_id());
				bundle.putString("text", List[pos].getText());
				Intent newIntent = new Intent(con.getApplicationContext(),
					GBViewSingle.class);
				newIntent.putExtras(bundle);
				con.startActivity(newIntent);
			}
		});
	}

	private GBObject[] getGBlist() {
		try {
			JSONObject jsonResponse = new JSONObject(
			Request.makeSecuredReq("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id="+id+"&text_format=html&api=2"));
			GBlist = jsonResponse.getJSONObject("return").getJSONArray("eintraege");
			GBObject[] GBa = new GBObject[(GBlist.length())];

			if (GBlist.length() != 0) {
				for (int i = 0; i < GBlist.length(); i++) {
					JSONObject tp = GBlist.getJSONObject(i);	
					
					UserObject von = new UserObject();
					von.ParseJSON(tp.getJSONObject("von"));
					GBa[i].setVon(von);
					
					GBa[i].setText(tp.getString("text_html"));
					GBa[i].setEntry_id(tp.getString("id"));
					GBa[i].setTime(tp.getString("datum_server"));
					GBa[i].setAvatar(tp.getString("avatar"));
				}
			} else {
				//Keine Einträge im Gästebuch
				return new GBObject[] {};
			}
			
			List = GBa;
			return List;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new GBObject[] {};
	}

}
