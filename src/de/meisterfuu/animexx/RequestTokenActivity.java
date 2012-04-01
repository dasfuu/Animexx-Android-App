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


public class RequestTokenActivity extends Activity {

	
	
    private OAuthConsumer consumer; 
    private OAuthProvider provider;
    private SharedPreferences config;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	try {
    		consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    		provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,Constants.ACCESS_URL,Constants.AUTHORIZE_URL);
    	} catch (Exception e) {
    		Log.e("OAuth", "Error creating consumer / provider",e);
    	}
    	getRequestToken();
    }

	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent); 
		config = PreferenceManager.getDefaultSharedPreferences(this);
		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i("OAuth", "Callback received : " + uri);
			Log.i("OAuth", "Retrieving Access Token");
			getAccessToken(uri);
		}
	}
	
	private void getRequestToken() {
		try {
			Log.d("OAuth", "getRequestToken() called");
			final RequestTokenActivity temp = this;
			final booleanobject url = new booleanobject();
			url.str = "false";	  	  	
			
			new Thread(new Runnable() {
	  		    public void run() {
					try {
						url.str = provider.retrieveRequestToken(consumer, Constants.OAUTH_CALLBACK_URL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
	  		    	temp.runOnUiThread(new Runnable() {
	  		        public void run() {
	  		        	if(url.str != "false"){
	  					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.str));
	  					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
	  					temp.startActivity(intent);	
	  		        	} else temp.finish();
	  		        }
	  		      });
	  		    }
	  		  }).start();
			

		
		} catch (Exception e) {
			Log.e("OAuth", "Error retrieving request token", e);
		}
	}
	
	private void getAccessToken(Uri uri) {
		final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
		try {
			provider.retrieveAccessToken(consumer, oauth_verifier);

			final Editor edit = config.edit();
			edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
			edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
			edit.commit();
			
			String token = config.getString(OAuth.OAUTH_TOKEN, "");
			String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
			
			consumer.setTokenWithSecret(token, secret);
			this.startActivity(new Intent(this ,AnimexxActivity.class));

			Log.i("OAuth", "Access Token Retrieved");
			
		} catch (Exception e) {
			Log.e("OAuth", "Access Token Retrieval Error", e);
		}
	}
	
}
