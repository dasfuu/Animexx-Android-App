package de.meisterfuu.animexx.other;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class KaroTalerAlert extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			Bundle bundle = this.getIntent().getExtras();
			int abholbar = bundle.getInt("abholbar");
			int guthaben = bundle.getInt("guthaben");
			Alert(this, abholbar, guthaben);


	}

	private void Alert(final Context c, int amount, int gesamt){

		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage("Möchtest du "+amount+" Karotaler abholen?")
		       .setCancelable(false)
		       .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        		Intent intent2 = new Intent();
		        		intent2.setAction("de.meisterfuu.animexx.karotaler");
		        		intent2.putExtra("action", "get" );
		        		sendBroadcast(intent2);
		                dialog.cancel();
		                KaroTalerAlert.this.finish();
		           }
		       })
		       .setNegativeButton("Nein", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		                KaroTalerAlert.this.finish();
		           }
		       });
		AlertDialog alert = builder.create();
	     alert.show();

	}
}
