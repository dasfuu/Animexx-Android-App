package de.meisterfuu.animexx;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;


public class Debug extends Activity {

	private Button Button;
	private WebView Web;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug);
		Web = (WebView) findViewById(R.id.webView1);
		Button = (Button) findViewById(R.id.fragment_microblog_send);
		Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					String s = Request.makeSecuredReq("https://ws.animexx.de/json/persstart/get_widget_data/?api=2&widget_id=2_19&return_typ=app&zeit_von=1334595292");
					Web.loadDataWithBaseURL("fake://fake.de", s, "text/html", "UTF-8", null);
					Log.i("Debug Request", s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	}
	
}
