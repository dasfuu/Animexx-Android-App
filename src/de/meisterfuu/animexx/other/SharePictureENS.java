package de.meisterfuu.animexx.other;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.KEYS;
import de.meisterfuu.animexx.ENS.ENSAnswer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class SharePictureENS extends Activity{
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();

		// if this is from the share menu
		if (Intent.ACTION_SEND.equals(action))
		{
			if (extras.containsKey(Intent.EXTRA_STREAM))
			{
				try
				{
					// Get resource path from intent call
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

					// Query gallery for camera picture via
					// Android ContentResolver interface
					ContentResolver cr = getContentResolver();
					InputStream is = cr.openInputStream(uri);
					// Get binary bytes for encode
					byte[] data = getBytesFromFile(is);

					// base 64 encode for text transmission (HTTP)
					//byte[] encoded_data = Base64.encodeBase64(data);
					//String data_string = new String(encoded_data); 
					String data_string =  Base64.encodeToString(data, Base64.DEFAULT);
					
					//SendRequest(data_string);
					new SendRequest().execute(data_string);

					return;
				} catch (Exception e)
				{
					Log.e(this.getClass().getName(), e.toString());
				}

			} else if (extras.containsKey(Intent.EXTRA_TEXT))
			{
				return;
			}
		}

	}

	private class SendRequest extends AsyncTask<String, Integer, String[]>
	// private void SendRequest(String data_string)
	{
		private final ProgressDialog dialog = new ProgressDialog(SharePictureENS.this);

		protected void onPreExecute() {
	         this.dialog.setMessage("Uploading photo...");
	         this.dialog.show();
	      }

		@Override
		protected String[] doInBackground(String... data_string) {
			String result[] = new String[2];

			try {
				String datai = data_string[0];

				HttpPost request = new HttpPost("http://api.imgur.com/2/upload.json");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("key", KEYS.IMGUR));
				nameValuePairs.add(new BasicNameValuePair("image", datai));
				nameValuePairs.add(new BasicNameValuePair("type", "base64"));				
				request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				DefaultHttpClient httpclient = new DefaultHttpClient();
				httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"Android App " + Constants.VERSION);
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
				String s = responseBuilder.toString();
				JSONObject a = new JSONObject(s);
				result[0] = "Bild erfolgreich hochgeladen!";
				result[1] = a.getJSONObject("upload").getJSONObject("links").getString("imgur_page");
			} catch (Exception e) {
				e.printStackTrace();
				result[0] = "Hochladen gescheitert!";
				result[1] = "Hochladen gescheitert!";
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			// Update percentage
		}

		@Override
		protected void onPostExecute(String[] result) {
			super.onPostExecute(result);

			this.dialog.dismiss();
			Toast.makeText(SharePictureENS.this, result[0],
					Toast.LENGTH_LONG).show();
			if(result[0].equalsIgnoreCase("Bild erfolgreich hochgeladen!")){
				
				Bundle bundle2 = new Bundle();
				bundle2.putString("msg", "Ich habe etwas interessantes für dich bei Imgur hochgeladen: \n"+result[1]);
				Intent newIntent = new Intent(getApplicationContext(),
						ENSAnswer.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
				
				SharePictureENS.this.finish();
			} else {
				SharePictureENS.this.finish();
			}
		}

	}

	public static byte[] getBytesFromFile(InputStream is) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}

			buffer.flush();

			return buffer.toByteArray();
		} catch (IOException e) {
			Log.e("Animexx", e.toString());
			return null;
		}
	}

}