package de.meisterfuu.animexx.events;

import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.views.CardUI;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.cards.PictureCard;
import de.meisterfuu.animexx.cards.TextCard;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.MultiListAdapter;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class EventViewCardDetail extends SherlockActivity {

	EventObject Event;
	String Note;

	long id;

	Context context = this;

	ProgressDialog dialog;
	RelativeLayout Loading;
	Boolean save = false;

	boolean error = false;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;
	private CardUI mCardView;
	ImageDownloaderCustom imagerloader;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_event_view_card_detail);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Event");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);
		imagerloader = new ImageDownloaderCustom("events");

		id = this.getIntent().getExtras().getLong("id");

		refresh();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.action_pages:
			if(Loading.getVisibility() == View.VISIBLE) return true;
			Bundle bundle = new Bundle();
			bundle.putLong("event_id", Event.getId());
			bundle.putString("pages", Event.getPages().toString());
			Intent newIntent = new Intent(context, EventViewPage.class);
			newIntent.putExtras(bundle);
			startActivity(newIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.event_view_card_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}


	private void refresh() {

		final EventViewCardDetail temp = this;
		final long id = this.id;
		Loading.setVisibility(View.VISIBLE);

		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/events/event/details/?api=2&id=" + id);

					JSONObject jsonResponse = new JSONObject(JSON);
					JSONObject data = jsonResponse.getJSONObject("return");

					Event = new EventObject();
					Event.parseJSON(data);
					Event.setDetail_json(data.toString());

					EventSQLHelper SQL = new EventSQLHelper(temp);
					SQL.open();
					SQL.updateEvent(Event);
					Note = SQL.getSingleNote(id);
					SQL.close();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
							populate();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();

					EventSQLHelper SQL = new EventSQLHelper(temp);
					SQL.open();
					Event = SQL.getSingleEvent(id);
					Note = SQL.getSingleNote(id);
					SQL.close();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							Loading.setVisibility(View.GONE);
							DoError();
							populate();
						}
					});
				}

			}
		}).start();

	}


	public void DoError() {
		Request.doToast("Keine Verbindung zu den Animexx Servern.", this);
		if (Event == null) {
			this.finish();
		}
	}


	public void populate() {
		save = true;

		mCardView.clearCards();

		if (!Event.getLogo_url().equals("")) {
			mCardView.addCard(new PictureCard(Event.getLogo_url(), Event.getId() + "", imagerloader));
		}

		String card1 = "";

		if (!Event.getAdress().equals("")) card1 += Event.getAdress() + "\n";
		if (!Event.getContact().equals("")) card1 += Event.getContact() + "\n";
		if (!Event.getVeranstalter().equals("")) card1 += Event.getVeranstalter() + "\n";

		mCardView.addCard(new TextCard(Event.getName(), card1, true));

		String card2 = "";

		card2 += "Dabei: " + Event.getDabei_anzahl() + "\n";
		card2 += "Größe: " + Event.getSize() + "\n";

		mCardView.addCard(new TextCard(Event.getName(), card2, false));

		String card3 = "";

		card3 += "Beginn: " + Event.getTime_start() + "\n";
		card3 += "Ende: " + Event.getTime_stop() + "\n";

		mCardView.addCard(new TextCard(Event.getName(), card3, false));

		OnClickListener NoteClick = new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("id", Event.getId());
				Intent newIntent = new Intent(context, EventEditNote.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);
			}
		};

		Card notizcard;

		if (Note != null) {
			notizcard = new TextCard("Notiz", Note, true);
		} else {
			notizcard = new TextCard("Notiz", "Notiz anlegen", true);
		}

		notizcard.setOnClickListener(NoteClick);

		mCardView.addCard(notizcard);

		mCardView.refresh();

	}


	@Override
	public void onResume() {
		super.onResume();
		if (save) {
			EventSQLHelper SQL = new EventSQLHelper(this);
			SQL.open();
			Note = SQL.getSingleNote(Event.getId());
			SQL.close();
			populate();
		}
	}

}
