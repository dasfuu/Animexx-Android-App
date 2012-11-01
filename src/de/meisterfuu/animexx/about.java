package de.meisterfuu.animexx;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.other.Feedback;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class about extends SherlockActivity {
	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.about);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("About");
		actionBar.setHomeButtonEnabled(true);

		WebView htm = (WebView) findViewById(R.id.webView1);
		Button bt = (Button) findViewById(R.id.btfeedback);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(), Feedback.class));
			}
		});
		htm.loadUrl("http://animexx.meister-fuu.de/about.htm");
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
