package de.meisterfuu.animexx.other;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.LoadImage;

public class CTAdapter extends ArrayAdapter<UserObject> {
	private final Activity context;
	private final UserObject[] names;

	static class ViewHolder {
		public TextView text;
		public LoadImage image;
	}

	public CTAdapter(Activity context, UserObject[] names) {
		super(context, R.layout.contact_list, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.contact_list, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.ctusername);
			viewHolder.image = (LoadImage) rowView.findViewById(R.id.ctimage);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final UserObject s = names[position];

		holder.image.setImageDrawable(s.getPicture());
		holder.text.setText(s.getUsername());
		
		
		return rowView;
	}
}
