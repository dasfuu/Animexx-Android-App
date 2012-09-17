package de.meisterfuu.animexx.other;

import android.app.Activity;
import android.content.Intent;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.about;
import de.meisterfuu.animexx.ENS.ENS;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.PersonalHomeListAll;
import de.meisterfuu.animexx.RPG.RPGViewList;
import de.meisterfuu.animexx.other.SlideMenuInterface.OnSlideMenuItemClickListener;

public class SlideMenuHelper implements OnSlideMenuItemClickListener {

	private SlideMenu slidemenu;
	private Activity a;
	private int SlideSpeed = 200;


	public SlideMenuHelper(Activity A) {
		a = A;
		slidemenu = (SlideMenu) a.findViewById(R.id.slideMenu);
		slidemenu.init(a, R.menu.slide, this, SlideSpeed);
	}


	public SlideMenu getSlideMenu() {
		return slidemenu;
	}


	public void onSlideMenuItemClick(int itemId) {

		switch (itemId) {
		case R.id.Settings:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), Settings.class));
			break;
		case R.id.RPG:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), RPGViewList.class));
			break;
		case R.id.ENS:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ENS.class));
			break;
		case R.id.Home:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), PersonalHomeListAll.class));
			break;
		case R.id.About:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), about.class));
			break;
		case R.id.Guestbook:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), GBViewList.class));
			break;
		case R.id.Kontakte:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ContactList.class));
			break;
		}

	}
}
