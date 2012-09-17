package de.meisterfuu.animexx.other;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.R;
import android.os.Bundle;
import android.graphics.Color;

public class SingleImage extends SherlockActivity {

	TouchImageView Image;
	String URL;
	ImageDownloader ImageLoader = new ImageDownloader();

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_single);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this);
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Bild");
		actionBar.setHomeButtonEnabled(true);

		Image = new TouchImageView(this);

		if (this.getIntent().hasExtra("URL")) {
			Bundle bundle = this.getIntent().getExtras();
			URL = bundle.getString("URL");
		} else
			this.finish();

		ImageLoader.download(URL, Image, false);
		setContentView(Image);
		Image.setBackgroundColor(Color.BLACK);

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
