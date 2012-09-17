package de.meisterfuu.animexx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;

import de.meisterfuu.animexx.ENS.ENSObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;
import android.widget.Toast;

public class Request {

	public static SharedPreferences config;


	public static void FetchMe() throws Exception {
		String jsonOutput = "";
		jsonOutput = makeSecuredReq("https://ws.animexx.de/json/mitglieder/ich/?api=2");
		JSONObject jsonResponse = new JSONObject(jsonOutput);
		String id = ((JSONObject) jsonResponse.get("return")).getString("id");
		String username = ((JSONObject) jsonResponse.get("return")).getString("username");
		Editor edit = Request.config.edit();
		edit.putString("id", id);
		edit.putString("username", username);
		edit.commit();
	}


	public static int GetUnread() {

		try {
			String jsonOutput = "";
			jsonOutput = makeSecuredReq("https://ws.animexx.de/json/ens/anzahl_ungelesen/?api=2");
			JSONObject jsonResponse = new JSONObject(jsonOutput);
			JSONObject m = (JSONObject) jsonResponse.get("return");
			int anzahl = m.getInt("ungelesen");
			Log.i("Animexx", anzahl + " ungelesene ENS");
			return anzahl;
		} catch (Exception e) {
			return -1;
		}

	}


	public static ENSObject GetENS(long id) throws Exception {

		String jsonOutput = "";
		jsonOutput = makeSecuredReq("https://ws.animexx.de/json/ens/ens_open/?ens_id=" + id + "&text_format=html&api=2");
		JSONObject jsonResponse = new JSONObject(jsonOutput);
		JSONObject m = (JSONObject) jsonResponse.get("return");
		ENSObject temp = new ENSObject();
		temp.parseJSON(m);
		return temp;
	}


	public static boolean RemoveENS(Long id, String anvon) {
		try {
			JSONObject jsonResponse = new JSONObject(makeSecuredReq("https://ws.animexx.de/json/ens/ens_move/?ens_ids[]=" + id + "&vonan=" + anvon + "&zielordner=3&api=2"));
			if (jsonResponse.getBoolean("success") == true)
				return true;
			else
				return false;

		} catch (Exception e) {
			return false;
		}

	}


	public static int[] GetUser(ArrayList<String> Names) throws Exception {

		String jsonOutput = "";
		String url;
		if (Names.size() == 0) return null;
		url = "https://ws.animexx.de/json/ens/an_check/?api=2";

		String s = "";
		s += "dummy=dummy";
		for (int i = 0; i < Names.size(); i++)
			s += "&users[]=" + Names.get(i);
		StringEntity se = new StringEntity(s);
		se.setContentType("application/x-www-form-urlencoded");
		HttpPost request = new HttpPost(url);
		request.setEntity(se);

		jsonOutput = SignSend(request);

		JSONObject jsonResponse = new JSONObject(jsonOutput);
		JSONObject m = (JSONObject) jsonResponse.get("return");
		if (m.getJSONArray("errors").length() > 0) return null;

		JSONArray erg = m.getJSONArray("user_ids");
		int[] ens = new int[erg.length()];

		for (int i = 0; i < erg.length(); i++) {
			ens[i] = erg.getInt(i);
		}
		return ens;
	}


	public static OAuthConsumer getConsumer() {
		String token = config.getString(OAuth.OAUTH_TOKEN, "");
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		consumer.setTokenWithSecret(token, secret);
		return consumer;
	}


	public static String makeSecuredReq(String url) throws Exception {
		// url = URLEncoder.encode(url, "ISO-8859-1");

		HttpGet request = new HttpGet(url);
		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
		/*
		 * OAuthConsumer consumer = getConsumer(); DefaultHttpClient httpclient
		 * = new DefaultHttpClient(); consumer.sign(request); HttpResponse
		 * response = httpclient.execute(request);
		 * Log.i("Animexx","Statusline : " + response.getStatusLine());
		 * InputStream data = response.getEntity().getContent(); BufferedReader
		 * bufferedReader = new BufferedReader(new InputStreamReader(data));
		 * String responeLine; StringBuilder responseBuilder = new
		 * StringBuilder(); while ((responeLine = bufferedReader.readLine()) !=
		 * null) { responseBuilder.append(responeLine); }
		 * Log.i("Animexx","Response : " + responseBuilder.toString()); return
		 * responseBuilder.toString();
		 */
	}


