package de.meisterfuu.animexx;

import oauth.signpost.OAuth;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnimexxActivity extends Activity {

	private Button AnmeldeButton;
	private SharedPreferences config;
	TextView console;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		console = (TextView) findViewById(R.id.TxCon);
		this.config = PreferenceManager.getDefaultSharedPreferences(this);
		Request.config = this.config;
		AnmeldeButton = (Button) findViewById(R.id.Anmelden);
		AnmeldeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(v.getContext(),
						RequestTokenActivity.class));
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isOAuthSuccessful()) { 
			console.setText("OAuth successful!");
			Log.i("OAuth", "OAuth nicht gescheitert");
			if (Request.config.getString("id", "none").equals("none"))
				try {
					Request.FetchMe();
				} catch (Exception e) {
				}
			if (Request.config.getString("c2dm", "null") == "null") {
				Intent registrationIntent = new Intent(
						"com.google.android.c2dm.intent.REGISTER");
				registrationIntent.putExtra("app",
						PendingIntent.getBroadcast(this, 0, new Intent(), 0));
				registrationIntent.putExtra("sender", "info@animexx.de");
				startService(registrationIntent);
			}

			startActivity(new Intent().setClass(getApplicationContext(),
					Menu.class));
		} else {
			Log.i("OAuth", "OAuth gescheitert");
			console.setText("Willkommen, du bist zur Zeit nicht angemeldet!");
		}
	}

	private boolean isOAuthSuccessful() {
		String token = config.getString(OAuth.OAUTH_TOKEN, null);
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, null);
		if (token != null && secret != null)
			return true;
		else
			return false;
	}

}