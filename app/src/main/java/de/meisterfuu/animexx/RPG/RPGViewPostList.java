package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import de.meisterfuu.animexx.GCMIntentService;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.AvatarAdapter;
import de.meisterfuu.animexx.other.AvatarObject;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.ImageSaveObject;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

public class RPGViewPostList extends SherlockListActivity implements UpDateUI {

	private ArrayList<RPGPostObject> RPGArray = new ArrayList<RPGPostObject>();
	private ArrayList<RPGCharaObject> CharaArray = new ArrayList<RPGCharaObject>();
	final Context context = this;

	private long id, count, counter;
	
	Editor tempo;

	private BroadcastReceiver receiver;
	private TaskRequest Task = null;
	private RPGPostAdapter adapter;

	private RelativeLayout Loading;
	private LinearLayout active_char;
	private TextView active_char_name;
	private ImageView active_char_img;

	private long SendAvatarID = -1;
	private RPGCharaObject SendChara = null;

	boolean ShowQuickAnswer, isToFu;

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;
	
	private ImageDownloaderCustom ImageLoader;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.rpg_post_list_slide);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("RPG");
		actionBar.setHomeButtonEnabled(true);
		
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		
		active_char = (LinearLayout) findViewById(R.id.active_char);
		active_char_name = (TextView) findViewById(R.id.active_char_name);
		active_char_img = (ImageView) findViewById(R.id.active_char_img);
		
		active_char_name.setText("Charakter wählen");
		active_char.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ShowCharaPicker();
			}
		});
		
		ImageLoader = new ImageDownloaderCustom("RPG_Avatar");

		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
			NotificationManager mManager;
			mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			mManager.cancel(32);
		} else
			finish();

		if (this.getIntent().hasExtra("count")) {
			Bundle bundle = this.getIntent().getExtras();
			count = bundle.getLong("count");
			counter = count - 30L;
		} else
			finish();

		if (this.getIntent().hasExtra("tofu")) {
			Bundle bundle = this.getIntent().getExtras();
			isToFu = bundle.getBoolean("tofu");
		} else
			isToFu = false;

		IntentFilter filter = new IntentFilter();
		filter.addAction("com.google.android.c2dm.intent.RECEIVE");
		filter.addCategory("de.meisterfuu.animexx");

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context arg0, Intent intent) {
				if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
					String[] arr = new String[1];
					intent.getExtras().keySet().toArray(arr);
					// Log.i("RPG",Arrays.deepToString(arr));
					// Log.i("RPG",""+intent.getExtras().getLong("event-id"));
					if (intent.getExtras().getString("id").equalsIgnoreCase("" + id)) {
						Request.doToast("Neuer RPG Beitrag!", context);
						refresh();
					}
				}
			}
		};

		registerReceiver(receiver, filter);

		adapter = new RPGPostAdapter(this, RPGArray, ImageLoader);
		setlist(adapter);
		refresh();
		FetchChara();

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.ac_answer:
			send();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_answer, menu);
		return super.onCreateOptionsMenu(menu);
	}


	boolean newP = false;
	
	private void send() {
		if (SendChara == null) {
			Request.doToast("Kein Charakter gewählt", context);
			ShowCharaPicker();
			newP = true;
		} else {
			//NEW Post Activity
			newP = false;
			Bundle bundle = new Bundle();
			bundle.putLong("id", id);
			bundle.putString("char_name", SendChara.getName());
			bundle.putString("rpg_name", "");
			bundle.putString("avatar_url", "");
			bundle.putLong("avatar_id", SendAvatarID);
			bundle.putLong("char_id", SendChara.getId());

			Intent newIntent = new Intent(this, RPGNewPost.class);
			newIntent.putExtras(bundle);
			startActivity(newIntent);
		}
	}


	private void setlist(RPGPostAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setStackFromBottom(true);
		lv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {

				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				// refresh();

			}
		});
	}


	private ArrayList<RPGPostObject> getRPGlist(String JSON) {

		ArrayList<RPGPostObject> RPGa = new ArrayList<RPGPostObject>();
		try {
			JSONObject jsonResponse = new JSONObject(JSON);
			JSONArray RPGlist = jsonResponse.getJSONArray("return");
			RPGa = new ArrayList<RPGPostObject>(RPGlist.length());

			if (RPGlist.length() != 0) {
				for (int i = 0; i < RPGlist.length(); i++) {
					JSONObject tp = RPGlist.getJSONObject(i);
					RPGPostObject RPG = new RPGPostObject(id);
					RPG.parseJSON(tp);
					if ((RPG.getId() >= counter) || RPGArray.isEmpty()) RPGa.add(RPG); // Sehr langsam bei vielen Eintraegen!
				}

			} else {
				// Keine weiteren Posts

			}

			return RPGa;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return RPGa;

	}


	public void UpDateUi(final String[] s) {
		final ArrayList<RPGPostObject> z = getRPGlist(s[0]);

		Loading.setVisibility(View.GONE);
		
		RPGArray.addAll(z);
		adapter.refill();
		if (RPGArray.size() != 0) counter = RPGArray.get(RPGArray.size() - 1).getId() + 1;

		if (z.size() == 30) refresh();
	}


	public void DoError() {
		Loading.setVisibility(View.GONE);

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// ((Activity) context).finish();
			}
		});
		alertDialog.show();
	}


	public void refresh() {

		try {
			Loading.setVisibility(View.VISIBLE);
			HttpGet[] HTTPs = new HttpGet[1];

			if (counter <= 0L) counter = 1L;
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/get_postings/?api=2&rpg=" + id + "&limit=30&text_format=html&from_pos=" + counter);
			Task = new TaskRequest(this);
			Task.execute(HTTPs);

		} catch (Exception e) {
			DoError();
			e.printStackTrace();
		}

	}


	@Override
	public void onBackPressed() {
			unregisterReceiver(receiver);
			GCMIntentService.rpg = -1;
			Task.cancel(true);
			finish();
			return;		
	}


	private void ShowCharaPicker() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Chara wählen");

		ListView modeList = new ListView(context);

		RPGCharaObject[] Array = new RPGCharaObject[CharaArray.size()];
		CharaArray.toArray(Array);
		ArrayAdapter<RPGCharaObject> Adapter = new ArrayAdapter<RPGCharaObject>(context, android.R.layout.simple_list_item_1, android.R.id.text1, Array);

		modeList.setAdapter(Adapter);
		modeList.setBackgroundResource(R.color.custom_theme_color);
		builder.setView(modeList);
		final Dialog dialog = builder.create();

		modeList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				SendChara = CharaArray.get(pos);
				active_char_name.setText(SendChara.getName());
				dialog.dismiss();
				ShowAvatarPicker(SendChara);
			}
		});

		dialog.show();

	}


	private void ShowAvatarPicker(final RPGCharaObject Chara) {
		SendAvatarID = -1;
		if (Chara.getAvatarArray().isEmpty()) {
			if(newP) send();
			return;
		}
		if (!isToFu) {
			if(newP) send();
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Avatar wählen");

		ListView List = new ListView(context);

		@SuppressWarnings("unchecked")
		ArrayList<AvatarObject> AvatarArray = (ArrayList<AvatarObject>) Chara.getAvatarArray().clone();
		AvatarArray.add(null);

		AvatarAdapter Adapter = new AvatarAdapter(this, AvatarArray);
		List.setAdapter(Adapter);
		List.setBackgroundResource(R.color.custom_theme_color);
		builder.setView(List);
		final Dialog dialog = builder.create();

		List.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				if (pos < Chara.getAvatarArray().size()) {
					SendAvatarID = Chara.getAvatarArray().get(pos).getId();	
					//active_char_img
					ImageLoader.download(new ImageSaveObject(Chara.getAvatarArray().get(pos).getUrl(), id+"_"+SendChara.getId()+"_"+SendAvatarID), active_char_img);
				} else {

				}
				dialog.dismiss();
				if(newP) send();
			}
		});

		dialog.show();
	}


	private void FetchChara() {

		final RPGViewPostList temp = this;

		new Thread(new Runnable() {

			public void run() {
				try {
					final String erg = Request.makeSecuredReq("https://ws.animexx.de/json/rpg/get_meine_charaktere/?api=2&rpg=" + id);

					temp.runOnUiThread(new Runnable() {

						public void run() {
							getCharaList(erg);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {

						public void run() {

						}
					});
				}

			}
		}).start();
	}


	private void getCharaList(String JSON) {

		try {
			JSONObject jsonResponse = new JSONObject(JSON);
			JSONArray RPGlist = jsonResponse.getJSONArray("return");
			CharaArray = new ArrayList<RPGCharaObject>(RPGlist.length());

			JSONObject tp;
			RPGCharaObject RPG;

			if (RPGlist.length() != 0) {
				for (int i = 0; i < RPGlist.length(); i++) {
					tp = RPGlist.getJSONObject(i);
					RPG = new RPGCharaObject();
					RPG.parseJSON(tp);
					CharaArray.add(RPG);
				}
			} else {

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}





	@Override
	protected void onStart() {
		super.onStart();
		GCMIntentService.rpg = (int) id;
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		GCMIntentService.rpg = (int) id;
	}


	@Override
	protected void onResume() {
		super.onResume();
		refresh();
		GCMIntentService.rpg = (int) id;
	}


	@Override
	protected void onPause() {
		super.onPause();
		GCMIntentService.rpg = -1;
	}


	@Override
	protected void onStop() {
		super.onStop();
		GCMIntentService.rpg = -1;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		GCMIntentService.rpg = -1;
	}
	
}
