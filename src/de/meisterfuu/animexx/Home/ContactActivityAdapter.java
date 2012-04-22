package de.meisterfuu.animexx.Home;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;

public class ContactActivityAdapter extends ArrayAdapter<ContactActivityObject> {
	private final Activity context;
	private ArrayList<ContactActivityObject> names;

	static class ViewHolder {
		public TextView text, txinfo;
	}

	public ContactActivityAdapter(Activity context, ArrayList<ContactActivityObject> names) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
	}
	
	public void refill(ArrayList<ContactActivityObject> newData) {
	    names=newData;
		Log.i("Anzahl", "Im Array sind "+names.size()+" Elemente.");
	    notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.calist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.CAtxt);
			viewHolder.txinfo = (TextView) rowView.findViewById(R.id.CAtxinfo);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		ContactActivityObject s = names.get(position);
		holder.text.setText(s.getFinishedText());
		holder.txinfo.setText(s.getTime());


		return rowView;
	}
}
