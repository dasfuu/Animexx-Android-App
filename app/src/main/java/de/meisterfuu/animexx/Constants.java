package de.meisterfuu.animexx;

public class Constants {

	// OAuth
	public static final String CONSUMER_KEY = KEYS.CONSUMER_KEY;
	public static final String CONSUMER_SECRET = KEYS.CONSUMER_SECRET;

	public static final String REQUEST_URL = "https://ws.animexx.de/oauth/request_token";
	public static final String ACCESS_URL = "https://ws.animexx.de/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://ssl.animexx.de/oauth/authorize";

	public static final String OAUTH_CALLBACK_SCHEME = "animexx";
	public static final String OAUTH_CALLBACK_HOST = "callback/";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME
			+ "://" + OAUTH_CALLBACK_HOST;

	// Other
	public static final String LOADING = "Daten werden abgerufen...";
	public static final String VERSION = "v1.8.5";

}
