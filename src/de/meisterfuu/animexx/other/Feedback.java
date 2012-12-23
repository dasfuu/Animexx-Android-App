package de.meisterfuu.animexx.other;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;

public class Feedback extends SherlockActivity {

	private Button Button;
	private TextView Text;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;
	Feedback temp = this;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.feedback);
		Text = (TextView) findViewById(R.id.edfeedback);
		Button = (Button) findViewById(R.id.btfeedback);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Feedback");
		actionBar.setHomeButtonEnabled(true);

		final Activity con = this;
		Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					String s = "("+Request.config.getString("id", "none")+") "+Request.config.getString("username", "none")+":";
					s += "\n\n";
					s += ""+Text.getText();
					//Request.ENSNotify("Feedback", s);
					final String ss = s;

					new Thread(new Runnable() {

						public void run() {
							try {
								Request.sendENS("App-Feedback", ss, "Feedback "+Constants.VERSION, new int[]{586283}, -1);
								Log.i("Feedback", "Feedback gesendet");
								temp.runOnUiThread(new Runnable() {

									public void run() {
										Request.doToast("Feedback gesendet!", con);
										con.finish();
									}
								});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).start();
					



				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
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
