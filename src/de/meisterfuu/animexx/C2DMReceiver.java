package de.meisterfuu.animexx;

import de.meisterfuu.animexx.ENS.ENS;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.ContactsActivityList;
import de.meisterfuu.animexx.RPG.RPGViewList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver {

	NotificationManager mManager;
	public static int rpg = -1;


	public void onReceive(Context context, Intent intent) {
		Request.config = PreferenceManager.getDefaultSharedPreferences(context);
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}


	private void handleMessage(Context context, Intent intent) {

		if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventENS")) {
			String von = intent.getExtras().getString("from_username");
			notifyENS("Neue ENS von " + von, "Neue ENS", context, 42);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventGaestebuch")) {
			String von = intent.getExtras().getString("from_username");
			notifyGB("Neuer Gästebucheintrag von " + von, "Neuer GB Eintrag", context, 43);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventGeburtstag")) {
			String von = intent.getExtras().getString("from_username");
			String url = intent.getExtras().getString("url");
			notifyURL(von + " hat Geburtstag", "Ein Geburtstag!", context, 44, url);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
			//
			if(!intent.getExtras().getString("id").equals(""+C2DMReceiver.rpg )) {
				notifyRPG("Ein neues Posting im RPG \""+intent.getExtras().getString("title")+"\"", "Neuer RPG Eintrag", context);
			}
		} else {
			notifyURL("Es ist etwas passiert, nur was?", "Event 42!", context, 666, "http://animexx.de");
		}

	}


	private void handleRegistration(Context context, Intent intent) {

		String registration = intent.getStringExtra("registration_id");
		if (intent.getStringExtra("error") != null) {
			Log.i("C2DM", "error");
			Editor edit = Request.config.edit();
			edit.putString("c2dm", "x");
			edit.commit();
		} else if (intent.hasExtra("unregistered")) {
			Editor edit = Request.config.edit();
			edit.putString("c2dm", "x");
			edit.commit();
			Log.i("C2DM", "unregistered");
		} else if (registration != null) {
			Log.i("C2DM", registration);

			Editor edit = Request.config.edit();
			edit.putString("c2dm", registration);
			edit.commit();
			try {
				Request.sendC2DM(registration, "0");
				Request.setC2DM();
				// Request.ENSNotify("Push Test!",
				// "Deine erste gepushte ENS :D Scheint ja alles zu funktionieren!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}


	@SuppressWarnings("deprecation")
	private void notifyENS(String s, String title, Context context, int id) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(id);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = s; 				// ticker-text
		long when = System.currentTimeMillis(); 	// notification time
		CharSequence contentTitle = title; 			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent(context, ENS.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		// notification.defaults |= Notification.DEFAULT_SOUND;
		notification.sound = Uri.parse(Request.config.getString("ringtonePrefENS", "DEFAULT_NOTIFICATION_URI"));
		if (Request.config.getBoolean("vibration", true)) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(id, notification);
	}


	@SuppressWarnings("deprecation")
	private void notifyURL(String s, String title, Context context, int id, String URL) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(id);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = s; 				// ticker-text
		long when = System.currentTimeMillis(); 	// notification time
		CharSequence contentTitle = title;			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent().setClass(context, ContactsActivityList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.sound = Uri.parse(Request.config.getString("ringtonePref", "DEFAULT_NOTIFICATION_URI"));
		if (Request.config.getBoolean("vibration", true)) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(id, notification);
	}


	@SuppressWarnings("deprecation")
	private void notifyGB(String s, String title, Context context, int id) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(id);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = s; 				// ticker-text
		long when = System.currentTimeMillis();		// notification time
		CharSequence contentTitle = title; 			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent(context, GBViewList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.sound = Uri.parse(Request.config.getString("ringtonePref", "DEFAULT_NOTIFICATION_URI"));
		if (Request.config.getBoolean("vibration", true)) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(id, notification);
	}

	@SuppressWarnings("deprecation")
	private void notifyRPG(String s, String title, Context context) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(32);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = s; 				// ticker-text
		long when = System.currentTimeMillis();		// notification time
		CharSequence contentTitle = title; 			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent(context, RPGViewList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.sound = Uri.parse(Request.config.getString("ringtonePref", "DEFAULT_NOTIFICATION_URI"));
		if (Request.config.getBoolean("vibration", true)) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(32, notification);
	}

	/*
	 * "XXEventUsernameChange", "XXEventTwitterAnbindung",
	 * "XXEventSelbstbeschreibungAenderung",
	 * "XXEventSelbstbeschreibungErwaehnung", "XXEventEventsDabei",
	 * "XXEventGeburtstag", "XXEventMagDoujinshi", "XXEventMagWeblogeintrag",
	 * "XXEventMagEvent", "XXEventMagFanart", "XXEventMagBastelei",
	 * "XXEventMagFanfic", "XXEventMagFoto", "XXEventMagFotoreihe",
	 * "XXEventMagOekaki", "XXEventMagNews", "XXEventMagWettbewerb",
	 * "XXEventMagManga", "XXEventMagDVD", "XXEventMagArtbook", "XXEventMagCD",
	 * "XXEventMagGame", "XXEventMagAsia", "XXEventMagUmfrage",
	 * "XXEventMagCosplay", "XXEventMagJFashion", "XXEventMagForumThread",
	 * "XXEventMagForumPosting", "XXEventMagZirkelForumThread",
	 * "XXEventMagZirkelForumPosting", "XXEventMagEventVideo",
	 * "XXEventMagFanVideo", "XXEventMagWebshop", "XXEventMagAudiobook",
	 * "XXEventVerrealkontaktet", "XXEventSteckbriefVideoNeu"
	 * -
	 * "XXEventRPGAdminUebergabe", "XXEventRPGBewerbungAngenommen", "XXEventRPGBewerbungAbgelehnt", "XXEventRPGBewerbung", "XXEventRPGAbmeldung",
	 * "XXEventRPGPosting"
	 */

}
