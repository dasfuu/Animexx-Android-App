package de.meisterfuu.animexx.profil;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.ENS.ENSAnswer;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.SingleImage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserSteckbrief extends Activity implements UpDateUI {

	String userid;
	String username;

	TextView txtusername, txtbirth, txtmail, txtweb, txtort, txtalter, txtname;
	Button ENS, GB;
	JSONObject data;
	ImageView Avatar;
	ImageDownloader ImageLoader = new ImageDownloader();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this); 
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.steckbrief_main);
		txtusername = (TextView) findViewById(R.id.username);
		txtname = (TextView) findViewById(R.id.txRealName);
		txtbirth = (TextView) findViewById(R.id.txBirthday);
		txtmail = (TextView) findViewById(R.id.txEMail);
		txtweb = (TextView) findViewById(R.id.txWeb);
		txtort = (TextView) findViewById(R.id.txOrt);
		txtalter = (TextView) findViewById(R.id.txAlter);
		ENS = (Button) findViewById(R.id.btENS);
		GB = (Button) findViewById(R.id.btGB);
		Avatar = (ImageView) findViewById(R.id.loadAvatar);

		if (this.getIntent().hasExtra("id")) {
			userid = this.getIntent().getStringExtra("id");
		}

		LoadData();
	}

	private void LoadData() {
		HttpGet[] HTTPs = new HttpGet[1];
		try {
			HTTPs[0] = Request
					.getHTTP("https://ws.animexx.de/json/mitglieder/steckbrief/?user_id="
							+ userid + "&api=2");
			new TaskRequest(this).execute(HTTPs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void UpDateUi(String[] s) {
		// TODO Auto-generated method stub

		try {
			data = new JSONObject(s[0]);
			data = data.getJSONObject("return");

			username = data.getString("username");
			txtusername.setText(username);
			String vor = "";

			if (data.getBoolean("freigabe_steckbrief")) {
				if (!data.isNull("foto_url"))
					ImageLoader.download(data.getString("foto_url"), Avatar);
				if (!data.isNull("vorname"))
					vor = data.getString("vorname");
				if (!data.isNull("nachname"))
					vor += " " + data.getString("nachname");
				txtname.setText(vor);
				if (!data.isNull("geburtstag"))
					txtbirth.setText(data.getString("geburtstag"));
				if (!data.isNull("email"))
					txtmail.setText(data.getString("email"));
				if (!data.isNull("homepage"))
					txtweb.setText(data.getString("homepage"));
				if (!data.isNull("ort"))
					txtort.setText(data.getString("ort"));
				if (!data.isNull("alter"))
					txtalter.setText(data.getString("alter"));
			}

			ENS.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Bundle bundle2 = new Bundle();
					bundle2.putString("betreff", "");
					bundle2.putString("an", username);
					bundle2.putString("anid", userid);
					Intent newIntent = new Intent(getApplicationContext(),
							ENSAnswer.class);
					newIntent.putExtras(bundle2);
					startActivity(newIntent);

				}
			});

			GB.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("id", userid);
					Intent newIntent = new Intent(getApplicationContext(),
							GBViewList.class);
					newIntent.putExtras(bundle);
					startActivity(newIntent);
				}
			});
			
			if (!data.isNull("foto_url"))
				Avatar.setOnClickListener(new OnClickListener(){
	
					public void onClick(View arg0) {
						Bundle bundle = new Bundle();
						try {
							bundle.putString("URL", data.getString("foto_url"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Intent newIntent = new Intent(getApplicationContext(),
								SingleImage.class);
						newIntent.putExtras(bundle);
						startActivity(newIntent);
					}
					
				});

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void DoError() {
		// TODO Auto-generated method stub

	}
	
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
