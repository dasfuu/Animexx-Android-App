package de.meisterfuu.animexx.ENS;

import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.common.base.Joiner;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.other.UserObject;
import de.meisterfuu.animexx.profil.UserPopUp;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class ENSSingle extends SherlockActivity {

	TextView Betreff;
	TextView Absender;
	TextView Nachricht;
	CheckBox Zitat;
	Button Answer;
	WebView ENS;
	ENSObject msg;
	Long id2;
	Context con;
	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.enssingle);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("ENS");
		actionBar.setHomeButtonEnabled(true);

		con = this;

		Betreff = (TextView) findViewById(R.id.TxBetreff);
		Absender = (TextView) findViewById(R.id.TxVon);
		Nachricht = (TextView) findViewById(R.id.TxENS);
		ENS = (WebView) findViewById(R.id.WebENS);
		Zitat = (CheckBox) findViewById(R.id.zitat);
		Answer = (Button) findViewById(R.id.BtAnswer);

		Uri data = getIntent().getData();
		List<String> params = null;
		try {
			params = data.getPathSegments();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (params != null && params.size() > 0) {
			id2 = Long.parseLong(params.get(0));
		} else if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id2 = bundle.getLong("id");
		} else {
			Request.doToast("Error", con);
			finish();
			return;
		}

		ENSsql SQL = new ENSsql(this);
		SQL.open();
		msg = SQL.getSingleENS(id2);
		SQL.close();
		if (msg != null && msg.getText() != "" && msg.getText() != null) {
			updateUI();
			return;
		}

		if (id2 != -1) {
			final ENSSingle temp = this;
			final ProgressDialog dialog = ProgressDialog.show(temp, "", "Loading. Please wait...", true);
			new Thread(new Runnable() {

				public void run() {
					try {
						msg = Request.GetENS(id2);
						try{
							saveENS();
						} catch (Exception e){
							e.printStackTrace();
						}
					} catch (Exception e) {
						msg = new ENSObject();
						msg.setText("Fehler beim abrufen!");
						UserObject o = new UserObject();
						o.setUsername("");
						o.setId("0");
						o.setSteckbriefFreigabe(false);
						msg.setVon(o);
						msg.setBetreff("Ups :/");
					}
					temp.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							updateUI();
						}

					});
				}
			}).start();
		}

	}


	private void saveENS() {
		ENSsql SQL = new ENSsql(this);
		SQL.open();
		SQL.updateENS(msg, true);
		SQL.close();
	}


	public void updateUI() {

		Betreff.setText(msg.getBetreff());

		if (msg.getAnVon() == "an") {
			Absender.setText("Von: " + msg.getVon().getUsername());
		} else {
			Joiner joiner = Joiner.on(", ").skipNulls();
			String an = joiner.join(msg.getAnArray());
			if (an.length() > 30) an = an.substring(0, 30) + "...";
			Absender.setText("An: " + an);
		}

		Nachricht.setText("Nachricht:");
		ENS.loadDataWithBaseURL("fake://fake.de", msg.getText(), "text/html", "UTF-8", null);
		Answer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putLong("relativ", id2);
				bundle2.putString("betreff", msg.getBetreff());
				bundle2.putString("an", msg.getVon().getUsername());
				bundle2.putString("anid", msg.getVon().getId());
				if (Zitat.isChecked()) {
					String s = ">" + Html.fromHtml(msg.getText()).toString();
					s = s.replaceAll("(\r\n|\n)", "\n>");
					if (s.charAt(s.length() - 1) == '>') s = "" + s.subSequence(0, s.length() - 1);
					bundle2.putString("msg", s);
				}
				Intent newIntent = new Intent(getApplicationContext(), ENSAnswer.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
			}
		});
		Absender.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserPopUp Menu = new UserPopUp(con, msg.getVon());
				Menu.PopUp();
			}
		});
	}


	@Override
	protected void onResume() {
		super.onResume();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.ac_answer:
			Bundle bundle2 = new Bundle();
			bundle2.putLong("relativ", id2);
			bundle2.putString("betreff", msg.getBetreff());
			bundle2.putString("an", msg.getVon().getUsername());
			bundle2.putString("anid", msg.getVon().getId());
			if (Zitat.isChecked()) {
				String s = ">" + Html.fromHtml(msg.getText()).toString();
				s = s.replaceAll("(\r\n|\n)", "\n>");
				if (s.charAt(s.length() - 1) == '>') s = "" + s.subSequence(0, s.length() - 1);
				bundle2.putString("msg", s);
			}
			Intent newIntent = new Intent(getApplicationContext(), ENSAnswer.class);
			newIntent.putExtras(bundle2);
			startActivity(newIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_answer, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
