package de.meisterfuu.animexx.Home;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import oauth.signpost.OAuth;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.Share.SharePicture;
import de.meisterfuu.animexx.other.ImageDownloader;

@SuppressLint("SimpleDateFormat")
public class HomeKontaktNewFragment extends SherlockActivity {

	ArrayList<HomeKontaktObject> Array = new ArrayList<HomeKontaktObject>();

	Context context;
	ImageDownloader Images = new ImageDownloader();
	ProgressDialog dialog;

	TextView msg;
	ImageView img;
	Button send;
	ImageButton btcamera, btimage;

	String id;



	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);

		setContentView(R.layout.home_kontakt_new);

		msg = (TextView) findViewById(R.id.fragment_microblog_text);
		msg.setText(Request.config.getString("microblog_temp", ""));

		img = (ImageView) findViewById(R.id.fragment_microblog_img);
		img.setVisibility(View.GONE);
		send = (Button) findViewById(R.id.fragment_microblog_send);
		send.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					send();
				} catch (UnsupportedEncodingException e) {
					Toast.makeText(HomeKontaktNewFragment.this, "Senden gescheitert: UnsupportedEncodingException", Toast.LENGTH_LONG).show();
				}
			}
		});

		btcamera = (ImageButton) findViewById(R.id.fragment_microblog_camera);
		btcamera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putString("req_code", "2");
				Intent newIntent = new Intent(getApplicationContext(), SharePicture.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
				Request.config.edit().putString("microblog_temp", msg.getText().toString()).commit();
				HomeKontaktNewFragment.this.finish();
			}
		});

		btimage = (ImageButton) findViewById(R.id.fragment_microblog_image);
		btimage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putString("req_code", "1");
				Intent newIntent = new Intent(getApplicationContext(), SharePicture.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
				Request.config.edit().putString("microblog_temp", msg.getText().toString()).commit();
				HomeKontaktNewFragment.this.finish();
			}
		});

		if (this.getIntent().hasExtra("id")) {
			id = this.getIntent().getStringExtra("id");
			try {
				String url = this.getIntent().getStringExtra("url");
				// Query gallery for camera picture via
				// Android ContentResolver interface
				ContentResolver cr = getContentResolver();
				InputStream is;
				is = cr.openInputStream(Uri.parse(url));
				// Get binary bytes for encode
				byte[] data = getBytesFromFile(is);

				// Show Picture
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
				img.setImageBitmap(bm);
				img.setVisibility(View.VISIBLE);
			} catch (Exception e) {
				img.setVisibility(View.GONE);
			}

		} else {

		}

	}


	public void send() throws UnsupportedEncodingException {

		dialog = new ProgressDialog(this);
		dialog.setMessage("Hochladen...");
		dialog.setCancelable(false);
		dialog.show();

		final HttpPost request = new HttpPost("https://ws.animexx.de/json/persstart5/post_microblog_message/?api=2");

		String s = "";
		s += "text=" + OAuth.percentEncode("" + msg.getText());
		if (id != null) {
			s += "&attach_foto=" + id;
		}

		StringEntity se = new StringEntity(s);
		se.setContentType("application/x-www-form-urlencoded");
		request.setEntity(se);

		new Thread(new Runnable() {

			public void run() {

				try {
					String s = Request.SignSend(request);
					JSONObject ob = new JSONObject(s).getJSONObject("return");

					HomeKontaktNewFragment.this.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Toast.makeText(HomeKontaktNewFragment.this, "Gesendet!", Toast.LENGTH_LONG).show();
							Request.config.edit().remove("microblog_temp").commit();
							HomeKontaktNewFragment.this.finish();
						}

					});
				} catch (Exception e) {
					HomeKontaktNewFragment.this.runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Toast.makeText(HomeKontaktNewFragment.this, "Error!", Toast.LENGTH_LONG).show();
							HomeKontaktNewFragment.this.finish();
						}

					});
					e.printStackTrace();
				}

			}

		}).start();

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