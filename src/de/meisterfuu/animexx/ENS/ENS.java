package de.meisterfuu.animexx.ENS;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.*;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.ENS.ENSAnswer;
import de.meisterfuu.animexx.ENS.ENSEingangFragment;
import de.meisterfuu.animexx.ENS.ENSAusgangFragment;
import de.meisterfuu.animexx.other.SlideMenuHelper;
import de.meisterfuu.animexx.other.TabListener;
import de.meisterfuu.animexx.other.SlideMenu;

public class ENS extends SherlockFragmentActivity  {

	private SlideMenu slidemenu;
	private SlideMenuHelper slidemenuhelper;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Screen Rotating Fix?
		if(null != savedInstanceState) return;

		setContentView(R.layout.activity_main);

		slidemenuhelper = new SlideMenuHelper(this, getSupportActionBar());
		slidemenu = slidemenuhelper.getSlideMenu();

		// setup action bar for tabs
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setIcon(R.drawable.actionbar_in);

		Tab tab = actionBar.newTab().setText("Eingang").setTabListener(new TabListener<ENSEingangFragment>(this, "Eingang", ENSEingangFragment.class));
		actionBar.addTab(tab);

		tab = actionBar.newTab().setText("Ausgang").setTabListener(new TabListener<ENSAusgangFragment>(this, "Ausgang", ENSAusgangFragment.class));
		actionBar.addTab(tab);

		/*
		 * There are two ways to add the slide menu:
		 * From code or to inflate it from XML (then you have to declare it in the activities layout XML)
		 */
		// this is from code. no XML declaration necessary, but you won't get state restored after rotation.
		// slidemenu = new SlideMenu(this, R.menu.slide, this, 333);
		// this inflates the menu from XML. open/closed state will be restored after rotation, but you'll have to call init.
		//slidemenu = (SlideMenu) findViewById(R.id.slideMenu);
		//slidemenu.init(this, R.menu.slide, this, 200);

		// this can set the menu to initially shown instead of hidden
		// slidemenu.setAsShown();

		// set optional header image
		// slidemenu.setHeaderImage(R.drawable.animexx);

		// this demonstrates how to dynamically add menu items
		// SlideMenuItem item = new SlideMenuItem();
		// item.id = MYITEMID;
		// item.icon = getResources().getDrawable(R.drawable.add);
		// item.label = "Fünf";
		// slidemenu.addMenuItem(item);

	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			slidemenu.show();
			return true;
		case R.id.NewENS:
			startActivity(new Intent().setClass(getApplicationContext(), ENSAnswer.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.actionbar_ens, menu);
		return super.onCreateOptionsMenu(menu);
	}



}
