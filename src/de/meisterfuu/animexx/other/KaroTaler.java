package de.meisterfuu.animexx.other;

import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.Request;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class KaroTaler extends BroadcastReceiver{

	String action;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.hasExtra("action")) {
			Bundle bundle = intent.getExtras();
			action = bundle.getString("action");			
			if(action.equalsIgnoreCase("stats")){
				stats(context, true);
			} else if (action.equalsIgnoreCase("get")){
				get(context);
			} else if (action.equalsIgnoreCase("allstats")){
				stats(context, false);
			} 
		}
	}

	private void stats(Context context, boolean b) {

		try {
			int abholbar = 0;
			JSONObject a = new JSONObject(Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_statistik/?api=2"));
			JSONArray ab = a.getJSONObject("return").getJSONArray("kt_abholbar");
			if(ab.length() > 0){
				abholbar = ab.getInt(0);
			} else {
				abholbar = 0;
			}
			int guthaben = a.getJSONObject("return").getInt("kt_guthaben");
			
			if(abholbar > 0){
				
				Log.i("xx","KaroTaler Receiver 2");
				Bundle bundle = new Bundle();
				bundle.putInt("abholbar", abholbar);
				bundle.putInt("guthaben", guthaben);	
				Intent newIntent = new Intent(context,
						KaroTalerAlert.class);
				newIntent.putExtras(bundle);
				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(newIntent);
				
				//Alert(context.getApplicationContext(), abholbar, guthaben);
			}else if(b == false){
				
				Bundle bundle = new Bundle();
				bundle.putInt("abholbar", abholbar);
				bundle.putInt("guthaben", guthaben);	
				Intent newIntent = new Intent(context,
						KaroTalerAlert.class);
				newIntent.putExtras(bundle);
				newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(newIntent);
				
				//Alert(context.getApplicationContext(), abholbar, guthaben);				
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void get(Context c) {

		try {
			Request.makeSecuredReq("https://ws.animexx.de/json/items/kt_abholen/?api=2");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	




}
