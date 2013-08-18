package de.meisterfuu.animexx.overview;

import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class OverviewCardActivity extends SherlockActivity {
	JSONObject tempDetail;

	Context context = this;

	ProgressDialog dialog;

	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;

	int KaroGuthaben, KaroAbholbar, newENS;
	boolean karo = false;
	boolean ENS = false;

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
		actionBar.setTitle("Dashboard");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);
		
		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(true);

		CardStack stack2 = new CardStack();
		stack2.setTitle("REGULAR CARDS");
		mCardView.addStack(stack2);



		refresh();
		populate();
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


	private void refresh() {
		getKaroTalerStats();
		getNewENSStats();
	}


	public void DoError() {
		Request.doToast("Fehler", this);
	}


	public void populate() {
		/*
		this.addText("Eingeloggt als: " + Request.config.getString("username", "Unbekannt"));

		if (this.karo) {
			if (KaroAbholbar > 0) {

				OnClickListener KatoralerClick = new OnClickListener() {

					public void onClick(View v) {
						Log.i("xx", "KaroTaler Receiver 2");
						Bundle bundle = new Bundle();
						bundle.putInt("abholbar", KaroAbholbar);
						bundle.putInt("guthaben", KaroGuthaben);
						Intent newIntent = new Intent(context, KaroTalerAlert.class);
						newIntent.putExtras(bundle);
						newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(newIntent);
					}
				};

				this.addText("Du kannst " + KaroAbholbar + " abholen. (Guthaben: " + KaroGuthaben + ")", KatoralerClick);
			} else {
				this.addText("Du hast " + KaroGuthaben + " Karotaler.");
			}
		}

		if (this.ENS) {
			OnClickListener ENSClick = new OnClickListener() {

				public void onClick(View v) {
					Intent newIntent = new Intent(context, ENSActivity.class);
					context.startActivity(newIntent);
				}
			};
			if (newENS > 0) {
				this.addText("Du hast " + newENS + " ungelesene ENS.", ENSClick);
			} else {
				this.addText("Du hast keine ungelesenen ENS.", ENSClick);
			}
		}
		 */
	}


	public void getKaroTalerStats() {

		final OverviewCardActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {

					JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_statistik/?api=2"));
					JSONArray ab = a.getJSONObject("return").getJSONArray("kt_abholbar");
					if (ab.length() > 0) {
						KaroAbholbar = ab.getJSONObject(0).getInt("kt");
					} else {
						KaroAbholbar = 0;
					}
					KaroGuthaben = a.getJSONObject("return").getInt("kt_guthaben");

					temp.runOnUiThread(new Runnable() {

						public void run() {
							temp.karo = true;
							temp.populate();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							DoError();
						}
					});
				}
			}
		}).start();

	}


	public void getNewENSStats() {

		final OverviewCardActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {

					newENS = Request.GetUnread();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							temp.ENS = true;
							temp.populate();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {
							DoError();
						}
					});
				}
			}
		}).start();

	}

}
