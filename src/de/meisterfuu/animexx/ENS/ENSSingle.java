package de.meisterfuu.animexx.ENS;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.UserObject;
import de.meisterfuu.animexx.profil.UserPopUp;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class ENSSingle extends Activity {

	TextView Betreff;
	TextView Absender;
	TextView Nachricht;
	Button Answer;
	WebView ENS;
	ENSObject msg;
	Long id2;
	Context con;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enssingle);

		con = this;

		Betreff = (TextView) findViewById(R.id.TxBetreff);
		Absender = (TextView) findViewById(R.id.TxVon);
		Nachricht = (TextView) findViewById(R.id.TxENS);
		ENS = (WebView) findViewById(R.id.WebENS);
		Answer = (Button) findViewById(R.id.BtAnswer);

		Bundle bundle = this.getIntent().getExtras();
		id2 = bundle.getLong("id");

		
			ENSsql SQL = new ENSsql(this);
			SQL.open();
			msg = SQL.getSingleENS(id2);
			SQL.close();
			updateUI();
			if(msg != null && msg.getText() != "" && msg.getText() != null){
				return;
			}
			
			

		if (id2 != -1) {
			final ENSSingle temp = this;
			final ProgressDialog dialog = ProgressDialog.show(temp, "",
					"Loading. Please wait...", true);
			new Thread(new Runnable() {
				public void run() {
					try {
						msg = Request.GetENS(id2);
						saveENS();
					} catch (Exception e) {
						msg = new ENSObject();
						msg.setText("Fehler beim abrufen!");
						UserObject o = new UserObject();
						o.setUsername("");
						o.setId("0");
						o.setSteckbriefFreigabe(false);
						msg.setVon(o);
						msg.setBetreff("Ups :/");
					}
					temp.runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							updateUI();
						}

					});
				}
			}).start();
		}

	}
	

	private void saveENS() {
		ENSsql SQL = new ENSsql(this);
		SQL.open();
		SQL.updateENS(msg,true);
		SQL.close();	
	}
	
	public void updateUI() {

		Betreff.setText(msg.getBetreff());
		Absender.setText("Von: " + msg.getVon().getUsername());
		Nachricht.setText("Nachricht:");
		ENS.loadDataWithBaseURL("fake://fake.de", msg.getText(), "text/html", "UTF-8",
				null);
		Answer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putLong("relativ", id2);
				bundle2.putString("betreff", msg.getBetreff());
				bundle2.putString("an", msg.getVon().getUsername());
				Intent newIntent = new Intent(getApplicationContext(),
						ENSAnswer.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
			}
		});
		Absender.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserPopUp Menu = new UserPopUp(con, msg.getVon());
				Menu.PopUp();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
