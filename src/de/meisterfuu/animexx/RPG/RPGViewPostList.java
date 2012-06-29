package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;

public class RPGViewPostList extends ListActivity implements UpDateUI {

	AlertDialog alertDialog;
	ArrayList<RPGPostObject> RPGArray = new ArrayList<RPGPostObject>();
	ProgressDialog dialog;
	final Context context = this;
	RPGPostAdapter adapter;
	TaskRequest Task = null;
	int mPrevTotalItemCount;
	private BroadcastReceiver receiver;
	EditText edAnswer;
	Button Send;
	long id, count;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		
		setContentView(R.layout.rpg_post_list_answer);
		edAnswer = (EditText) findViewById(R.id.ed_answer);
		Send = (Button) findViewById(R.id.btsend);
		
		
		if (this.getIntent().hasExtra("id")) {
			Bundle bundle = this.getIntent().getExtras();
			id = bundle.getLong("id");
		} else finish();
		
		if (this.getIntent().hasExtra("count")) {
			Bundle bundle = this.getIntent().getExtras();
			count = bundle.getLong("count");  
		} else finish();
		
		  IntentFilter filter = new IntentFilter();
		  filter.addAction("com.google.android.c2dm.intent.RECEIVE");
		  filter.addCategory("de.meisterfuu.animexx");

		  receiver = new BroadcastReceiver() {	
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				Request.doToast("Neuer RPG Beitrag!", context);
				refresh();	 			
			}
		  };

		  registerReceiver(receiver, filter);
				
		adapter = new RPGPostAdapter(this, RPGArray);
		setlist(adapter);
		refresh();	 
	    
		Send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				send();
			}


		});
	}	
	
	private void send() {
		//Neuen Beitrag senden		
	}
	
	@Override
	protected void onDestroy() {
	  super.onDestroy();
	  unregisterReceiver(receiver);
	}
	
	private void setlist(RPGPostAdapter a) {

		setListAdapter(a);

		ListView lv = getListView();
		
		lv.setStackFromBottom(true);
		lv.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {

				
				return true;
			}
		});

		
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int pos, long id) {

				refresh();
				
			}
		});
	}

	private ArrayList<RPGPostObject> getRPGlist(String JSON) {
		
			ArrayList<RPGPostObject> RPGa = new ArrayList<RPGPostObject>();
			try{
				JSONObject jsonResponse = new JSONObject(JSON);
				JSONArray RPGlist = jsonResponse.getJSONArray("return");
				RPGa = new ArrayList<RPGPostObject>(RPGlist.length());
		
				if (RPGlist.length() != 0) {
					for (int i = 0; i < RPGlist.length(); i++) {
						JSONObject tp = RPGlist.getJSONObject(i);	
						RPGPostObject RPG = new RPGPostObject();
						RPG.parseJSON(tp);
						if(!RPGArray.contains(RPG))RPGa.add(RPG);
					}
					
				} else {
					//Keine weiteren Posts
					
				}
				
				return RPGa;
		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return RPGa;
	
	}


	public void UpDateUi(final String[] s) {		

		final ArrayList<RPGPostObject> z = getRPGlist(s[0]);
		if (z.size() == 30) refresh();
		RPGArray.addAll(z);
		adapter.refill();
	}

	public void DoError() {


		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage("Es ist ein Fehler aufgetreten. Kein Internet?");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      ((Activity) context).finish();
		   }
		});
		alertDialog.show();
	}

	public void refresh() {
			
		try {			

			HttpGet[] HTTPs = new HttpGet[1];
			long counter = count-30;
			if (counter < 0) counter = 0;
			HTTPs[0] = Request.getHTTP("https://ws.animexx.de/json/rpg/get_postings/?api=2&rpg="+id+"&limit=30&text_format=html&from_pos="+counter);
			Task = new TaskRequest(this);
			Task.execute(HTTPs);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		Task.cancel(true);
		finish();
		return;
	}
	
	
}
