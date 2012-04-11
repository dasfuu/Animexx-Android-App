package de.meisterfuu.animexx.ENS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.profil.UserPopUp;

public class ENSPopUp {
	final CharSequence[] items = { "Öffnen", "Absender", "Antworten",
			"Wegwerfen" };
	String username;
	String userid;
	String ENSid;
	String Betreff;
	String title = username;
	Context con;
	AlertDialog alert;
	AlertDialog.Builder builder;

	public ENSPopUp(Context context, String username, String userid,
			String ENSid, String Betreff) {

		this.username = username;
		this.userid = userid;
		this.con = context;
		this.ENSid = ENSid;
		this.Betreff = Betreff;

		builder = new AlertDialog.Builder(con);
		builder.setTitle(Betreff);

		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:
					openENS();
					break;
				case 1:
					openUser();
					break;
				case 2:
					openAnswer();
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

	private void openENS() {
		Bundle bundle = new Bundle();
		bundle.putString("id", ENSid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				ENSSingle.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}

	private void openAnswer() {
		Bundle bundle2 = new Bundle();
		bundle2.putString("betreff", "Re:" + Betreff);
		bundle2.putString("relativ", ENSid);
		bundle2.putString("an", username);
		Intent newIntent = new Intent(con.getApplicationContext(),
				ENSAnswer.class);
		newIntent.putExtras(bundle2);
		con.startActivity(newIntent);
	}

	private void openUser() {
		UserPopUp Menu = new UserPopUp(con, username, userid);
		Menu.PopUp();
	}

}
