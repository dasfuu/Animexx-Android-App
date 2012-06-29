package de.meisterfuu.animexx;

import java.text.ParseException;
import java.util.regex.Pattern;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Helper {

	public static String BetreffRe(String Betreff) {
		String s;
		String begin, mid, end;

		if (Betreff.startsWith("Re:") == true) {
			s = "Re[2]:" + Betreff.substring(3);
			return s;
		}

		int index = Betreff.indexOf("]:");
		int count = 0;
		if (index > 1) {
			end = "]:";
			mid = Betreff.substring(3, index);
			begin = Betreff.substring(0, 3);
			if ((Pattern.matches("\\d+", mid) == true) && (begin.equals("Re[")) && end.equals("]:")) {
				try {
					count = Integer.parseInt(mid) + 1;
					return begin + count + end + Betreff.substring(index + 2);
				} catch (Exception e) {
					s = "Re:" + Betreff;
					return s;
				}
			}
		}

		return "Re:" + Betreff;
	}


	public static void Tutorial(String text, String config, Context c) {
		Request.config = PreferenceManager.getDefaultSharedPreferences(c);
		if (!Request.config.getBoolean(config, false)) {
			AlertDialog alertDialog = new AlertDialog.Builder(c).create();
			alertDialog.setMessage(text);
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					//
				}
			});
			alertDialog.show();

			final Editor edit = Request.config.edit();
			edit.putBoolean(config, true);
			edit.commit();
		}
	}


	public static String DateToString(String date, boolean Short) {

		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		java.util.Date temp;

		try {
			temp = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Error";
		}

		java.util.Date Now = new java.util.Date();

		long diff = (Now.getTime() - temp.getTime());
		if (diff < 0) return "Error";

		diff /= 1000;

		if (diff < 60) {
			if (Short)
				return "vor " + diff + "s";
			else if ((diff) < 2)
				return "vor einer Sekunde";
			else
				return "vor " + (diff) + " Sekunden";
		} else if (diff < 3600) {
			if (Short)
				return "vor " + (diff / 60) + "min";
			else if ((diff / 60) < 2)
				return "vor einer Minute";
			else
				return "vor " + (int) (diff / 60) + " Minuten";
		} else if (diff < 86400) {
			if (Short)
				return "vor " + (diff / 60 / 60) + "h";
			else if ((diff / 60 / 60) < 2)
				return "vor einer Stunde";
			else
				return "vor " + (int) (diff / 60 / 60) + " Stunden";
		} else {
			if (Short)
				return "vor " + (diff / 60 / 60 / 24) + "d";
			else if ((diff / 60 / 60 / 24) < 2)
				return "vor einem Tag";
			else
				return "vor " + (int) (diff / 60 / 60 / 24) + " Tagen";
		}

	}

}
