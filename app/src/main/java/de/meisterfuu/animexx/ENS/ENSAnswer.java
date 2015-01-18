package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;
import java.util.Arrays;


import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.*;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.booleanobject;
import de.meisterfuu.animexx.other.ContactList;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.other.UserObject;

public class ENSAnswer extends SherlockActivity   {

	TextView Betreff;
	TextView An;
	TextView Nachricht;
	Button Send, Add;
	String xBetreff = "";
	ArrayList<UserObject> xAnUser = new ArrayList<UserObject>();
	long xRelativ = -1;
	final ENSAnswer temp = this;
	boolean error = false;
	boolean fetched = false;
	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.ensanswer);


		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Neue ENS");
		actionBar.setHomeButtonEnabled(true);


		Betreff = (TextView) findViewById(R.id.edBetreff);
		Nachricht = (TextView) findViewById(R.id.edENS);
		Send = (Button) findViewById(R.id.BtSend);
		Add = (Button) findViewById(R.id.add);
		An = (TextView) findViewById(R.id.edAn);

		if (this.getIntent().hasExtra("betreff")) {
			xBetreff = this.getIntent().getStringExtra("betreff");
		}

		if (this.getIntent().hasExtra("an")) {
			if (this.getIntent().hasExtra("anid")) {
				UserObject tUserObject = new UserObject();
				tUserObject.setId(this.getIntent().getStringExtra("anid"));
				tUserObject.setUsername(this.getIntent().getStringExtra("an"));
				xAnUser.add(tUserObject);
				An.setText(ShowUsername());
				fetched = true;
			} else {
				if (An.getText().equals(""))
					An.setText(An.getText() + ", " + this.getIntent().getStringExtra("an"));
				else
					An.setText(this.getIntent().getStringExtra("an"));
			}
		}

		if (this.getIntent().hasExtra("relativ")) {
			xRelativ = this.getIntent().getLongExtra("relativ", -1);
		}

		if (xRelativ != -1) xBetreff = Helper.BetreffRe(xBetreff);

		if (this.getIntent().hasExtra("msg")) {
			Nachricht.setText(this.getIntent().getStringExtra("msg"));
		}

		Betreff.setText(xBetreff);
		// An.setText(ShowUsername());
		if (!fetched) UpdateUser("" + An.getText(), false);

		Send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (!error) {
					if (fetched) {
						send();
					} else {
						UpdateUser("" + An.getText(), true);
					}
				}
			}
		});

		Add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent AddIntent = new Intent();
				AddIntent.setClass(temp, ContactList.class);
				AddIntent.putExtra("action", 1);
			     startActivityForResult(AddIntent, 0);
			}
		});

		An.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
				// useless

			}


			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// useless

			}


			public void onTextChanged(CharSequence s, int start, int before, int count) {
				error = false;
				fetched = false;

			}

		});

	}


	@Override
	public void onBackPressed() {
		finish();
		return;
	}


	@Override
	protected void onResume() {
		super.onResume();

	}


	private void send() {

		final String betreff2 = "" + Betreff.getText();
		final String msg2 = "" + Nachricht.getText();

		final int[] AnIDs = new int[xAnUser.size()];
		for (int i = 0; i < xAnUser.size(); i++) {
			AnIDs[i] = Integer.parseInt(xAnUser.get(i).getId());
		}

		final long relativ = xRelativ;
		final ENSAnswer temp = this;
		final ProgressDialog dialog = ProgressDialog.show(temp, "", "Sende ENS...", true);
		new Thread(new Runnable() {

			public void run() {
				final booleanobject x = new booleanobject();
				try {
					x.bool = Request.sendENS(betreff2, msg2, "Sent from Android", AnIDs, relativ);
				} catch (Exception e) {
					x.bool = false;
					e.printStackTrace();
				}

				temp.runOnUiThread(new Runnable() {

					public void run() {
						dialog.dismiss();
						if (!x.bool) {
							Request.doToast("Senden gescheitert!", getApplicationContext());
						} else {
							finish();
						}

					}
				});
			}
		}).start();
	}


	public String ShowUsername() {
		Joiner joiner = Joiner.on(", ").skipNulls();
		return joiner.join(xAnUser);
	}


	public void UpdateUser(final String sAn, final boolean SendAfter) {
		final ProgressDialog load = ProgressDialog.show(temp, "", "Prüfe Empfünger...", true);
		if (!SendAfter) {
			load.dismiss();
		}

		Iterable<String> user = Splitter.on(CharMatcher.anyOf(";,")).trimResults().omitEmptyStrings().split(sAn);
		final ArrayList<String> tAn = new ArrayList<String>();
		for (String element : user) {
			tAn.add(element);
		}

		Log.i("Splitter", tAn.toString());


		int count = 0;
		for (int i = 0; i < xAnUser.size(); i++) {
			for (int j = 0; j < tAn.size(); j++) {
				if (xAnUser.get(i).getUsername().equalsIgnoreCase(tAn.get(j))) {
					count++;
				}
			}
		}

		if (count == tAn.size()) {
			fetched = true;
			if (!temp.An.getText().equals(ShowUsername()))
				temp.An.setText(ShowUsername());

			if (SendAfter){
				load.dismiss();
				send();
			}
				return;
		}

		fetched = false;

		new Thread(new Runnable() {

			public void run() {


				try {
					int[] userIDs = Request.GetUser(tAn);
					Log.i("GetUser", Arrays.toString(userIDs));

					ArrayList<UserObject> tAnUser = new ArrayList<UserObject>();
					for (int i = 0; i < userIDs.length; i++) {
						UserObject tUserObject = new UserObject();
						tUserObject.setId("" + userIDs[i]);
						tUserObject.setUsername(tAn.get(i));
						tAnUser.add(tUserObject);
					}
					xAnUser = tAnUser;
					Log.i("Join", xAnUser.toString());

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (!temp.An.getText().equals(ShowUsername())) temp.An.setText(ShowUsername());
							temp.error = false;
							temp.fetched = true;
							if (SendAfter) {
								load.dismiss();
							}
							if (SendAfter) send();
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							Request.doToast("Fehler!", temp);
							if (SendAfter) {
								load.dismiss();
							}
							temp.error = true;
						}
					});
					return;
				}

			}
		}).start();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
     {

           if (resultCode == RESULT_OK) {
      		if (data.hasExtra("username")) {
     			if (data.hasExtra("id")) {
     				UserObject tUserObject = new UserObject();
     				tUserObject.setId(data.getStringExtra("id"));
     				tUserObject.setUsername(data.getStringExtra("username"));
     				xAnUser.add(tUserObject);
     				An.setText(An.getText() + ", " + data.getStringExtra("username"));
     				return;
     			}
     		}
           }
           Request.doToast("Fehler", temp);

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

}
