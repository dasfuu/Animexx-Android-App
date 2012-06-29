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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.TaskRequest;
import de.meisterfuu.animexx.UpDateUI;


public class RPGViewPostListStart extends ListActivity implements UpDateUI {

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
	boolean error;
	long id, count = 0;
	
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
		
		
				
		adapter = new RPGPostAdapter(this, RPGArray);
		setlist(adapter);
		refresh();	 
	    
		Send.setVisibility(View.GONE);
		edAnswer.setVisibility(View.GONE);
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

		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v, int pos,
					long id) {

				
				return true;
			}
		});

		lv.setOnScrollListener(new OnScrollListener() {
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			    if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
			        mPrevTotalItemCount = totalItemCount;
			        if(!error)refresh();
			    }
			}

			public void onScrollStateChanged(AbsListView view,
					int scrollState) {
				//Useless Forced Method -.-
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
						RPGa.add(RPG);
					}
					
				} else {
					//Keine weiteren Posts
					error = true;
				}
				
				return RPGa;
		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return RPGa;
	
	}


	public void UpDateUi(final String[] s) {		

		final ArrayList<RPGPostObject> z = getRPGlist(s[0]);
		RPGArray.addAll(z);
		adapter.refill();
	}

	public void DoError() {

		error = true;
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

