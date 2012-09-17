package de.meisterfuu.animexx.GB;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.ENS.ENSAnswer;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.profil.UserPopUp;

public class GBViewSingle extends SherlockActivity {

	TextView Absender;
	TextView Nachricht;
	Button Answer;
	WebView GB;
	String text;
	String id, username, userid;
	Context con;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.gb_single);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this);
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);

		con = this;

		Absender = (TextView) findViewById(R.id.TxVon);
		Nachricht = (TextView) findViewById(R.id.TxGB);
		GB = (WebView) findViewById(R.id.WebGB);
		Answer = (Button) findViewById(R.id.BtAnswer);

		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getString("id");
		username = bundle.getString("von");
		actionBar.setTitle(bundle.getString("an"));
		userid = bundle.getString("von_id");
		text = bundle.getString("text");

		Absender.setText("Von: " + username);
		Nachricht.setText("Nachricht:");
		GB.loadDataWithBaseURL("fake://fake.de", text, "text/html", "UTF-8", null);
		Answer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("ID", userid);
				bundle.putString("an", username);
				Intent newIntent = new Intent(con.getApplicationContext(), GBAnswer.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);
			}
		});
		Absender.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserPopUp Menu = new UserPopUp(con, username, userid);
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
			startActivity(new Intent().setClass(getApplicationContext(), ENSAnswer.class));
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
