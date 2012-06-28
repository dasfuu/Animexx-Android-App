package de.meisterfuu.animexx.RPG;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;

public class RPGPostAdapter extends ArrayAdapter<RPGPostObject> {
	private final Activity context;
	private final ArrayList<RPGPostObject> names;

	static class ViewHolder {
		public TextView text;
		public TextView name;
		public TextView time;
	}

	public RPGPostAdapter(Activity context, ArrayList<RPGPostObject> names) {
		super(context, R.layout.rpg_post_list, names);
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
			rowView = inflater.inflate(R.layout.rpg_post_list, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.tx_rpgpost);
			viewHolder.name = (TextView) rowView.findViewById(R.id.tx_rpgchara);
			viewHolder.time = (TextView) rowView.findViewById(R.id.tx_rpgtime);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGPostObject s = names.get(position);

		holder.text.setText(s.getPost());
		if(!s.isIn_time()) holder.text.setTextColor(Color.RED); else holder.text.setTextColor(Color.BLACK);
		holder.name.setText(s.getChara()+" ("+s.getUser().getUsername()+")");
		holder.time.setText(s.getTime());
		//holder.info.setText("Letzter Eintrag am "+s.getLastUpdate()+"\ndurch "+s.getLastUser().getUsername()+" ("+s.getLastChar()+")\n"+s.getPostCount()+" Einträge");
		
		
		return rowView;
	}
}