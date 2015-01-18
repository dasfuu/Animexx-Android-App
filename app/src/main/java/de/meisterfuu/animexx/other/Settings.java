package de.meisterfuu.animexx.other;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gcm.GCMRegistrar;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.KEYS;
import de.meisterfuu.animexx.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.util.Log;

@SuppressWarnings("deprecation")
public class Settings extends SherlockPreferenceActivity {

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		addPreferencesFromResource(R.xml.settings);
		setContentView(R.layout.settings);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("About");
		actionBar.setHomeButtonEnabled(true);

		final Settings temp = this;
		Preference pref = findPreference("push");
		pref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference, Object newValue) {
				if (newValue.toString().equals("false")) {
					Log.i("GCM", "Unregister");
					Intent UnRegisterIntent = new Intent();
					UnRegisterIntent.setAction("com.google.android.c2dm.intent.UNREGISTER");
					temp.sendBroadcast(UnRegisterIntent);

				} else {
					Log.i("GCM", "Register");
					GCMRegistrar.register(temp, KEYS.GCM_SENDER_ID);
				}
				return true;
			}

		});

		// oAuthLogOut

		final SharedPreferences config = PreferenceManager.getDefaultSharedPreferences(this);
		Preference logout = findPreference("oAuthLogOut");
		logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {
				final Editor edit = config.edit();
				edit.clear();
				edit.commit();
				Log.i("Push Logout", ""+config.getBoolean("push", false));
				Intent UnRegisterIntent = new Intent();
				UnRegisterIntent.setAction("com.google.android.c2dm.intent.UNREGISTER");
				temp.sendBroadcast(UnRegisterIntent);
				Helper.isLoggedIn(temp);
				return true;
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
