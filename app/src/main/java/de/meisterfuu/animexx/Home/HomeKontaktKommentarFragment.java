package de.meisterfuu.animexx.Home;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import oauth.signpost.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.ImageDownloader;


public class HomeKontaktKommentarFragment extends SherlockFragment implements Refreshable {

	ArrayList<HomeKontaktKommentarObject> Array = new ArrayList<HomeKontaktKommentarObject>();

	Context context;
	ProgressDialog dialog;
	Boolean loading, loaded = false;
	HomeKontaktKommentarAdapter adapter;
	ListView lv;
	ImageDownloader Images = new ImageDownloader();
	int loadCount = 0;
	RelativeLayout Loading;
	String id;

	
	TextView msg;
	Button send;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_comment_list, null);
		Loading = (RelativeLayout) view.findViewById(R.id.RPGloading);
		Loading.setVisibility(View.GONE);
		context = this.getActivity();
		
		msg = (TextView) view.findViewById(R.id.home_comment_msg);
		send = (Button) view.findViewById(R.id.home_comment_send);
		
		send.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				if(msg.getText().length() > 0){
					send(msg.getText().toString());
				}				
			}
			
		});

		adapter = new HomeKontaktKommentarAdapter(this, Array);
		adapter.Images = Images;
		lv = (ListView) view.findViewById(R.id.HomeListView);
		lv.setAdapter(adapter);

		if (!loaded) {
			loaded = true;
			if (getArguments() != null && !getArguments().isEmpty()) {
				id = new String(getArguments().getCharArray("data"));			
			} 
			refresh();
			
		}

		return view;
	}


	public void send(String message) {


		final ProgressDialog dialog = new ProgressDialog(this.getActivity());
		dialog.setMessage("Senden...");
		dialog.setCancelable(false);
		dialog.show();

		final HttpPost request = new HttpPost("https://ws.animexx.de/json/persstart5/post_item_kommentar/?api=2&item_id="+id+"&widget_id=kontakte");

		String s = "";
		s += "text=" + OAuth.percentEncode("" + message);
		if (id != null) {
			s += "&attach_foto=" + id;
		}

		try {
			StringEntity se = new StringEntity(s);
			se.setContentType("application/x-www-form-urlencoded");
			request.setEntity(se);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			Toast.makeText(HomeKontaktKommentarFragment.this.getActivity(), "Error!", Toast.LENGTH_LONG).show();	
			return;
		}

		
		
		new Thread(new Runnable() {

			public void run() {
				
				try {
					String s = Request.SignSend(request);
					JSONObject ob = new JSONObject(s).getJSONObject("return");
	
					
					HomeKontaktKommentarFragment.this.getActivity().runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							msg.setText("");
							refresh();
							Toast.makeText(HomeKontaktKommentarFragment.this.getActivity(), "Gesendet!", Toast.LENGTH_LONG).show();
						}

					});
				} catch (Exception e) {
					HomeKontaktKommentarFragment.this.getActivity().runOnUiThread(new Runnable() {

						public void run() {
							dialog.dismiss();
							Toast.makeText(HomeKontaktKommentarFragment.this.getActivity(), "Error!", Toast.LENGTH_LONG).show();							
						}

					});
					e.printStackTrace();
				}



			}

		}).start();
		
	}


	public static HomeKontaktKommentarFragment newInstance(String data) {
		HomeKontaktKommentarFragment myFragment = new HomeKontaktKommentarFragment();

		Bundle args = new Bundle();
		args.putCharArray("data", data.toCharArray());
		myFragment.setArguments(args);

		return myFragment;
	}


	public static HomeKontaktKommentarFragment newInstance() {
		HomeKontaktKommentarFragment myFragment = new HomeKontaktKommentarFragment();

		return myFragment;
	}


	/*
	 * public boolean onKeyUp(int keyCode, KeyEvent event) {
	 * if (keyCode == KeyEvent.KEYCODE_MENU) {
	 * Filter.animateToggle();
	 * return true;
	 * }
	 * return super.onKeyUp(keyCode, event);
	 * }
	 */

	public void DoError() {
		Request.doToast("Fehler", this.getActivity());
	}


	public void LoadPlus() {
		loadCount++;
		Loading.setVisibility(View.VISIBLE);
	}


	public void LoadMinus() {
		loadCount--;
		if (loadCount == 0) {
			Loading.setVisibility(View.GONE);
			//Collections.sort(Array);
		}
	}


	public void refresh() {

		Array = new ArrayList<HomeKontaktKommentarObject>();
		getKontakt(Array);

	}


	public void getKontakt(final ArrayList<HomeKontaktKommentarObject> Liste) {
		LoadPlus();
		new Thread(new Runnable() {

			public void run() {
				try {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.WEEK_OF_YEAR, -1);
					final String JSON = Request.makeSecuredReq("https://ws.animexx.de/json/persstart5/get_widget_kommentare/?api=2&widget_id=kontakte&item_id="+id);
					JSONArray list = null;

					JSONObject jsonResponse = new JSONObject(JSON);
					list = jsonResponse.getJSONArray("return");

					if (list.length() != 0) {
						for (int i = 0; i < list.length(); i++) {
							HomeKontaktKommentarObject tempObject = new HomeKontaktKommentarObject(list.getJSONObject(i));
							Liste.add(tempObject);
						}
					}

					HomeKontaktKommentarFragment.this.getActivity().runOnUiThread(new Runnable() {

						public void run() {
							//Collections.sort(Array);
							adapter = new HomeKontaktKommentarAdapter(HomeKontaktKommentarFragment.this, Array);
							adapter.Images = Images;
							lv.setAdapter(adapter);
							adapter.notifyDataSetChanged();
							LoadMinus();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

}

