package de.meisterfuu.animexx.Share;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.ENS.ENSAnswer;
import de.meisterfuu.animexx.Home.HomeKontaktNewFragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SharePicture extends SherlockActivity {

	ProgressDialog dialog;

	ImageView img;
	TextView title, url, date;
	Button microblog, ens, browser;

	String url_share, url_direct, mime, filename;
	long id;

	int after;

	private ShareActionProvider mShareActionProvider;

	private Uri imageUri;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);

		setContentView(R.layout.activity_share_picture);

		img = (ImageView) this.findViewById(R.id.share_image_image);

		title = (TextView) this.findViewById(R.id.share_image_title);
		url = (TextView) this.findViewById(R.id.share_image_URL);
		date = (TextView) this.findViewById(R.id.share_image_date);

		microblog = (Button) this.findViewById(R.id.share_image_shortblog);
		browser = (Button) this.findViewById(R.id.share_image_browser);
		ens = (Button) this.findViewById(R.id.share_image_ENS);

		ens.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putString("msg", "\n" + url_share + "\n");
				Intent newIntent = new Intent(getApplicationContext(), ENSAnswer.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
				SharePicture.this.finish();
			}

		});

		microblog.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Bundle bundle2 = new Bundle();
				bundle2.putString("url", imageUri.toString());
				bundle2.putString("id", id + "");
				Intent newIntent = new Intent(getApplicationContext(), HomeKontaktNewFragment.class);
				newIntent.putExtras(bundle2);
				startActivity(newIntent);
				SharePicture.this.finish();
			}

		});

		browser.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Uri uri = Uri.parse(url_share);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}

		});

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String action = intent.getAction();
		String type = intent.getType();

		// if this is from the share menu
		if (Intent.ACTION_SEND.equals(action)) {
			if (extras.containsKey(Intent.EXTRA_STREAM)) {
				if (type.startsWith("image/")) {
					try {
						// Get resource path from intent call
						final Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);

						microblog.setOnClickListener(new OnClickListener() {

							public void onClick(View v) {
								Bundle bundle2 = new Bundle();
								bundle2.putString("uri", uri.toString());
								bundle2.putString("id", id + "");
								Intent newIntent = new Intent(getApplicationContext(), HomeKontaktNewFragment.class);
								newIntent.putExtras(bundle2);
								startActivity(newIntent);
								SharePicture.this.finish();
							}

						});

						// Query gallery for camera picture via
						// Android ContentResolver interface
						ContentResolver cr = getContentResolver();
						InputStream is = cr.openInputStream(uri);

						// Get binary bytes for encode
						byte[] data = getBytesFromFile(is);

						// Show Picture
						BitmapFactory.Options options = new BitmapFactory.Options();
						Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
						img.setImageBitmap(bm);

						upload(data, uri.getLastPathSegment());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (extras.containsKey(Intent.EXTRA_TEXT)) {
				this.finish();
			}
		} else if (this.getIntent().hasExtra("req_code")) {
			after = 1;
			if (this.getIntent().getExtras().getString("req_code").equals("1")) {
				request_gal();
			} else {
				request_cam();
			}
		}

	}


	private void after() {
		if (after == 1) {
			Bundle bundle2 = new Bundle();
			bundle2.putString("url", imageUri.toString());
			bundle2.putString("id", id + "");
			Intent newIntent = new Intent(getApplicationContext(), HomeKontaktNewFragment.class);
			newIntent.putExtras(bundle2);
			startActivity(newIntent);
			SharePicture.this.finish();
		}
	}

	private static final int REQUEST_CODE_GALLERY = 1;
	private static final int REQUEST_CODE_CAMERA = 2;


	public void request_gal() {
		// define the file-name to save photo taken by Camera activity
		String fileName = "animexx_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
		// create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaColumns.TITLE, fileName);
		values.put(ImageColumns.DESCRIPTION, "Animexx App");
		// imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		Intent it = new Intent();
		it.setType("image/*");
		it.setAction(Intent.ACTION_GET_CONTENT);
		it.addCategory(Intent.CATEGORY_OPENABLE);
		it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(it, REQUEST_CODE_GALLERY);
	}


	public void request_cam() {
		// define the file-name to save photo taken by Camera activity
		String fileName = "animexx_" + Calendar.getInstance().getTimeInMillis() + ".jpg";
		// create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaColumns.TITLE, fileName);
		values.put(ImageColumns.DESCRIPTION, "Animexx App");
		// imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		// create new Intent
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, REQUEST_CODE_CAMERA);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Calendar c = Calendar.getInstance();
		String name = "" + c.getTimeInMillis();
		if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
			try {
				InputStream is = getContentResolver().openInputStream(data.getData());
				byte[] fdata = getBytesFromFile(is);
				is.close();
				// Show Picture
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 3;
				Bitmap bm = BitmapFactory.decodeByteArray(fdata, 0, fdata.length, options);
				img.setImageBitmap(bm);
				upload(fdata, name);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
			try {

				// Query gallery for camera picture via
				// Android ContentResolver interface
				ContentResolver cr = getContentResolver();
				InputStream is = cr.openInputStream(imageUri);

				// Get binary bytes for encode
				byte[] fdata = getBytesFromFile(is);

				// Show Picture
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bm = BitmapFactory.decodeByteArray(fdata, 0, fdata.length, options);
				img.setImageBitmap(bm);

				upload(fdata, imageUri.getLastPathSegment());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.menu_share_image, menu);

		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();

		return super.onCreateOptionsMenu(menu);
	}


	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}


	public void upload(byte[] data, String name) {
		try {
			// base 64 encode for text transmission (HTTP)
			// byte[] encoded_data = Base64.encodeBase64(data);
			// String data_string = new String(encoded_data);
			String data_string = Base64.encodeToString(data, Base64.DEFAULT);

			// SendRequest(data_string);
			final HttpPost request = new HttpPost("https://ws.animexx.de/json/mitglieder/files/upload/?api=2");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("filename", "/Android/" + name));
			nameValuePairs.add(new BasicNameValuePair("data", data_string));
			nameValuePairs.add(new BasicNameValuePair("type", "base64"));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			dialog = new ProgressDialog(this);
			dialog.setMessage("Hochladen...");
			dialog.setCancelable(false);
			dialog.show();

			new Thread(new Runnable() {

				public void run() {

					try {
						String s = Request.SignSend(request);
						JSONObject ob = new JSONObject(s);
						url_share = ob.getJSONObject("return").getString("url_share");
						url_direct = ob.getJSONObject("return").getString("url_intern");
						mime = ob.getJSONObject("return").getString("mime");
						id = ob.getJSONObject("return").getInt("id");
						filename = ob.getJSONObject("return").getString("filename");

						SharePicture.this.runOnUiThread(new Runnable() {

							public void run() {
								dialog.dismiss();
								url.setText(url_share);
								title.setText(filename);
								date.setText(mime);
								Intent i = new Intent(Intent.ACTION_SEND);
								i.setType("text/plain");
								i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
								i.putExtra(Intent.EXTRA_TEXT, url_share);
								setShareIntent(i);
								Toast.makeText(SharePicture.this, "Hochgeladen!", Toast.LENGTH_LONG).show();

								after();
							}

						});

					} catch (Exception e) {
						SharePicture.this.runOnUiThread(new Runnable() {

							public void run() {
								dialog.dismiss();
								Toast.makeText(SharePicture.this, "Error!", Toast.LENGTH_LONG).show();
								SharePicture.this.finish();
							}

						});
						e.printStackTrace();
					}

				}

			}).start();

			return;
		} catch (Exception e) {
			e.printStackTrace();
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