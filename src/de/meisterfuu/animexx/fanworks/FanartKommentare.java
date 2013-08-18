package de.meisterfuu.animexx.fanworks;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FanartKommentare extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fanart_kommentare);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fanart_kommentare, menu);
		return true;
	}

}
