package de.meisterfuu.animexx.GB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.profil.UserPopUp;

public class GBPopUp{

final CharSequence[] items = { "Öffnen", "Absender", "Antworten"};
String username;
String userid;
String GBid;
String text;
String title = username;
Context con;
AlertDialog alert;
AlertDialog.Builder builder;

public GBPopUp(Context context, String username, String userid,
String GBid, String text) {

this.username = username;
this.userid = userid;
this.con = context;
this.GBid = GBid;
this.text = text;

builder = new AlertDialog.Builder(con);
builder.setTitle(text);

builder.setItems(items, new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int item) {
	switch (item) {
	case 0:
		openGBSingle();
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

private void openGBSingle() {
Bundle bundle = new Bundle();
bundle.putString("von", username);
bundle.putString("von_id", userid);
bundle.putString("id", GBid);
bundle.putString("text", text);
Intent newIntent = new Intent(con.getApplicationContext(),
	GBViewSingle.class);
newIntent.putExtras(bundle);
con.startActivity(newIntent);
}

private void openAnswer() {
	Bundle bundle = new Bundle();
	bundle.putString("ID", userid);
	bundle.putString("an", username);
	Intent newIntent = new Intent(con.getApplicationContext(),
			GBAnswer.class);
	newIntent.putExtras(bundle);
	con.startActivity(newIntent);
}

private void openUser() {
UserPopUp Menu = new UserPopUp(con, username, userid);
Menu.PopUp();
}

}
