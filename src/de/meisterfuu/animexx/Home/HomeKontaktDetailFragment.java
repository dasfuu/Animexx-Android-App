package de.meisterfuu.animexx.Home;

import de.meisterfuu.animexx.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HomeKontaktDetailFragment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_kontakt_detail_fragment);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_kontakt_detail, menu);
		return true;
	}

}
