package de.meisterfuu.animexx.ENS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.booleanobject;

public class ENSAnswer extends Activity {

	TextView Betreff;
	TextView An;
	TextView Nachricht;
	Button Send;
	String xBetreff = "";
	String xAn = "";
	String xRelativ = "-1";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ensanswer);

		Betreff = (TextView) findViewById(R.id.edBetreff);
		An = (TextView) findViewById(R.id.edAn);
		Nachricht = (TextView) findViewById(R.id.edENS);
		Send = (Button) findViewById(R.id.BtSend);

		if (this.getIntent().hasExtra("betreff")) {
			xBetreff = this.getIntent().getStringExtra("betreff");
		}

		if (this.getIntent().hasExtra("an")) {
			xAn = this.getIntent().getStringExtra("an");
		}
		
		if (this.getIntent().hasExtra("relativ")) {
			xRelativ = this.getIntent().getStringExtra("relativ");
		}
		
		if (this.getIntent().hasExtra("msg")) {
			Nachricht.setText(this.getIntent().getStringExtra("msg"));
		}

		Betreff.setText(xBetreff);
		An.setText(xAn);

		Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				send();
			}
		});

	}

	public void onBackPressed() {
		finish();
		return;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void send() {

		final String betreff2 = "" + Betreff.getText();
		final String an2 = "" + An.getText();
		final String msg2 = "" + Nachricht.getText();
		final String relativ = xRelativ;
		final ENSAnswer temp = this;
		final ProgressDialog dialog = ProgressDialog.show(temp, "",
				"Sende ENS...", true);
		new Thread(new Runnable() {
			public void run() {
				final booleanobject x = new booleanobject();
				try {
					int[] s = Request.GetUser(new String[] { an2 });
					x.bool = Request.sendENS(betreff2, msg2, "", s, relativ);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					x.bool = false;
					e.printStackTrace();
				}

				temp.runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						if (!x.bool) {
							Request.doToast("Senden gescheitert!",
									getApplicationContext());
						} else {
							finish();
						}

					}
				});
			}
		}).start();
	}

}
