package de.meisterfuu.animexx.fanworks.fanart;

import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;

import de.meisterfuu.animexx.fanworks.FanworkHelper;
import de.meisterfuu.animexx.fanworks.FanworkObject;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.ImageSaveObject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Fanart extends SherlockActivity  {

	ImageView img;
	TextView title, author, date;
	WebView desc;
	ImageDownloaderCustom img_loeader;
	
	FanworkObject fanart;
	long fanwork_id;
	ShareActionProvider mShareActionProvider;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Helper.isLoggedIn(this);
		setContentView(R.layout.activity_fanart);
		
		title = (TextView) this.findViewById(R.id.home_kontakt_detail_title);
		author = (TextView) this.findViewById(R.id.home_kontakt_detail_info);
		date = (TextView) this.findViewById(R.id.home_kontakt_detail_date);
		desc = (WebView) this.findViewById(R.id.webView1);
		img = (ImageView) this.findViewById(R.id.fragment_microblog_img);
		img.setVisibility(View.GONE);

		img_loeader = new ImageDownloaderCustom("fanart");
		
		try{
			fanwork_id = this.getIntent().getExtras().getLong("fanwork_id");
			getFanwork();
		} catch (Exception e) {
			this.finish();
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();

	}

	public void getFanwork() {
		final SherlockActivity temp = this;
		new Thread(new Runnable() {

			public void run() {
				try {
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/fanworks/get_fanwork_data/?api=2&fanwork_class="+FanworkHelper.FANART+"&fanwork_id="+fanwork_id+"&img_max_x=2000&img_max_y=2000&img_format=jpg&img_quality=95");
					
					JSONObject jsonResponse = new JSONObject(JSON);
					fanart = FanworkObject.parseJSON(jsonResponse.getJSONObject("return"));


					temp.runOnUiThread(new Runnable() {

						public void run() {		
							if(fanart.getItem_image_big() != null){
								ImageSaveObject imgObj = new ImageSaveObject(fanart.getItem_image_big(), fanart.getId()+"_big");
								img_loeader.download(imgObj, img, true);
								img.setVisibility(View.VISIBLE);
							} else {
								img.setVisibility(View.GONE);
							}
							title.setText(fanart.getTitle());
							desc.loadDataWithBaseURL("http://www.animexx.de/", fanart.getBeschreibung(), "text/html", "UTF-8", null);
							author.setText(fanart.getVon().getUsername());
							date.setText(fanart.getDate_server());
							Intent i = new Intent(Intent.ACTION_SEND);
							i.setType("text/plain");
							i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
							i.putExtra(Intent.EXTRA_TEXT, "http://www.animexx.de/fanart/"+fanwork_id+"/");
							setShareIntent(i);
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
					temp.finish();
				}
			}
		}).start();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.fanart, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.menu_item_share_fanart);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) item.getActionProvider();

		return super.onCreateOptionsMenu(menu);
	}


	private void setShareIntent(Intent shareIntent) {
		if (mShareActionProvider != null) {
			mShareActionProvider.setShareIntent(shareIntent);
		}
	}

}
