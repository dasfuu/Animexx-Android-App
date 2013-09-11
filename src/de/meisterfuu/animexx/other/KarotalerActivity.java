package de.meisterfuu.animexx.other;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class KarotalerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_karotaler);
		
		new Thread(new Runnable() {

			public void run() {

					try {
						Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_abholen/?api=2");
						KarotalerActivity.this.runOnUiThread(new Runnable() {
				                public void run() {
				               	 KarotalerActivity.this.finish();
				                 }
				             });
					} catch (Exception e) {
						e.printStackTrace();
						KarotalerActivity.this.runOnUiThread(new Runnable() {
				                public void run() {
				               	 KarotalerActivity.this.finish();
				                 }
				             });
					}
			}
		}).start();
	}



}
