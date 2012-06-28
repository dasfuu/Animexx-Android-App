package de.meisterfuu.animexx;

import de.meisterfuu.animexx.other.Feedback;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

public class about extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		WebView htm = (WebView) findViewById(R.id.webView1);
		Button bt = (Button) findViewById(R.id.btfeedback);
		bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(), Feedback.class));
			}
		});
		htm.loadUrl("http://animexx.meister-fuu.de/about.htm");
	}

}
