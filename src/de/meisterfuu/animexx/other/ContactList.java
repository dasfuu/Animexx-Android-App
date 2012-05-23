package de.meisterfuu.animexx.other;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.profil.UserPopUp;

public class ContactList extends ListActivity {

	
	UserObject[] List;
	Context con;
	int action = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		
		con = this;
		
		if (this.getIntent().hasExtra("action")) {
			Bundle bundle = this.getIntent().getExtras();
			action = bundle.getInt("action");
		} 


			final ContactList tempAPP = this;
			final ProgressDialog dialog = ProgressDialog.show(tempAPP, "",
					Constants.LOADING, true);
			new Thread(new Runnable() {
				public void run() {
					final CTAdapter a;
					a = new CTAdapter(tempAPP, getContactlist());
					tempAPP.runOnUiThread(new Runnable() {
						public void run() {
							setlist(a);
							dialog.dismiss();
						}
					});
				}
			}).start();
		
	}

	private void setlist(CTAdapter a) {
		setListAdapter(a);
		
		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {
				
				if(action == 0){
					UserPopUp Menu = new UserPopUp(con, List[pos].getUsername(), List[pos].getId());
					Menu.PopUp();				
				}

			}
		});
	}

	private UserObject[] getContactlist() {
		try {
			JSONObject jsonResponse = new JSONObject(
			Request.makeSecuredReq("https://ws.animexx.de/json/kontakte/get_kontakte/?api=2"));
			JSONArray Userlist = jsonResponse.getJSONArray("return");
			UserObject[] temp = new UserObject[(Userlist.length())];

			if (Userlist.length() != 0) {
				for (int i = 0; i < Userlist.length(); i++) {
					
					JSONObject tp = Userlist.getJSONObject(i);			
					temp[i] = new UserObject();					
					temp[i].ParseJSON(tp);

				}
			} else {
				//Keine Kontakte :(
				return new UserObject[] {};
			}
			
			List = temp;
			return List;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new UserObject[] {};
	}

}
