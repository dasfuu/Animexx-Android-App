package de.meisterfuu.animexx;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class RequestTokenActivity extends Activity {

	private OAuthConsumer consumer;
	private OAuthProvider provider;
	private SharedPreferences config;
	private TextView LadeMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_animation);
		LadeMessage = (TextView) findViewById(R.id.login_status);

		try {
			consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET);
			provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
					Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
			LadeMessage.setText("Starte OAuth...");
		} catch (Exception e) {
			Log.e("OAuth", "Error creating consumer / provider", e);
		}
		getRequestToken();
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		config = PreferenceManager.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i("OAuth", "Callback received : " + uri);
			Log.i("OAuth", "Retrieving Access Token");
			LadeMessage.setText("Login abgeschlossen! :)");
			getAccessToken(uri);
		}
	}

	private void getRequestToken() {
		try {
			Log.d("OAuth", "getRequestToken() called");
			LadeMessage.setText("Auf Animexx Server warten...");
			final RequestTokenActivity temp = this;
			final booleanobject url = new booleanobject();
			url.str = "false";

			new Thread(new Runnable() {
				public void run() {
					try {
						url.str = provider.retrieveRequestToken(consumer,
								Constants.OAUTH_CALLBACK_URL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						url.str = "false";
					}
					temp.runOnUiThread(new Runnable() {
						public void run() {
							if (url.str != "false") {
								LadeMessage.setText("Browser starten...");
								Intent intent = new Intent(Intent.ACTION_VIEW,
										Uri.parse(url.str));
								intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
										| Intent.FLAG_ACTIVITY_NO_HISTORY
										| Intent.FLAG_FROM_BACKGROUND);
								temp.startActivity(intent);
							} else
								temp.LadeMessage.setText("Verbindung zum Animexx Server gescheitert!");
						}
					});
				}
			}).start();

		} catch (Exception e) {
			Log.e("OAuth", "Error retrieving request token", e);
		}
	}

	private void getAccessToken(Uri uri) {
		final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER).trim();
		try {


			final RequestTokenActivity temp = this;
			new Thread(new Runnable() {
				public void run() {
					try {
						provider.retrieveAccessToken(consumer, oauth_verifier);
					} catch (Exception e) {
						temp.runOnUiThread(new Runnable() {
							public void run() {
								LadeMessage.setText("Fehler beim verarbeiten des AccessTokens :(");
							}
						});
					}
					temp.runOnUiThread(new Runnable() {
						public void run() {
							final Editor edit = config.edit();
							edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
							edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
							edit.commit();

							String token = config.getString(OAuth.OAUTH_TOKEN, "");
							String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");

							consumer.setTokenWithSecret(token, secret);
							temp.startActivity(new Intent(temp, LoginActivity.class));
							Log.i("OAuth", "Access Token Retrieved");
							temp.finish();
						}
					});
				}
			}).start();


		} catch (Exception e) {
			LadeMessage.setText("Fehler beim verarbeiten des AccessTokens :(");
			Log.e("OAuth", "Access Token Retrieval Error", e);
			Log.e("OAuth", oauth_verifier, e);

		}
	}

}
