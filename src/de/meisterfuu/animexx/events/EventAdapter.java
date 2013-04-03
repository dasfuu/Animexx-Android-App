package de.meisterfuu.animexx.events;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.VerticalTextView;

public class EventAdapter extends ArrayAdapter<EventObject> {

	private final Activity context;
	private ArrayList<EventObject> names;

	static class ViewHolder {

		public TextView text, txinfo;
		public VerticalTextView txtyp;
		public LinearLayout Color;
		public ImageView IMG2;
	}


	public EventAdapter(Activity context, ArrayList<EventObject> names) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
	}


	public void refill(ArrayList<EventObject> names) {
		this.names = names;
		Log.i("Anzahl", "Im Array sind " + names.size() + " Elemente.");
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.calist_small, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.CAtxt);
			viewHolder.txinfo = (TextView) rowView.findViewById(R.id.CAtxinfo);
			viewHolder.txtyp = (VerticalTextView) rowView.findViewById(R.id.txType);
			viewHolder.Color = (LinearLayout) rowView.findViewById(R.id.CAcolor);
			viewHolder.IMG2 = (ImageView) rowView.findViewById(R.id.webIMG2);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final EventObject s = names.get(position);
		holder.text.setText(s.getName());

		holder.txinfo.setText(s.getOrt() + " " + s.getDabei_anzahl());

		holder.IMG2.setVisibility(View.GONE);

		holder.txtyp.setBackgroundResource(R.color.bg_blue2);
		holder.txtyp.setText("");

		if (s.isAnimexxInvolved()) {
			holder.txtyp.setBackgroundResource(R.color.header_bg);
		}

		return rowView;
	}


	@Override
	public int getCount() {
		return names.size();
	}
}
