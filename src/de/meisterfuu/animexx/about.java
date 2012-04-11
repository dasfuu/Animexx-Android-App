package de.meisterfuu.animexx;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class about extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView htm = (WebView) findViewById(R.id.webView1);
		htm.loadUrl("http://animexx.meister-fuu.de/about.htm");
	}

}
