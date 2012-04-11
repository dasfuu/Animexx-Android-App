package de.meisterfuu.animexx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class ENSNotify extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Request.config = PreferenceManager.getDefaultSharedPreferences(context);
		int[] an = new int[1];
		an[0] = Integer.parseInt(Request.config.getString("id", "-1"));
		Log.i("Animexx", "ENSNotify");
		try {
			Request.sendENS(intent.getStringExtra("betreff"),
					intent.getStringExtra("text"), "Android Notify", an, "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
