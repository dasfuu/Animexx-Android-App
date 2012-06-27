package de.meisterfuu.animexx;

import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.ENS.ENS;
import de.meisterfuu.animexx.ENS.ENSMenu;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.Home.ContactsActivityList;
import de.meisterfuu.animexx.other.ContactList;
import de.meisterfuu.animexx.other.Settings;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Menu extends Activity {


	ImageButton ENS,Guestbook,Contacts,About,Home,Settings;
	ImageView imgCoin, imgPush, imgunreadENS;
	boolean taler, push, unread;
	long time = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.main_menu);
		
		ENS = (ImageButton) findViewById(R.id.btens);
		Guestbook = (ImageButton) findViewById(R.id.btguestbook);
		Contacts = (ImageButton) findViewById(R.id.btcontacts);
		About = (ImageButton) findViewById(R.id.btabout);
		Home = (ImageButton) findViewById(R.id.bthome);
		Settings = (ImageButton) findViewById(R.id.btsettings);
		
		imgCoin = (ImageView) findViewById(R.id.img_coin);
		imgPush = (ImageView) findViewById(R.id.img_push);
		imgunreadENS = (ImageView) findViewById(R.id.img_unreadENS);
		
		setClick(ENS, ENSMenu.class);
		setClick(Guestbook, GBViewList.class);
		setClick(Contacts, ContactList.class);
		setClick(About, about.class);
		setClick(Home, ContactsActivityList.class);
		setClick(Settings, Settings.class);
		
		
		final Menu temp = this;
		imgCoin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					
				new Thread(new Runnable() {
					public void run() {
							
							try {
								Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_abholen/?api=2");
								temp.taler = false;
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							temp.runOnUiThread(new Runnable() {
								public void run() {
									temp.time = 0;
									setIcons();
								}
							});
								
							
						}
					}).start();
				
			}
		});
		
		imgunreadENS.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
					
				startActivity(new Intent().setClass(
						getApplicationContext(), ENS.class));
				
			}
		});

	}
	
	
	private void setClick(ImageButton b, final Class<?> c){
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent().setClass(getApplicationContext(), c));
			}
		});
	}


	@Override
	public void onBackPressed() {
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
		finish();
		return;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		taler = false;
		push = false;
		unread = false;
		
		final Menu temp = this;
		
		long i = System.currentTimeMillis() - time;
		Log.i("Animexx","last check "+i+"ms ago");
		if((i > 30000L) || time == 0){
			new Thread(new Runnable() {
				public void run() {
					
					temp.push = Request.checkpush(temp);	
						temp.runOnUiThread(new Runnable() {
							public void run() {
								setIcons();
							}
						});
						
					temp.unread = (Request.GetUnread() > 0);	
						temp.runOnUiThread(new Runnable() {
							public void run() {
								setIcons();
							}
						});
						
					temp.taler = KarotalerCheck();
						temp.runOnUiThread(new Runnable() {
							public void run() {
								setIcons();
							}
						});
					
				}
			}).start();
		
			time = System.currentTimeMillis();
		}
	}
	
	private boolean KarotalerCheck(){
		try {
			int abholbar = 0;
			JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_statistik/?api=2"));
			JSONArray ab = a.getJSONObject("return").getJSONArray("kt_abholbar");
			if(ab.length() > 0){
				abholbar = ab.getJSONObject(0).getInt("kt");
			} else {
				abholbar = 0;
			}
			
			return (abholbar > 0);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private void setIcons(){
		if(taler){
			imgCoin.setVisibility(View.VISIBLE);			
		} else {
			imgCoin.setVisibility(View.GONE);
		}
		
		if(push){
			imgPush.setVisibility(View.VISIBLE);
		} else {
			imgPush.setVisibility(View.GONE);
		}
		
		if(unread){
			imgunreadENS.setVisibility(View.VISIBLE);
		} else {
			imgunreadENS.setVisibility(View.GONE);
		}
		
	}




}
