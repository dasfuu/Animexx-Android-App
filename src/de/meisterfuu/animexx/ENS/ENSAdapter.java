package de.meisterfuu.animexx.ENS;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ENSAdapter extends ArrayAdapter<ENSObject> {
	private final Activity context;
	private ArrayList<ENSObject> names;

	static class ViewHolder {
		public TextView text, txinfo;
		public ImageView image;
	}

	public ENSAdapter(Activity context, ArrayList<ENSObject> names) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
	}
	
	public void refill(ArrayList<ENSObject> newData) {
	    names = newData;
		Log.i("Anzahl", "Im Array sind "+names.size()+" Elemente.");
	    notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.enslist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.txt);
			viewHolder.txinfo = (TextView) rowView.findViewById(R.id.txinfo);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.img);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		ENSObject s = names.get(position);
		holder.text.setText(s.getBetreff());
		if (s.getTyp() == 99) {
			holder.image.setImageResource(R.drawable.folder);
			holder.txinfo.setText("  ");
		} else {
			holder.txinfo.setText("Von " + s.getVon().getUsername() + " am " + s.getTime());
			String flag = Integer.toBinaryString(s.getFlags());

			if (flag.charAt(flag.length() - 1) == '0') {
				holder.image.setImageResource(R.drawable.unseenmail);
			} else if (flag.charAt(flag.length() - 1) == '1') {
				holder.image.setImageResource(R.drawable.unreadmail);
			}
			if (flag.length() >= 2 && flag.charAt(flag.length() - 2) == '1')
				holder.image.setImageResource(R.drawable.mail);
		}

		return rowView;
	}
}
