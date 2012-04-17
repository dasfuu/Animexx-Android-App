package de.meisterfuu.animexx.GB;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.LoadImage;
import de.meisterfuu.animexx.profil.UserPopUp;

public class GBAdapter extends ArrayAdapter<GBObject> {
	private final Activity context;
	private final GBObject[] names;

	static class ViewHolder {
		public TextView text;
		public LoadImage image;
		public TextView info;
	}

	public GBAdapter(Activity context, GBObject[] names) {
		super(context, R.layout.gblist, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.gblist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.GBtxt);
			viewHolder.image = (LoadImage) rowView.findViewById(R.id.gbimage);
			viewHolder.info = (TextView) rowView.findViewById(R.id.GBinfo);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final GBObject s = names[position];

		holder.image.setImageDrawable(s.getAvatar());
		holder.text.setText(s.getEinleitung());
		holder.info.setText(s.getVon()+" am "+s.getTime());
		
		if(!s.getVon_id().equals("none")){
			holder.info.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					UserPopUp Menu = new UserPopUp(context, s.getVon(), s.getVon_id());
					Menu.PopUp();
				}
			});
		}
		
		return rowView;
	}
}