	public static HttpGet getHTTP(String url) throws Exception {

		HttpGet request = new HttpGet(url);
		return request;

	}


	public static boolean sendENS(String Betreff, String Text, String Signatur, int[] an, long referenz_id) throws Exception {
		String url = "https://ws.animexx.de/json/ens/ens_senden/?api=2";
		HttpPost request = new HttpPost(url);

		String s = "";
		s += "betreff=" + Betreff;
		s += "&text=" + OAuth.percentEncode(Text);
		s += "&sig=" + Signatur;
		for (int i = 0; i < an.length; i++)
			s += "&an_users[]=" + an[i];
		if (referenz_id != -1) {
			s += "&referenz_typ=reply";
			s += "&referenz_id=" + referenz_id;
		}
		StringEntity se = new StringEntity(s);
		se.setContentType("application/x-www-form-urlencoded");
		request.setEntity(se);

		Log.i("Animexx", "Requesting URL : " + url);

		String jsonOutput = SignSend(request);

		JSONObject jsonResponse = new JSONObject(jsonOutput);
		Boolean m = jsonResponse.getBoolean("success");

		return m;
	}


	public static boolean sendENS(String Betreff, String Text, String Signatur, String an, String referenz_id) {
		try {
			String url = "https://ws.animexx.de/json/ens/ens_senden/?api=2" + an;
			HttpPost request = new HttpPost(url);

			String s = "";
			s += "betreff=" + Betreff;
			s += "&text=" + OAuth.percentEncode(Text);
			s += "&sig=" + Signatur;
			if (referenz_id != "-1") {
				s += "&referenz_typ=reply";
				s += "&referenz_id=" + referenz_id;
			}
			StringEntity se = new StringEntity(s);
			se.setContentType("application/x-www-form-urlencoded");
			request.setEntity(se);

			Log.i("Animexx", "Requesting URL : " + url);

			SignSend(request);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	public static HttpPost sendGB(String Text, String an) {
		try {
			String url = "https://ws.animexx.de/json/mitglieder/gaestebuch_schreiben/?api=2&user_id=" + an;
			HttpPost request = new HttpPost(url);

			String s = "text=" + OAuth.percentEncode(Text);

			StringEntity se = new StringEntity(s);
			se.setContentType("application/x-www-form-urlencoded");
			request.setEntity(se);

			return request;
		} catch (Exception e) {
			return null;
		}
	}


	/*
	  	- charakter, int / benötigt / Die ID des Charakters
		- text, string / benötigt / Der Text
		- avatar, int / optional / Die ID des Avatars. Kann nur bei Vertofuzierten RPGs angegeben werden. (wenn /get_meine_charaktere/ Avatare zurückgegeben hat)
		- kursiv, int / optional / Falls angegeben und "1", dann wird das Posting kursiv.
		- intime, int / optional / Falls angegeben und ungleich "1", dann wird es ein Out-Time-Posting. Standard: In-Time.
	 */

	public static HttpPost sendRPGPost(String Text, long RPGid, boolean InTime, boolean Aktion, long CharaID, long AvatarID) {
		try {
			int Time = 0;
			int Akt = 0;
			if(Aktion) Akt = 1;
			if(InTime) Time = 1;

			String url = "https://ws.animexx.de/json/rpg/erstelle_posting/?api=2&rpg=" + RPGid+"&charakter="+CharaID+"&kursiv="+Akt+"&intime="+Time;
			if(AvatarID != -1) url += "&avatar="+AvatarID;

			HttpPost request = new HttpPost(url);

			String s = "text=" + OAuth.percentEncode(Text);

			StringEntity se = new StringEntity(s);
			se.setContentType("application/x-www-form-urlencoded");
			request.setEntity(se);

			return request;
		} catch (Exception e) {
			return null;
		}
	}

	public static HttpPost sendRPGPost(String Text, long RPGid, boolean InTime, boolean Aktion, long CharaID) {
		return sendRPGPost(Text, RPGid, InTime, Aktion, CharaID, -1);
	}




	public static String sendC2DM(String id, String collapse) throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/registration_id_set/?api=2";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("registration_id", id));
		nameValuePairs.add(new BasicNameValuePair("collapse_by_type", collapse));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}


	public static String setC2DM() throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/set_active_events/?api=2";
		HttpPost request = new HttpPost(url);

		// String s="dummy=dummy";
		// s += "&events[]=XXEventENS";
		// s += "&events[]=XXEventGeburtstag";
		// s += "&events[]=XXEventGaestebuch";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventENS"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventGeburtstag"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventGaestebuch"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventRPGPosting"));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// StringEntity se = new StringEntity(s);
		// se.setContentType("application/x-www-form-urlencoded");

		// request.setEntity(se);

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}


