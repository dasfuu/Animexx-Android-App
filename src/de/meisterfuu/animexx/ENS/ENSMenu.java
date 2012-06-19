package de.meisterfuu.animexx.ENS;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ENSMenu extends ListActivity {

	static String[] List = new String[] { "Posteingang", "Postausgang",
			"Neue ENS", "ENS Pushing..." };

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setlist();
		final ENSMenu temp = this;
		new Thread(new Runnable() {
			public void run() {
				if (Request.checkpush() == true)
					List[3] = "ENS Pushing On (Ausschalten)";
				else
					List[3] = "ENS Pushing Off (Einschalten)";
				temp.runOnUiThread(new Runnable() {
					public void run() {
						setlist();
					}
				});
			}
		}).start();
	}

	private void setlist() {
		setListAdapter(new ArrayAdapter<String>(this, R.layout.menu, List));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					startActivity(new Intent().setClass(
							getApplicationContext(), ENS.class));
					break;
				case 1:
					startActivity(new Intent().setClass(
							getApplicationContext(), ENSAusgang.class));
					break;
				case 2:
					startActivity(new Intent().setClass(
							getApplicationContext(), ENSAnswer.class));
					break;
				case 3:
					c2dm();
					break;
				case 4:
					startActivity(new Intent().setClass(
							getApplicationContext(), ENSSync.class));
					break;
				default:
					Request.doToast("Gibts noch nicht :P",
							getApplicationContext());
					break;
				}
			}
		});
	}

	private void c2dm() {
		final ENSMenu temp = this;
		new Thread(new Runnable() {
			public void run() {
				if (Request.checkpush() == true)
					List[3] = "ENS Pushing On (Ausschalten)";
				else
					List[3] = "ENS Pushing Off (Einschalten)";
				temp.runOnUiThread(new Runnable() {
					public void run() {
						if (List[3] == "ENS Pushing Off (Einschalten)") {
							Log.i("Check", "False");
							List[3] = "ENS Pushing On (Ausschalten)";
							Intent registrationIntent = new Intent(
									"com.google.android.c2dm.intent.REGISTER");
							registrationIntent.putExtra("app", PendingIntent
									.getBroadcast(temp, 0, new Intent(), 0));
							registrationIntent.putExtra("sender",
									"info@animexx.de");
							startService(registrationIntent);
						} else {
							Log.i("Check", "True");
							List[3] = "ENS Pushing Off (Einschalten)";
							Intent unregIntent = new Intent(
									"com.google.android.c2dm.intent.UNREGISTER");
							unregIntent.putExtra("app", PendingIntent
									.getBroadcast(temp, 0, new Intent(), 0));
							startService(unregIntent);
						}
						setlist();
					}
				});

			}
		}).start();

	}

	/*
	 * private void toggleENSPull(){ Editor edit = Request.config.edit();
	 * if(Request.config.getBoolean("ENSPULL", false) == true){
	 * edit.putBoolean("ENSPULL", false); //Request.doToast("ENS Pulling aus!",
	 * getApplicationContext()); if
	 * (Request.checkservice(getApplicationContext()) == true){ stopService(new
	 * Intent(this, ENSService.class)); } }else{ edit.putBoolean("ENSPULL",
	 * true); //Request.doToast("ENS Pulling an!", getApplicationContext());
	 * startService(new Intent(this, ENSService.class)); } edit.commit();
	 * setlist(); }
	 */

}
