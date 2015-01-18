package de.meisterfuu.animexx.overview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.fima.cardsui.objects.Card;
import com.fima.cardsui.objects.Card.OnCardSwiped;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.cards.ENSCard;
import de.meisterfuu.animexx.cards.GBCard;
import de.meisterfuu.animexx.cards.InteractionCard;
import de.meisterfuu.animexx.cards.KarotalerCard;
import de.meisterfuu.animexx.events.EventViewPage;
import de.meisterfuu.animexx.fanworks.Fanart;
import de.meisterfuu.animexx.other.KarotalerActivity;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

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
	private CardStack userStack, ensStack, birthdayStack, eventStack, gbStack, interactionStack, karotalerStack;


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
		mCardView.setSwipeable(true);

		//refresh();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.refresh:
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.overview_card, menu);
		return super.onCreateOptionsMenu(menu);
	}


	private void refresh() {
		mCardView.clearCards();
		getKaroTalerStats();
		getNewENSStats();
		getInteractionStats();
		getGuestbookStats();
	}


	public void DoError() {
		// Request.doToast("Fehler", this);
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
							karotalerStack = new CardStack();
							mCardView.addStack(karotalerStack);
							
							Card card;

							if (KaroAbholbar > 0) {
								card = new KarotalerCard("Neue Karotaler", "Du kannst " + KaroAbholbar + " Karotaler abholen. (Guthaben: " + KaroGuthaben + ")");
								OnClickListener on = new OnClickListener() {

									public void onClick(View v) {
										Intent newIntent = new Intent(context, KarotalerActivity.class);
										context.startActivity(newIntent);
										karotalerStack.remove(0);
										mCardView.refresh();
									}
								};
								card.setOnClickListener(on);
							} else {
								card = new KarotalerCard("Karotaler", "Du hast " + KaroGuthaben + " Karotaler.");
							}
							karotalerStack.add(card);
							mCardView.refresh();

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


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		refresh();
	}


	public void getNewENSStats() {

		final OverviewCardActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {

					newENS = Request.GetUnread();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (newENS > 0) {
								ensStack = new CardStack();
								mCardView.addStack(ensStack);
								
								Card enscard = new ENSCard("Neue ENS", "Du hast " + newENS + " ungelesene ENS.");
								OnClickListener ENSClick = new OnClickListener() {

									public void onClick(View v) {
										Intent newIntent = new Intent(context, ENSActivity.class);
										context.startActivity(newIntent);
									}
								};
								enscard.setOnClickListener(ENSClick);
								ensStack.add(enscard);
								mCardView.refresh();
							}
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


	public void getGuestbookStats() {

		final String id = Request.config.getString("id", "none");
		final OverviewCardActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {

					JSONObject a;
					final long dash_guestbook_id_latest = Request.config.getLong("dash_guestbook_id_latest", 0);
					a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id=" + id + "&text_format=html&api=2&anzahl=1"));

					final JSONArray ab = a.getJSONObject("return").getJSONArray("eintraege");
					final JSONObject eintrag = ab.getJSONObject(0);
					final long gbid = eintrag.getLong("id");

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (gbid > dash_guestbook_id_latest) {
								try {
									
									gbStack = new CardStack();
									mCardView.addStack(gbStack);
									

									Card gbcard;

									gbcard = new GBCard("Neuer Gästebucheintrag", "Von " + eintrag.getJSONObject("von").getString("username"));

									OnClickListener GBClick = new OnClickListener() {

										public void onClick(View v) {
											Intent newIntent = new Intent(context, GBViewList.class);
											context.startActivity(newIntent);
											Request.config.edit().putLong("dash_guestbook_id_latest", gbid).commit();
											gbStack.remove(0);
											mCardView.refresh();
										}
									};

									gbcard.setOnCardSwipedListener(new OnCardSwiped() {

										public void onCardSwiped(Card card, View layout) {
											Request.config.edit().putLong("dash_guestbook_id_latest", gbid).commit();
										}
									});
									gbcard.setOnClickListener(GBClick);
									gbStack.add(gbcard);

									mCardView.refresh();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
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


	public void getInteractionStats() {

		final OverviewCardActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {

					JSONObject a;
					long interaction_time_vom = Request.config.getLong("dash_interaction_time_vom", 0) + 1;
					//interaction_time_vom = 0; //DEBUG
					a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/persstart5/get_widget_data/?api=2&widget_id=interaktionen&zeit_von=" + interaction_time_vom));

					final JSONArray ab = a.getJSONArray("return");
					final int newInteraction = ab.length();

					temp.runOnUiThread(new Runnable() {

						public void run() {
							if (newInteraction > 0) {
								
								interactionStack = new CardStack();
								mCardView.addStack(interactionStack);
								
								for (int i = 0; i < newInteraction; i++) {
									try {

										String title = ab.getJSONObject(i).getString("item_name");
										if (title.equals("null") || title == null) title = ab.getJSONObject(i).getString("event_typ_name");
										Card interactioncard = new InteractionCard(title, ab.getJSONObject(i).getString("event_typ_name") + "\n"
												+ ab.getJSONObject(i).getJSONObject("von").getString("username"));
										final String url = ab.getJSONObject(i).getString("link");
										OnClickListener InteraktionClick = new OnClickListener() {

											public void onClick(View v) {
												
												Uri uri = Uri.parse("http://animexx.onlinewelten.com/"+url);
												try{
													Intent intent = new Intent(Intent.ACTION_VIEW, uri);
													context.startActivity(intent);
												} catch(Exception e){
													Toast.makeText(context, "Das ist noch nicht möglich.", Toast.LENGTH_SHORT).show();
													e.printStackTrace();
												}
												//Toast.makeText(context, "Das ist noch nicht mglich.", Toast.LENGTH_SHORT).show();
												// Intent newIntent = new Intent(context, HomeKontaktActivity.class);
												// context.startActivity(newIntent);
												//Request.config.edit().putLong("dash_interaction_time_vom", ab.getJSONObject(j).getLong("date_server_ts")).commit();
											}
										};
										final int j = i;
										interactioncard.setOnCardSwipedListener(new OnCardSwiped() {

											public void onCardSwiped(Card card, View layout) {
												try {
													Request.config.edit().putLong("dash_interaction_time_vom", ab.getJSONObject(j).getLong("date_server_ts")).commit();
												} catch (JSONException e) {
													e.printStackTrace();
												}
											}
										});
										interactioncard.setOnClickListener(InteraktionClick);
										interactionStack.add(interactioncard);
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}
								mCardView.refresh();
							}
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
