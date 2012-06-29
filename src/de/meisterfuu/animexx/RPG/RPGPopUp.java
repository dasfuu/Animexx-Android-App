package de.meisterfuu.animexx.RPG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import de.meisterfuu.animexx.Request;


public class RPGPopUp {
	final CharSequence[] items = { "Posts", "Charaktere", "Von Anfang an lesen"};
	long rpgid, count;
	String title;
	Context con;
	AlertDialog alert;
	AlertDialog.Builder builder;

	
	public RPGPopUp(Context context, long rpgid, String title , long count) {
		this.title = title;
		this.rpgid = rpgid;
		this.con = context;
		this.count = count;
		build();
	}
	
	private void build(){
		
		builder = new AlertDialog.Builder(con);
		builder.setTitle(title);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					openRPG();
					break;
				case 1:
					openChara();
					break;
				case 2:
					openRPGstart();
					break;
				default:
					Request.doToast("Gibts noch nicht :P",
							con.getApplicationContext());
					break;
				}
			}
		});
		alert = builder.create();
		alert.setOwnerActivity((Activity) con);
		
	}

	public void PopUp() {
		alert.show();
	}

	private void openRPG() {
		Bundle bundle = new Bundle();
		bundle.putLong("id", rpgid);
		bundle.putLong("count", count);
		Intent newIntent = new Intent(con, RPGViewPostList.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}

	private void openChara() {
		Bundle bundle = new Bundle();
		bundle.putLong("id", rpgid);
		Intent newIntent = new Intent(con, RPGViewCharaList.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}
	
	private void openRPGstart() {
		Bundle bundle = new Bundle();
		bundle.putLong("id", rpgid);
		Intent newIntent = new Intent(con, RPGViewPostListStart.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}



}
