package de.meisterfuu.animexx.other;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.ENS.ENSAnswer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

public class SharePictureENS extends Activity {

	ProgressDialog dialog;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();
		dialog = new ProgressDialog(this);

		// if this is from the share menu
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				try {
					// Get resource path from intent call
					Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

					// Query gallery for camera picture via
					// Android ContentResolver interface
					ContentResolver cr = getContentResolver();
					InputStream is = cr.openInputStream(uri);
					// Get binary bytes for encode
					byte[] data = getBytesFromFile(is);

					// base 64 encode for text transmission (HTTP)
					// byte[] encoded_data = Base64.encodeBase64(data);
					// String data_string = new String(encoded_data);
					String data_string = Base64.encodeToString(data, Base64.DEFAULT);

					// SendRequest(data_string);
					final HttpPost request = new HttpPost("https://ws.animexx.de/json/mitglieder/files/upload/?api=2");

					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
					nameValuePairs.add(new BasicNameValuePair("filename", "/Android/" + uri.getLastPathSegment()));
					nameValuePairs.add(new BasicNameValuePair("data", data_string));
					nameValuePairs.add(new BasicNameValuePair("type", "base64"));
					request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					dialog.setMessage("Uploading photo...");
					dialog.show();

					final SharePictureENS temp = this;

					new Thread(new Runnable() {

						public void run() {
							
							try {
								Request.SignSend(request);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}


							temp.runOnUiThread(new Runnable() {

								public void run() {
									dialog.dismiss();
									Toast.makeText(temp, "Hochgeladen!", Toast.LENGTH_LONG).show();

									Bundle bundle2 = new Bundle();
									bundle2.putString("msg", "Ich habe etwas interessantes für dich bei Imgur hochgeladen: \n" + "URL");
									Intent newIntent = new Intent(getApplicationContext(), ENSAnswer.class);
									newIntent.putExtras(bundle2);
									startActivity(newIntent);
								}

							});
						}

					}).start();
					



					return;
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
				return;
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