package de.meisterfuu.animexx.profil;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.views.CardUI;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.cards.PictureCard;
import de.meisterfuu.animexx.cards.TextCard;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ProgressDialog;
import android.content.Context;

public class UserCardSteckbrief extends SherlockActivity implements UpDateUI {

	JSONObject tempDetail;

	Context context = this;

	JSONObject data;

	String userid;
	String username;
	ProgressDialog dialog;

	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("steckbriefbilder");

	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	private CardUI mCardView;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_overview_card);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Home");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		if (this.getIntent().hasExtra("id")) {
			userid = this.getIntent().getStringExtra("id");
		}

		LoadData();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.user_card_steckbrief, menu);
		return true;
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


	private void LoadData() {
		HttpGet[] HTTPs = new HttpGet[1];
		try {
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/mitglieder/steckbrief/?user_id=" + userid + "&api=2&img_max_x=1000&img_max_y=1000&img_format=jpg&img_quality=90");
			new TaskRequest(this).execute(HTTPs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void UpDateUi(String[] s) {
		try {
			data = new JSONObject(s[0]);
			data = data.getJSONObject("return");

			username = data.getString("username");
			getSupportActionBar().setTitle(username);
			String vor = "";
			String card1 = "";
			String card2 = "";

			if (data.getBoolean("freigabe_steckbrief")) {
				if (!data.isNull("foto_url")) {
					Card PictureCard = new PictureCard(data.getString("foto_url"), userid, ImageLoader);
					mCardView.addCard(PictureCard);
				}

				if (!data.isNull("vorname")) {
					vor = data.getString("vorname");
				}
				if (!data.isNull("nachname")) {
					vor += " " + data.getString("nachname");
				}
				if (vor.length() > 0) card1 += vor;

				if (!data.isNull("geburtstag")) {
					card1 += "\nGeburtstag: " + data.getString("geburtstag");
				}

				if (!data.isNull("alter")) {
					card1 += "\nAlter: " + data.getString("alter");
				}

				if (!data.isNull("geschlecht")) {
					if (data.getString("geschlecht").equalsIgnoreCase("w")) {
						card1 += "\nGeschlecht: Weiblich";
					} else if (data.getString("geschlecht").equalsIgnoreCase("m")) {
						card1 += "\nGeschlecht: Männlich";
					} else {
						card1 += "\nGeschlecht: Unbekannt";
					}
				}

				if (!data.isNull("email")) {
					card1 += "\nE-Mail: " + data.getString("email");
				}

				if (!data.isNull("homepage")) {
					card1 += "\nHomepage: " + data.getString("homepage");
				}

				if (!data.isNull("strasse")) {
					card1 += "\nStraße: " + data.getString("strasse");
				}

				if (!data.isNull("plz")) {
					card1 += "\nPLZ: " + data.getString("plz");
				}

				if (!data.isNull("ort")) {
					card1 += "\nOrt: " + data.getString("ort");
				}

				if (!data.isNull("land")) {
					card1 += "\nLand: " + data.getString("land");
				}

				if (card1.length() > 0) {
					if(card1.startsWith("\n")){
						card1 = card1.substring(1);
					}
					Card TextCard = new TextCard(username, card1, true);
					mCardView.addCard(TextCard);
				}
				
				if (data.getJSONArray("kontaktdaten").length() > 0) {
					JSONArray ar = data.getJSONArray("kontaktdaten");
					for(int i = 0; i < ar.length(); i++){
						if(i > 0){
							card2 +="\n";
						}
						card2 += ar.getJSONObject(i).getString("typ")+": " + ar.getJSONObject(i).getString("wert");
					}
					Card TextCard = new TextCard("Kontaktdaten", card2, true);
					mCardView.addCard(TextCard);
				}

			}

			mCardView.refresh();
			// ENS.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// Bundle bundle2 = new Bundle();
			// bundle2.putString("betreff", "");
			// bundle2.putString("an", username);
			// bundle2.putString("anid", userid);
			// Intent newIntent = new Intent(getApplicationContext(),
			// ENSAnswer.class);
			// newIntent.putExtras(bundle2);
			// startActivity(newIntent);
			//
			// }
			// });
			//
			// GB.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// Bundle bundle = new Bundle();
			// bundle.putString("id", userid);
			// bundle.putString("username", username);
			// Intent newIntent = new Intent(getApplicationContext(),
			// GBViewList.class);
			// newIntent.putExtras(bundle);
			// startActivity(newIntent);
			// }
			// });

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	public void refresh() {
		// TODO Auto-generated method stub

	}


	public void DoError() {
		// TODO Auto-generated method stub

	}

}
