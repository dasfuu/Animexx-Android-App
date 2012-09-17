package de.meisterfuu.animexx.ENS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.booleanobject;
import de.meisterfuu.animexx.profil.UserPopUp;

public class ENSPopUp {
	final CharSequence[] items = { "Öffnen", "Absender", "Antworten",
			"Wegwerfen" };
	String username;
	String userid;
	Long ENSid;
	String Betreff;
	String title = username;
	String anvon;
	int List = 0; //1 = Eingang; 2 = Ausgang
	Context con;
	AlertDialog alert;
	AlertDialog.Builder builder;

	public ENSPopUp(Context context, String username, String userid,
			Long ENSid, String Betreff, String anvon, int List) {
		this(context, username, userid, ENSid, Betreff, anvon);
		this.List = List;
	}

	public ENSPopUp(Context context, String username, String userid,
			Long ENSid, String Betreff, String anvon) {

		this.username = username;
		this.userid = userid;
		this.con = context;
		this.ENSid = ENSid;
		this.Betreff = Betreff;
		this.anvon = anvon;


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
				case 3:
					RemoveENS();
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
		bundle.putLong("id", ENSid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				ENSSingle.class);
		newIntent.putExtras(bundle);
		con.startActivity(newIntent);
	}

	private void openAnswer() {
		Bundle bundle2 = new Bundle();
		bundle2.putString("betreff", Betreff);
		bundle2.putLong("relativ", ENSid);
		bundle2.putString("an", username);
		bundle2.putString("anid", userid);
		Intent newIntent = new Intent(con.getApplicationContext(),
				ENSAnswer.class);
		newIntent.putExtras(bundle2);
		con.startActivity(newIntent);
	}

	private void openUser() {
		UserPopUp Menu = new UserPopUp(con, username, userid);
		Menu.PopUp();
	}

	private void RemoveENS() {


		final Activity temp = (Activity) con;
		final ProgressDialog dialog = ProgressDialog.show(con, "",
				"Lösche ENS...", true);
		new Thread(new Runnable() {
			public void run() {
				final booleanobject x = new booleanobject();
				try {
					x.bool = Request.RemoveENS(ENSid, anvon);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					x.bool = false;
					e.printStackTrace();
				}

				temp.runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();
						if (!x.bool) {
							Request.doToast("Löschen gescheitert!",
									con.getApplicationContext());
						} else {
							if(List == 1){
								con.startActivity(new Intent().setClass(
									con.getApplicationContext(), ENS.class));
							} else if (List == 2){
								con.startActivity(new Intent().setClass(
									con.getApplicationContext(), ENS.class));
							}
							Request.doToast("ENS gelöscht!",
									con.getApplicationContext());
						}
					}
				});
			}
		}).start();

	}

}
