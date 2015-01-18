package de.meisterfuu.animexx.events;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import android.os.Bundle;
import android.widget.TextView;

public class EventEditNote extends SherlockActivity {

	TextView edNotiz;
	long id;
	boolean save = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_event_edit_note);

		// setup slide menu
		//slidemenuhelper = new SlideMenuHelper(this, this.getSupportActionBar());
		//slidemenu = slidemenuhelper.getSlideMenu();
		// setup action bar
		//ActionBar actionBar = getSupportActionBar();
		//actionBar.setTitle("Notiz");
		//actionBar.setHomeButtonEnabled(true);

		edNotiz = (TextView) findViewById(R.id.edNote);

		if (this.getIntent().hasExtra("id")) {
			id = this.getIntent().getLongExtra("id", -1L);
		}
		if (id == -1L) {
			this.finish();
		} else {
			save = true;
		}

	}


	@Override
	public void onResume() {
		super.onResume();
		if(save){
			EventSQLHelper SQL = new EventSQLHelper(this);
			SQL.open();		
			edNotiz.setText(SQL.getSingleNote(id));
			SQL.close();			
		}
	}


	@Override
	public void onPause() {
		super.onPause();
		if(save){
			EventSQLHelper SQL = new EventSQLHelper(this);
			SQL.open();
			SQL.updateNote(edNotiz.getText().toString(), id);
			SQL.close();			
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//slidemenu.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
