package de.meisterfuu.animexx.events;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.MultiListAdapter;
import de.meisterfuu.animexx.other.MultiSherlockListActivity;
import de.meisterfuu.animexx.other.SlideMenu;
import de.meisterfuu.animexx.other.SlideMenuHelper;

public class EventViewDetail extends MultiSherlockListActivity {

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


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.listview_loading_bot);
		Loading = (RelativeLayout) findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);

		// setup slide menu
		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Event");
		actionBar.setHomeButtonEnabled(true);

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		adapter = new MultiListAdapter(this, Array);
		setListAdapter(adapter);

		ListView lv = getListView();
		lv.setDivider(null);
		lv.setDividerHeight(0);
		
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lv.getLayoutParams();
		mlp.setMargins(20, 0, 20, 0);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(Array.get(position).getOnclicklistener() != null) Array.get(position).getOnclicklistener().onClick(view);
			}

		});
		
		id = this.getIntent().getExtras().getLong("id");

		refresh();
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

		final EventViewDetail temp = this;
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
							adapter.notifyDataSetChanged();
							Loading.setVisibility(View.GONE);
							DoError();

						}
					});
				}

			}
		}).start();

	}


	public void DoError() {
		Request.doToast("Keine Verbindung zu den Animexx Servern.", this);
		if(Event == null) {
			this.finish();
		}
	}


	public void populate() {
		save = true;

		if (!Event.getLogo_url().equals("")) {
			this.addLogo(Event.getLogo_url());
		} else {
			this.addTitle(Event.getName());
			this.addSpacer();
		}

		if(!Event.getAdress().equals("")) this.addText(Event.getAdress());
		if(!Event.getContact().equals("")) this.addText(Event.getContact());
		if(!Event.getVeranstalter().equals("")) this.addText(Event.getVeranstalter());
		this.addSpacer();
		
		this.addText("Dabei: "+Event.getDabei_anzahl());
		this.addText("Größe: "+Event.getSize());
		this.addSpacer();
		
		this.addText("Beginn: "+Event.getTime_start());
		this.addText("Ende: "+Event.getTime_stop());
		this.addSpacer();
		
		OnClickListener NoteClick = new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putLong("id", Event.getId());
				Intent newIntent = new Intent(context, EventEditNote.class);
				newIntent.putExtras(bundle);
				startActivity(newIntent);				
			}
		};
		
		if(Note != null) {
			this.addPicture(R.drawable.note, "Notiz:", NoteClick);
			this.addText(Note, NoteClick);
		} else {
			this.addText("Notiz hinzufügen", NoteClick);
		}
		
		
		adapter.notifyDataSetChanged();


	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if(save){
			Array.clear();
			EventSQLHelper SQL = new EventSQLHelper(this);
			SQL.open();		
			Note = SQL.getSingleNote(Event.getId());
			SQL.close();			
			populate();
		}
	}
	

}
