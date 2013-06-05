package de.meisterfuu.animexx;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.HomeKontaktActivity;
import de.meisterfuu.animexx.RPG.RPGViewList;
import de.meisterfuu.animexx.other.ContactList;
import de.meisterfuu.animexx.other.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Menu extends Activity {

	ImageButton ENS, Guestbook, Contacts, About, Home, Settings, RPG;
	ImageView imgCoin, imgPush, imgunreadENS, imglogo;
	RelativeLayout BlauerBalken;
	TextView bbMessage;
	boolean taler, push, unread;
	long time = 0;
	int neu;
	final Menu temp = this;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main_menu);

		BlauerBalken = (RelativeLayout) findViewById(R.id.BlauerBalken);
		bbMessage = (TextView) findViewById(R.id.bbMessage);
		BlauerBalken.setVisibility(View.GONE);

		ENS = (ImageButton) findViewById(R.id.btens);
		Guestbook = (ImageButton) findViewById(R.id.btguestbook);
		Contacts = (ImageButton) findViewById(R.id.btcontacts);
		About = (ImageButton) findViewById(R.id.btabout);
		Home = (ImageButton) findViewById(R.id.bthome);
		Settings = (ImageButton) findViewById(R.id.btsettings);
		RPG = (ImageButton) findViewById(R.id.btrpg);

		imgCoin = (ImageView) findViewById(R.id.img_coin);
		imgPush = (ImageView) findViewById(R.id.img_push);
		imgunreadENS = (ImageView) findViewById(R.id.img_unreadENS);
		imglogo = (ImageView) findViewById(R.id.logo);

		setClick(ENS, ENSActivity.class);
		setClick(Guestbook, GBViewList.class);
		setClick(Contacts, ContactList.class);
		setClick(About, about.class);
		// setClick(About, Debug.class);
		// setClick(Home, ContactsActivityList.class);
		setClick(Home, HomeKontaktActivity.class);
		setClick(Settings, Settings.class);
		setClick(RPG, RPGViewList.class);

		imgCoin.setOnClickListener(Karotaler);

		imgunreadENS.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				startActivity(new Intent().setClass(getApplicationContext(), ENSActivity.class));

			}
		});

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
	}


	private void setClick(ImageButton b, final Class<?> c) {
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(), c));
			}
		});
	}

	OnClickListener Karotaler = new OnClickListener() {

		public void onClick(View v) {

			new Thread(new Runnable() {

				public void run() {

					try {
						if (taler) {
							JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_abholen/?api=2"));
							neu = a.getInt("return");
						}
						temp.taler = false;
					} catch (Exception e) {
						e.printStackTrace();
					}

					temp.runOnUiThread(new Runnable() {

						public void run() {
							temp.time = 0;
							AlertDialog alertDialog = new AlertDialog.Builder(temp).create();
							alertDialog.setTitle("Karotaler abgeholt!");
							alertDialog.setMessage("Du hast jetzt " + neu + " Karotaler.");
							alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog, int which) {
									//
								}
							});
							alertDialog.show();
							setIcons();
						}
					});

				}
			}).start();

		}
	};


	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		finish();
		return;
	}


	@Override
	public void onResume() {
		super.onResume();

		final Menu temp = this;

		long i = System.currentTimeMillis() - time;
		Log.i("Animexx", "last check " + i + "ms ago");
		if ((i > 30000L) || time == 0) {
			new Thread(new Runnable() {

				public void run() {

					temp.push = Request.checkpush(temp);
					temp.runOnUiThread(new Runnable() {

						public void run() {
							setIcons();
						}
					});

					temp.unread = (Request.GetUnread() > 0);
					temp.runOnUiThread(new Runnable() {

						public void run() {
							setIcons();
						}
					});

					temp.taler = KarotalerCheck();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							setIcons();
						}
					});

				}
			}).start();

			time = System.currentTimeMillis();
		}
	}


	private boolean KarotalerCheck() {
		try {
			int abholbar = 0;
			JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_statistik/?api=2"));
			JSONArray ab = a.getJSONObject("return").getJSONArray("kt_abholbar");
			if (ab.length() > 0) {
				abholbar = ab.getJSONObject(0).getInt("kt");
			} else {
				abholbar = 0;
			}

			return (abholbar > 0);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}


	private void setIcons() {
		if (taler) {
			imgCoin.setVisibility(View.VISIBLE);
			// imglogo.setColorFilter(Color.YELLOW);
			bbMessage.setText("Karotaler abholen");
			BlauerBalken.setVisibility(View.VISIBLE);
			BlauerBalken.setOnClickListener(Karotaler);
			bbMessage.setOnClickListener(Karotaler);
		} else {
			imgCoin.setVisibility(View.GONE);
			BlauerBalken.setVisibility(View.GONE);
			BlauerBalken.setOnClickListener(null);
			bbMessage.setOnClickListener(null);
			// imglogo.setColorFilter(Color.TRANSPARENT);
		}

		if (push) {
			imgPush.setVisibility(View.VISIBLE);
		} else {
			imgPush.setVisibility(View.GONE);

			if ((Request.config.getBoolean("push", true))) {

				GCMRegistrar.checkDevice(this);
				GCMRegistrar.checkManifest(this);
				final String regId = GCMRegistrar.getRegistrationId(this);
				if (regId.equals("")) {
					Log.i("GCM", "Register");
					GCMRegistrar.register(this, KEYS.GCM_SENDER_ID);
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

		}

		if (unread) {
			imgunreadENS.setVisibility(View.VISIBLE);
		} else {
			imgunreadENS.setVisibility(View.GONE);
		}

	}

}
