package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.profil.UserPopUp;

public class RPGCharaAdapter extends ArrayAdapter<RPGCharaObject> {

	private final Activity context;
	private final ArrayList<RPGCharaObject> names;

	static class ViewHolder {

		public TextView text;
		public ImageView image;
		public TextView info;
	}


	public RPGCharaAdapter(Activity context, ArrayList<RPGCharaObject> names) {
		super(context, R.layout.rpg_list, names);
		this.context = context;
		this.names = names;
	}


	public void refill() {
		Log.i("Anzahl", "Im Array sind " + names.size() + " Elemente.");
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.rpg_list, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.RPGName);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.RPGIcon);
			viewHolder.info = (TextView) rowView.findViewById(R.id.RPGInfo);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGCharaObject s = names.get(position);

		holder.text.setText(s.getName());
		if (!s.isFree())
			holder.info.setText(s.getUser().getUsername());
		else
			holder.info.setText("Rolle frei!");

		holder.info.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				UserPopUp Menu = new UserPopUp(context, s.getUser());
				Menu.PopUp();
			}
		});

		return rowView;
	}
}