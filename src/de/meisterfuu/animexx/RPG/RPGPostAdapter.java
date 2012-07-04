package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.profil.UserPopUp;

public class RPGPostAdapter extends BaseAdapter {

	private final Activity context;
	private final ArrayList<RPGPostObject> names;

	static class ViewHolder {
		public TextView text;
		public TextView name;
		public TextView time;
		public WebView Ava;
	}


	public RPGPostAdapter(Activity context, ArrayList<RPGPostObject> names) {
		this.context = context;
		this.names = names;
	}


	public void refill() {
		Log.i("Anzahl", "Im Array sind " + names.size() + " Elemente.");
		notifyDataSetChanged();
	}


	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder = null;
		

		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			holder = new ViewHolder();

			rowView = inflater.inflate(R.layout.rpg_post_list_ava, null);
			holder.Ava = (WebView) rowView.findViewById(R.id.AvaList);
			holder.text = (TextView) rowView.findViewById(R.id.tx_rpgpost);
			holder.name = (TextView) rowView.findViewById(R.id.tx_rpgchara);
			holder.time = (TextView) rowView.findViewById(R.id.tx_rpgtime);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		final RPGPostObject s = names.get(position);
		
		if (s.isAction())
			holder.text.setTypeface(null, Typeface.ITALIC);
		else
			holder.text.setTypeface(null, Typeface.NORMAL);
		holder.text.setText(Html.fromHtml(s.getPost() + "<br>"));

		if (!s.isIn_time())
			holder.text.setTextColor(Color.GRAY);
		else
			holder.text.setTextColor(Color.BLACK);
		holder.name.setText(s.getChara() + " (" + s.getUser().getUsername() + ")");
		holder.time.setText(Helper.DateToString(s.getTime(), true));
		if (s.getAvatar_id() != 0)  {
			if(Request.config.getBoolean("rpgshowavatar", true)) {
				holder.Ava.setVisibility(View.GONE);
			} else {
				holder.Ava.loadUrl(s.getAvatar_url());
				holder.Ava.setVisibility(View.VISIBLE);
			}
		} else {
			holder.Ava.setVisibility(View.GONE);
		}
		

		// holder.time.setText(""+s.getId());

		holder.name.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				UserPopUp Menu = new UserPopUp(context, s.getUser());
				Menu.PopUp();
			}
		});

		return rowView;
	}


	public int getCount() {
		return names.size();
	}


	public Object getItem(int arg0) {
		return names.get(arg0);
	}


	public long getItemId(int arg0) {
		return arg0;
	}

}