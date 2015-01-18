package de.meisterfuu.animexx;

import com.google.android.gcm.GCMRegistrar;

import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.Home.HomeKontaktActivity;
import de.meisterfuu.animexx.RPG.RPGViewList;
import de.meisterfuu.animexx.other.ContactList;
import de.meisterfuu.animexx.overview.OverviewCardActivity;

import oauth.signpost.OAuth;
import android.app.Activity;
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
	boolean fetch = false;


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
				startActivity(new Intent().setClass(v.getContext(), RequestTokenActivity.class));
			}
		});

	}


	@Override
	protected void onResume() {
		super.onResume();

		if (isOAuthSuccessful()) {

			console.setText("OAuth successful!");
			Log.i("OAuth", "OAuth successful");
			final AnimexxActivity temp = this;
			if (Request.config.getString("id", "none").equals("none")) {
				new Thread(new Runnable() {

					public void run() {
						try {
							fetch = true;
							Request.FetchMe();
						} catch (Exception e) {
							e.printStackTrace();
						}
						temp.runOnUiThread(new Runnable() {

							public void run() {
								temp.finish();
							}
						});
					}
				}).start();
			}
            if ((Request.config.getBoolean("gcm_repair", true))) {
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                Request.setC2DM();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            temp.runOnUiThread(new Runnable() {
                                public void run() {
                                    Request.config.edit().putBoolean("gcm_repair", false).commit();
                                }
                            });
                        }
                    }).start();
             }
			if ((Request.config.getBoolean("push", true))) {
				GCMRegistrar.checkDevice(this);
				GCMRegistrar.checkManifest(this);
				final String regId = GCMRegistrar.getRegistrationId(this);
				if (regId.equals("")) {
					Log.i("GCM", "Register");
					GCMRegistrar.register(getApplicationContext(), KEYS.GCM_SENDER_ID);
				} else {
					Log.v("GCM", "Already registered");
				}
			} else {
				GCMRegistrar.checkDevice(this);
				GCMRegistrar.checkManifest(this);
				final String regId = GCMRegistrar.getRegistrationId(this);
				if (!regId.equals("")) {
					Log.i("GCM", "Unregister");
					Intent UnRegisterIntent = new Intent();
					UnRegisterIntent.setAction("com.google.android.c2dm.intent.UNREGISTER");
					this.sendBroadcast(UnRegisterIntent);
				}
			}

			String startscreen = Request.config.getString("start_activity", "Home");
			if (startscreen.equals("Feed")) {
				startActivity(new Intent().setClass(getApplicationContext(), HomeKontaktActivity.class));
			} else if (startscreen.equals("RPG")) {
				startActivity(new Intent().setClass(getApplicationContext(), RPGViewList.class));
			} else if (startscreen.equals("Kontakte")) {
				startActivity(new Intent().setClass(getApplicationContext(), ContactList.class));
			} else if (startscreen.equals("ENS")) {
				startActivity(new Intent().setClass(getApplicationContext(), ENSActivity.class));
			} else if (startscreen.equals("Home")) {
				startActivity(new Intent().setClass(getApplicationContext(), OverviewCardActivity.class));
			} else {
				startActivity(new Intent().setClass(getApplicationContext(), OverviewCardActivity.class));
			}
			
			if (!fetch) finish();
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