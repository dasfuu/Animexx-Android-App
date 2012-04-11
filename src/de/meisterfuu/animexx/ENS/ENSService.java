package de.meisterfuu.animexx.ENS;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class ENSService extends Service {

	private Timer timer;
	long delay = 300000;
	TimerTask Task = new Task(this);
	NotificationManager mManager;
	public SharedPreferences config;

	@Override
	public void onCreate() {
		timer = new Timer("checkens");
		Log.i("Service", "onCreate");
		mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		timer.schedule(Task, 300000, delay);

	}

	@Override
	public void onDestroy() {
		Log.i("Service", "onDestroy");
		Request.doToast("ENS Pulling aus!", getApplicationContext());
		timer.cancel();
		timer.purge();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		Log.i("Service", "onStart");
		Request.doToast("ENS Pulling an!", getApplicationContext());

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void notify(String s) {
		mManager.cancel(42);
		int icon = R.drawable.notification_icon; // icon from resources
		CharSequence tickerText = "Neue ENS"; // ticker-text
		long when = System.currentTimeMillis(); // notification time
		Context context = getApplicationContext(); // application Context
		CharSequence contentTitle = "Neue ENS"; // message title
		CharSequence contentText = s; // message text

		// Intent notificationIntent = new Intent(this, ENSService.class);

		Intent intent = new Intent(this, ENS.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);

		// PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
		// notificationIntent, 0);

		// the next two lines initialize the Notification, using the
		// configurations above
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		mManager.notify(42, notification);
	}

	public class Task extends TimerTask {

		int z;

		@Override
		public void run() {

			z = GetNewUnread(303);
			if (z > 0) {
				plug.notify("Sie haben " + GetUnread() + " neue ENS");
			}
		}

		ENSService plug;

		public Task(ENSService s) {
			plug = s;
		}

		public int GetUnread() {

			String jsonOutput = "";
			try {
				jsonOutput = makeSecuredReq(
						"https://ws.animexx.de/json/ens/anzahl_ungelesen/?api=2",
						getConsumer(config));
				JSONObject jsonResponse = new JSONObject(jsonOutput);
				JSONObject m = (JSONObject) jsonResponse.get("return");
				int anzahl = m.getInt("ungelesen");
				Log.i("Animexx", anzahl + " ungelesene ENS");
				return anzahl;

			} catch (Exception e) {
				Log.e("Animexx", "Error executing request", e);
			}
			return 0;
		}

		public int GetNewUnread(int sec) {

			String jsonOutput = "";
			try {
				jsonOutput = makeSecuredReq(
						"https://ws.animexx.de/json/ens/anzahl_neue_ens/?sekunden="
								+ sec + "&api=2", getConsumer(config));
				JSONObject m = new JSONObject(jsonOutput);
				int anzahl = m.getInt("return");
				// Log.i("Animexx", anzahl+" ungelesene ENS");
				return anzahl;

			} catch (Exception e) {
				Log.e("Animexx", "Error executing request", e);
			}
			return 0;
		}

		private OAuthConsumer getConsumer(SharedPreferences prefs) {
			String token = Request.config.getString(OAuth.OAUTH_TOKEN, "");
			String secret = Request.config.getString(OAuth.OAUTH_TOKEN_SECRET,
					"");
			OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
					Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			consumer.setTokenWithSecret(token, secret);
			return consumer;
		}

		public String makeSecuredReq(String url, OAuthConsumer consumer)
				throws Exception {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			Log.i("Animexx", "Requesting URL : " + url);
			consumer.sign(request);
			HttpResponse response = httpclient.execute(request);
			Log.i("Animexx", "Statusline : " + response.getStatusLine());
			InputStream data = response.getEntity().getContent();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(data));
			String responeLine;
			StringBuilder responseBuilder = new StringBuilder();
			while ((responeLine = bufferedReader.readLine()) != null) {
				responseBuilder.append(responeLine);
			}
			Log.i("Animexx", "Response : " + responseBuilder.toString());
			return responseBuilder.toString();
		}
	}

}