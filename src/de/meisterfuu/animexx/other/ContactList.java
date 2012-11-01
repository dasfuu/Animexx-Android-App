package de.meisterfuu.animexx.other;

import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.profil.UserPopUp;

public class ContactList extends SherlockListActivity {

	ArrayList<UserObject> List;
	Context con;
	int action = 0;
	private EditText filterText = null;
	CTAdapter adapter;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.contact_list_header);
		filterText = (EditText) findViewById(R.id.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());;
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Kontakte");
		actionBar.setHomeButtonEnabled(true);

		con = this;

		if (this.getIntent().hasExtra("action")) {
			Bundle bundle = this.getIntent().getExtras();
			action = bundle.getInt("action");
		}

		final ContactList tempAPP = this;
		final ProgressDialog dialog = ProgressDialog.show(tempAPP, "", Constants.LOADING, true);
		new Thread(new Runnable() {

			public void run() {

				adapter = new CTAdapter(tempAPP, getContactlist());
				tempAPP.runOnUiThread(new Runnable() {

					public void run() {
						setlist(adapter);
						dialog.dismiss();
					}
				});
			}
		}).start();

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


	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}


		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}


		public void onTextChanged(CharSequence s, int start, int before, int count) {
			adapter.filterUser(s);
		}

	};


	private void setlist(final CTAdapter a) {
		setListAdapter(a);

		ListView lv = getListView();

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				if (action == 0) {
					UserPopUp Menu = new UserPopUp(con, a.names.get(pos).getUsername(), a.names.get(pos).getId());
					Menu.PopUp();
				} else if (action == 1) {
					Intent returnIntent = new Intent();
					returnIntent.putExtra("username", a.names.get(pos).getUsername());
					returnIntent.putExtra("id", a.names.get(pos).getId());
					setResult(RESULT_OK, returnIntent);
					finish();
				}

			}
		});
	}


	private ArrayList<UserObject> getContactlist() {
		try {
			JSONObject jsonResponse = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/kontakte/get_kontakte/?api=2"));
			JSONArray Userlist = jsonResponse.getJSONArray("return");
			ArrayList<UserObject> temp = new ArrayList<UserObject>(Userlist.length());

			if (Userlist.length() != 0) {
				for (int i = 0; i < Userlist.length(); i++) {
					JSONObject tp = Userlist.getJSONObject(i);
					UserObject tempu = new UserObject();
					tempu.ParseJSON(tp);
					temp.add(tempu);
				}
			} else {
				// Keine Kontakte :(
				return temp;
			}

			Collections.sort(temp);
			List = temp;
			return List;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