	public static String delsendC2DM(String id) throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/registration_id_del/?api=2";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("registration_id", id));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}


	public static void doToast(String s, Context c) {
		CharSequence text = s;
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(c, text, duration);
		toast.show();
	}


	public static int GetNewUnread(int sec) throws Exception {

		String jsonOutput = "";
		jsonOutput = makeSecuredReq("https://ws.animexx.de/json/ens/anzahl_neue_ens/?sekunden=" + sec + "&api=2");
		JSONObject jsonResponse = new JSONObject(jsonOutput);
		JSONObject m = (JSONObject) jsonResponse.get("return");
		int anzahl = m.getInt("ungelesen");
		Log.i("Animexx", anzahl + " ungelesene ENS");
		return anzahl;
	}


	public static boolean checkservice(Context z) {

		final ActivityManager activityManager = (ActivityManager) z.getSystemService("activity");
		final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

		for (int i = 0; i < services.size(); i++) {

			if ("de.meisterfuu.animexx".equals(services.get(i).service.getPackageName())) {
				Log.d("Service", "packagename stimmt überein !!!");

				if ("de.meisterfuu.animexx.ENS.ENSService".equals(services.get(i).service.getClassName())) {
					Log.d("Service", "getClassName stimmt überein !!!");

					return true;
				}
			}
		}
		return false;
	}


	public static boolean checkpush(Context con) {
		try {
			String jsonOutput = makeSecuredReq("https://ws.animexx.de/json/cloud2device/registration_id_get/?api=2");
			String jsonResponse = new JSONObject(jsonOutput).getJSONObject("return").getJSONArray("registration_ids").getString(0);
			Log.i("GCM-Server", jsonResponse);

			GCMRegistrar.checkDevice(con);
			GCMRegistrar.checkManifest(con);
			final String regId = GCMRegistrar.getRegistrationId(con);
			Log.i("GCM-Device", regId);

			if (jsonResponse.equals(regId)) {
				Log.i("GCM", "Active");
				return true;
			} else {
				Log.i("GCM", "Inactive");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}


	public static void ENSNotify(String betreff, String text) {
		int[] an = new int[1];
		an[0] = Integer.parseInt(Request.config.getString("id", "-1"));
		Log.i("Animexx", "ENSNotify");
		try {
			Request.sendENS(betreff, text, "Android Notify", an, -1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public static String SignSend(HttpRequestBase r) throws Exception {
		OAuthConsumer consumer = getConsumer();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android App " + Constants.VERSION);
		consumer.sign(r);
		Log.i("Animexx", "Request : " + r.getURI());
		HttpResponse response = httpclient.execute(r);
		Log.i("Animexx", "Statusline : " + response.getStatusLine());
		InputStream data = response.getEntity().getContent();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
		String responeLine;
		StringBuilder responseBuilder = new StringBuilder();
		while ((responeLine = bufferedReader.readLine()) != null) {
			responseBuilder.append(responeLine);
		}
		Log.i("Animexx", "Response : " + responseBuilder.toString());
		String s = responseBuilder.toString();

		return s;
	}

	// HttpPost request = new HttpPost(url);
	// HttpParameters para = new HttpParameters();
	// para.put("msg", msg)
}
