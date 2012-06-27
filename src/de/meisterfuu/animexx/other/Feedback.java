package de.meisterfuu.animexx.other;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;

public class Feedback extends Activity {

	private Button Button;
	private TextView Text;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback);
		Text = (TextView) findViewById(R.id.edfeedback);
		Button = (Button) findViewById(R.id.btfeedback);
		final Activity con = this;
		Button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				try {
					String s = "("+Request.config.getString("id", "none")+") "+Request.config.getString("username", "none")+":";
					s += "\n\n";
					s += ""+Text.getText();
					//Request.ENSNotify("Feedback", s);
					Request.sendENS("App-Feedback", s, "Feedback "+Constants.VERSION, new int[]{586283}, -1);
					Log.i("Feedback", "Feedback gesendet");
					Request.doToast("Feedback gesendet!", con);
					con.finish();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	}
	
}
