package de.meisterfuu.animexx;

import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Pattern;

import oauth.signpost.OAuth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class Helper {

	public static void sendStacTrace(Exception e, Context con) {
		StackTraceElement[] stack = e.getStackTrace();
		String text = "";
		for(StackTraceElement line : stack)
		{
			text += line.toString();
		}

		try {
			String s = "("+Request.config.getString("id", "none")+") "+Request.config.getString("username", "none")+":";
			s += "\n\n";
			s += ""+text;
			Request.sendENS("Debug Log", s, "Debug Log "+Constants.VERSION, new int[]{586283}, -1);
			Log.i("Debug Log", "Debug Log gesendet");
			Request.doToast("Debug Log gesendet!", con);
		} catch (Exception z) {
			z.printStackTrace();
		}
	}

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
			alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {

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


	public static long toTimestamp(String date) {
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		java.util.Date temp;

		try {
			temp = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
		return temp.getTime();

	}


	public static String DateToString(String date, boolean Short) {

		if(date.equalsIgnoreCase("Aktuell")){
			return "Aktuell";
		}
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		java.util.Date temp;

		try {
			temp = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "Error";
		}

		return TimestampToString(temp.getTime(), Short);

	}

	public static String TimestampToString(long time, boolean Short) {


		//java.util.Date Now = new java.util.Date();
		Calendar Now = Calendar.getInstance();

		long diff = (Now.getTimeInMillis() - time);
		if (diff < 0) return "Gerade eben";

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


	public static long HowLongAgo(String timestamp) {
		Long tempo;
		try {
			tempo = Long.parseLong(timestamp);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}

		java.util.Date Now = new java.util.Date();

		long diff = (Now.getTime() - tempo);
		if (diff < 0) return 0;

		diff /= 1000;

		return diff;

	}


	public static long TimestampBeforeSec(long sec) {
		java.util.Date Now = new java.util.Date();
		return Now.getTime() - sec;
	}


	public static void isLoggedIn(Activity a) {
		SharedPreferences config = PreferenceManager.getDefaultSharedPreferences(a);
		String token = config.getString(OAuth.OAUTH_TOKEN, null);
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, null);
		
		if(( Calendar.getInstance().get( Calendar.MONTH ) == Calendar.DECEMBER) &&  Calendar.getInstance().get( Calendar.DAY_OF_MONTH )  >= 24){
			a.setTheme(R.style.AnimexxChristmasStyle);
		} else {
			a.setTheme(R.style.AnimexxStyle);		
		}

		if (token != null && secret != null)
			return;
		else
			a.startActivity(new Intent(a, AnimexxActivity.class));
		a.finish();
	}


	public static boolean isLoggedIn(Context c) {
		SharedPreferences config = PreferenceManager.getDefaultSharedPreferences(c);
		String token = config.getString(OAuth.OAUTH_TOKEN, null);
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, null);
		if (token != null && secret != null)
			return true;
		else
			return false;
	}

}
