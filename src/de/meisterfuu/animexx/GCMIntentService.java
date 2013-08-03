package de.meisterfuu.animexx;

import com.google.android.gcm.GCMBaseIntentService;

import de.meisterfuu.animexx.ENS.ENSActivity;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.RPG.RPGViewList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

@SuppressWarnings("deprecation")
public class GCMIntentService extends GCMBaseIntentService {

	NotificationManager mManager;
	public static int rpg = -1;


	public GCMIntentService() {
		super("AnimexxGCM");
	}


	@Override
	protected void onRegistered(Context context, String regId) {
		String registration = regId;
		Log.i("GCM", registration);
		Editor edit = Request.config.edit();
		edit.putString("GCM", registration);
		edit.commit();
		try {
			Request.sendC2DM(registration, "0");
			Request.setC2DM();
			// Request.ENSNotify("Push Test!",
			// "Deine erste gepushte ENS :D Scheint ja alles zu funktionieren!");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	protected void onUnregistered(Context context, String regId) {
		Log.i("GCM", "OnUnregister");
		Editor edit = Request.config.edit();
		edit.putString("GCM", "null");
		edit.commit();
	}


	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i("GCM", "New GCM");
		if (!Helper.isLoggedIn(context)) return;

		Request.config = PreferenceManager.getDefaultSharedPreferences(context);

		if (!Request.config.getBoolean("notification", true)) return;

		if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventENS")) {
			String von = intent.getExtras().getString("from_username");
			String time = intent.getExtras().getString("id");
			notifyENS("Neue ENS von " + von, "Neue ENS", context, 42, time);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventGaestebuch")) {
			String von = intent.getExtras().getString("from_username");
			notifyGB("Neuer Gästebucheintrag von " + von, "Neuer GB Eintrag", context, 43);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventGeburtstag")) {
			String von = intent.getExtras().getString("from_username");
			String url = intent.getExtras().getString("url");
			//notifyURL(von + " hat Geburtstag", "Ein Geburtstag!", context, 44, url);

		} else if (intent.getExtras().getString("type").equalsIgnoreCase("XXEventRPGPosting")) {

			if (!Request.config.getBoolean("rpg_notify", true)) return;
			if (!intent.getExtras().getString("id").equals("" + GCMIntentService.rpg)) {
				notifyRPG("Ein neues Posting im RPG \"" + intent.getExtras().getString("title") + "\"", "Neuer RPG Eintrag", context);
			}
		} else {
			//notifyURL("Es ist etwas passiert, nur was?", "Event 42!", context, 666, "http://animexx.de");
		}
	}


	@Override
	protected void onError(Context context, String errorId) {
		Log.i("GCM", "OnError: " + errorId);
	}


	private void notifyENS(String s, String title, Context context, int id, String time) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(id);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = title; 				// ticker-text
		long when = System.currentTimeMillis(); 	// notification time
		CharSequence contentTitle = title; 			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent(context, ENSActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// the next two lines initialize the Notification, using the
		// configurations above
		if (Helper.HowLongAgo(time) > (3600)) {
			if (Request.GetUnread() == 0) return;
		}
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



	private void notifyGB(String s, String title, Context context, int id) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(id);
		int icon = R.drawable.notification_icon; 	// icon from resources
		CharSequence tickerText = title; 				// ticker-text
		long when = System.currentTimeMillis();		// notification time
		CharSequence contentTitle = title; 			// message title
		CharSequence contentText = s; 				// message text

		Intent intent = new Intent(context, GBViewList.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		if (contentTitle == null) contentTitle = "Gästebuch";
		if (contentText == null) contentText = "Neuer Gästebucheintrag";
		if (tickerText == null) tickerText = "Neuer GB Eintrag";

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


	private void notifyRPG(String s, String title, Context context) {
		mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mManager.cancel(32);
		int icon = R.drawable.ic_notifiy_rpg; 	// icon from resources
		CharSequence tickerText = title; 				// ticker-text
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
		notification.sound = Uri.parse(Request.config.getString("rpg_notify_sound", "DEFAULT_NOTIFICATION_URI"));

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
