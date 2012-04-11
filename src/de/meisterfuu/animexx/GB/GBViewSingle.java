package de.meisterfuu.animexx.GB;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.profil.UserPopUp;


public class GBViewSingle extends Activity {

	TextView Absender;
	TextView Nachricht;
	Button Answer;
	WebView GB;
	String text;
	String id, username, userid;
	Context con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gb_single);

		con = this;

		Absender = (TextView) findViewById(R.id.TxVon);
		Nachricht = (TextView) findViewById(R.id.TxGB);
		GB = (WebView) findViewById(R.id.WebGB);
		Answer = (Button) findViewById(R.id.BtAnswer);

		Bundle bundle = this.getIntent().getExtras();
		id = bundle.getString("id");
		username = bundle.getString("von");
		userid = bundle.getString("von_id");
		text = bundle.getString("text");

		
		Absender.setText("Von: " + username);
		Nachricht.setText("Nachricht:");
		GB.loadDataWithBaseURL("fake://fake.de", text, "text/html", "UTF-8",
				null);
		Answer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putString("ID", userid);
				bundle.putString("an", username);	
				Intent newIntent = new Intent(con.getApplicationContext(),
						GBAnswer.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);
			}
		});
		Absender.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserPopUp Menu = new UserPopUp(con, username, userid);
				Menu.PopUp();
			}
		});


	}



	@Override
	protected void onResume() {
		super.onResume();
	}

}
