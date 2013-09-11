package de.meisterfuu.animexx.other;

import java.util.List;

import com.actionbarsherlock.app.ActionBar;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.about;
import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.HomeKontaktActivity;
import de.meisterfuu.animexx.RPG.RPGViewList;
import de.meisterfuu.animexx.events.EventViewList;
import de.meisterfuu.animexx.other.SlideMenuInterface.OnSlideMenuItemClickListener;
import de.meisterfuu.animexx.overview.OverviewCardActivity;

public class SlideMenuHelper implements OnSlideMenuItemClickListener {

	private SlideMenu slidemenu;
	private Activity a;
	private int SlideSpeed = 200;


	public SlideMenuHelper(Activity A, ActionBar AC) {
		a = A;
		slidemenu = (SlideMenu) a.findViewById(R.id.slideMenu);
		slidemenu.init(a, R.menu.slide, this, SlideSpeed, AC);
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
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ENSActivity.class));
			break;
		case R.id.Home:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), HomeKontaktActivity.class));
			break;
		case R.id.About:
			//a.startActivity(new Intent().setClass(a.getApplicationContext(), HomeKontaktBigFragment.class));
			a.startActivity(new Intent().setClass(a.getApplicationContext(), about.class));
			break;
		case R.id.Guestbook:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), GBViewList.class));
			break;
		case R.id.Kontakte:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), ContactList.class));
			break;
		case R.id.Events:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), EventViewList.class));
			break;
		case R.id.Dashboard:
			a.startActivity(new Intent().setClass(a.getApplicationContext(), OverviewCardActivity.class));
			break;
		case R.id.Chat:
			//a.startActivity(new Intent().setClass(a.getApplicationContext(), Debug.class));
			startApplication("de.meisterfuu.animexxenger");
			break;
		case R.id.Foren:
			startApplication("com.tapatalk.sslanimexxde");
			break;
		}
		

	}
	
	
	public void startApplication(String packageName)
	{
	    try
	    {
	        Intent intent = new Intent("android.intent.action.MAIN");
	        intent.addCategory("android.intent.category.LAUNCHER");

	        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        List<ResolveInfo> resolveInfoList = a.getPackageManager().queryIntentActivities(intent, 0);

	        for(ResolveInfo info : resolveInfoList)
	            if(info.activityInfo.packageName.equalsIgnoreCase(packageName))
	            {
	                launchComponent(info.activityInfo.packageName, info.activityInfo.name);
	                return;
	            }

	        // No match, so application is not installed
	        showInMarket(packageName);
	    }
	    catch (Exception e) 
	    {
	        showInMarket(packageName);
	    }
	}

	private void launchComponent(String packageName, String name)
	{
	    Intent intent = new Intent("android.intent.action.MAIN");
	    intent.addCategory("android.intent.category.LAUNCHER");
	    intent.setComponent(new ComponentName(packageName, name));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

	    a.startActivity(intent);
	}

	private void showInMarket(String packageName)
	{
	    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    a.startActivity(intent);
	}
}
