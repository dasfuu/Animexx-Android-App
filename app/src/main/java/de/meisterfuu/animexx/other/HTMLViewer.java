package de.meisterfuu.animexx.other;

import android.os.Bundle;
import android.webkit.WebView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;


public class HTMLViewer extends SherlockActivity {
	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.html_viewer);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		
		//Get Bundle Data
		Bundle bundle = this.getIntent().getExtras();
		String title = bundle.getString("title");	
		String html = bundle.getString("html");		
		
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle(title);
		actionBar.setHomeButtonEnabled(true);		
		
		WebView htm = (WebView) findViewById(R.id.webView1);
		htm.loadDataWithBaseURL("http://www.animexx.de", html, "text/html", "UTF-8", null);
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

