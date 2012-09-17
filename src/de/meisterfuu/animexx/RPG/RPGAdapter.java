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

public class RPGAdapter extends ArrayAdapter<RPGObject> {
	private final Activity context;
	private final ArrayList<RPGObject> names;

	static class ViewHolder {
		public TextView text;
		public ImageView image;
		public TextView info;
	}

	public RPGAdapter(Activity context, ArrayList<RPGObject> names) {
		super(context, R.layout.rpg_list, names);
		this.context = context;
		this.names = names;
	}

	public void refill() {
		Log.i("Anzahl", "Im Array sind "+names.size()+" Elemente.");
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
		final RPGObject s = names.get(position);

		holder.text.setText(s.getName());
		holder.info.setText("Letzter Eintrag am "+s.getLastUpdate()+"\ndurch "+s.getLastUser().getUsername()+" ("+s.getLastChar()+")\n"+s.getPostCount()+" Einträge");


		return rowView;
	}
}
