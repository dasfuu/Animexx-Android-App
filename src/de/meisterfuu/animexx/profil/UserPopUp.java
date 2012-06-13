package de.meisterfuu.animexx.profil;

import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.ENS.ENSAnswer;
import de.meisterfuu.animexx.GB.GBAnswer;
import de.meisterfuu.animexx.GB.GBViewList;
import de.meisterfuu.animexx.other.UserObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class UserPopUp {
	final CharSequence[] items = { "Steckbrief", "Gästebuch", "ENS schreiben", "GB Eintrag verfassen"};
	String username;
	String userid;
	String title = username;
	Context con;
	AlertDialog alert;
	AlertDialog.Builder builder;

	public UserPopUp(Context context, UserObject User){
		this.username = User.getUsername();
		this.userid = User.getId();
		this.con = context;
		build();
	}
	
	public UserPopUp(Context context, String username, String userid) {

		this.username = username;
		this.userid = userid;
		this.con = context;
		build();
	}
	
	private void build(){
		
		builder = new AlertDialog.Builder(con);
		builder.setTitle(username);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					openProfil();
					break;
				case 1:
					openGB();
					break;
				case 2:
					openSendENS();
					break;
				case 3:
					openSendGB();
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

	private void openGB() {
		Bundle bundle = new Bundle();
		bundle.putString("id", userid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				GBViewList.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}

	private void openSendENS() {
		Bundle bundle2 = new Bundle();
		bundle2.putString("betreff", "");
		bundle2.putString("an", username);
		bundle2.putString("anid", userid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				ENSAnswer.class);
		newIntent.putExtras(bundle2);
		con.startActivity(newIntent);
	}
	
	private void openSendGB() {
		Bundle bundle2 = new Bundle();
		bundle2.putString("an", username);
		bundle2.putString("ID", userid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				GBAnswer.class);
		newIntent.putExtras(bundle2);
		con.startActivity(newIntent);
	}

	private void openProfil() {
		Bundle bundle = new Bundle();
		bundle.putString("id", userid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				UserSteckbrief.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}

}
