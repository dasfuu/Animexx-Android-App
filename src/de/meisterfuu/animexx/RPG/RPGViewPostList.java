package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SlidingDrawer;
import de.meisterfuu.animexx.C2DMReceiver;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;
import de.meisterfuu.animexx.other.AvatarAdapter;
import de.meisterfuu.animexx.other.AvatarObject;

public class RPGViewPostList extends ListActivity implements UpDateUI {

	private ArrayList<RPGPostObject> RPGArray = new ArrayList<RPGPostObject>();
	private ArrayList<RPGCharaObject> CharaArray = new ArrayList<RPGCharaObject>();
	final Context context = this;

	private long id, count, counter; 
	

	private BroadcastReceiver receiver;
	private TaskRequest Task = null;
	private RPGPostAdapter adapter;

	private SlidingDrawer QuickAnswer;
	private RelativeLayout Loading;
	private EditText edAnswer;
	private Button Send, Chara;
	private CheckBox Aktion, InTime;
	private WebView Ava;

	private long SendAvatarID = -1;
	private RPGCharaObject SendChara = null;

	boolean ShowQuickAnswer, isToFu;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.rpg_post_list_slide);

		edAnswer = (EditText) findViewById(R.id.ed_answer);
		Send = (Button) findViewById(R.id.btsend);
		Chara = (Button) findViewById(R.id.btchara);
		Ava = (WebView) findViewById(R.id.webAva);

		QuickAnswer = (SlidingDrawer) findViewById(R.id.RPGQuickAnswer);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		
		Aktion = (CheckBox) findViewById(R.id.chAktion);
		InTime = (CheckBox) findViewById(R.id.chInTime);
		
		ShowQuickAnswer = true;
		if (!ShowQuickAnswer) QuickAnswer.setVisibility(View.GONE);

		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
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
				if(intent.getExtras().getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
					String[] arr = new String[1];
					intent.getExtras().keySet().toArray(arr);					
					//Log.i("RPG",Arrays.deepToString(arr));
					//Log.i("RPG",""+intent.getExtras().getLong("event-id"));
					if(intent.getExtras().getString("id").equalsIgnoreCase(""+id)) {
						Request.doToast("Neuer RPG Beitrag!", context);
						refresh();
					}
				}
			}
		};

		registerReceiver(receiver, filter);

		adapter = new RPGPostAdapter(this, RPGArray);
		setlist(adapter);
		refresh();
		FetchChara();

		Send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				send();
			}

		});
		
		Chara.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ShowCharaPicker();
			}

		});
	}


	private void send() {
		if(SendChara == null) {
			Request.doToast("Kein Charakter gewählt", context);
			ShowCharaPicker();
		} else if (edAnswer.getText().toString().length() != 0){
			sendData();
		} else {
			Request.doToast("Fehler: Leerer Post", context);
		}
	}


	private void setlist(RPGPostAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();

		lv.setStackFromBottom(true);
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

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
					RPGPostObject RPG = new RPGPostObject();
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
		if (ShowQuickAnswer) QuickAnswer.setVisibility(View.VISIBLE);

		RPGArray.addAll(z);
		adapter.refill();
		counter = RPGArray.get(RPGArray.size()-1).getId()+1;
		
		if (z.size() == 30) refresh();
	}


	public void DoError() {
		Loading.setVisibility(View.GONE);
		if (ShowQuickAnswer) QuickAnswer.setVisibility(View.VISIBLE);

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// ((Activity) context).finish();
			}
		});
		alertDialog.show();
	}


	public void refresh() {

		try {
			Loading.setVisibility(View.VISIBLE);
			QuickAnswer.setVisibility(View.GONE);
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
		if(QuickAnswer.isOpened()) {
			QuickAnswer.animateClose();
		} else {
			unregisterReceiver(receiver);
			C2DMReceiver.rpg = -1;
			Task.cancel(true);
			finish();
			return;
		}
	}


	private void ShowCharaPicker() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Chara wählen");

		ListView modeList = new ListView(context);
		
		RPGCharaObject[] Array = new RPGCharaObject[CharaArray.size()];
		CharaArray.toArray(Array);
		ArrayAdapter<RPGCharaObject> Adapter = new ArrayAdapter<RPGCharaObject>(context, android.R.layout.simple_list_item_1, android.R.id.text1, Array);

		modeList.setAdapter(Adapter);
		builder.setView(modeList);
		final Dialog dialog = builder.create();

		modeList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				SendChara = CharaArray.get(pos);
				dialog.dismiss();
				ShowAvatarPicker(SendChara);
				Chara.setText(SendChara.toString());
			}
		});

		dialog.show();

	}


	private void ShowAvatarPicker(final RPGCharaObject Chara) {
		SendAvatarID = -1;
		if (Chara.getAvatarArray().isEmpty()) {
			Ava.setVisibility(View.GONE);
			return;
		}
		if (!isToFu) {
			Ava.setVisibility(View.GONE);
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

		builder.setView(List);
		final Dialog dialog = builder.create();

		List.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				if(pos < Chara.getAvatarArray().size()){
					SendAvatarID = Chara.getAvatarArray().get(pos).getId();
					Ava.loadUrl(Chara.getAvatarArray().get(pos).getUrl());
					Ava.setVisibility(View.VISIBLE);
				} else {
					Ava.setVisibility(View.GONE);
				}
				dialog.dismiss();
			}
		});

		dialog.show();
	}


	private void FetchChara() {

		final RPGViewPostList temp = this;
		Send.setText("Lade..");
		Send.setClickable(false);

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
							temp.Send.setOnClickListener(null);
							temp.Send.setText("Fehler");
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

				Send.setClickable(true);
				Send.setText("Absenden");
			} else {
				Send.setClickable(true);
				Send.setOnClickListener(null);
				Send.setText("Keine Charas");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void sendData(){
		
		final RPGViewPostList temp = this;
		final ProgressDialog dialog = ProgressDialog.show(temp, "",
				"Sende...", true);
		new Thread(new Runnable() {
			public void run() {
				try {
					String erg = Request.SignSend(Request.sendRPGPost(edAnswer.getText().toString(), id, InTime.isChecked(), Aktion.isChecked(), SendChara.getId(), SendAvatarID));
					JSONObject jsonResponse = new JSONObject(erg);
					jsonResponse.getInt("return");
					temp.runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							edAnswer.setText("");
							SendChara = null;
							SendAvatarID = -1;
							QuickAnswer.animateClose();
							Request.doToast("Erfolgreich gesendet", temp);
							//refresh();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							Request.doToast("Fehler beim senden", temp);
						}
					});
				}

			}
		}).start();
	}

	
	protected void onStart() {
		super.onStart();
		C2DMReceiver.rpg = (int) id;
	}
     
     protected void onRestart() {
     	super.onRestart();
		C2DMReceiver.rpg = (int) id;
	}

     protected void onResume() {
     	super.onResume();
		C2DMReceiver.rpg = (int) id;
	}

     protected void onPause() {
     	super.onPause();
		C2DMReceiver.rpg = -1;
	}

     protected void onStop() {
     	super.onStop();
		C2DMReceiver.rpg = -1;
	}

     protected void onDestroy() {
     	super.onDestroy();
		C2DMReceiver.rpg = -1;
	}
}
