package de.meisterfuu.animexx.GB;

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
import de.meisterfuu.animexx.other.LoadImage;
import de.meisterfuu.animexx.profil.UserPopUp;

public class GBAdapter extends ArrayAdapter<GBObject> {
	private final Activity context;
	private final ArrayList<GBObject> names;

	static class ViewHolder {
		public TextView text;
		public LoadImage image;
		public ImageView user;
		public TextView info;
	}

	public GBAdapter(Activity context, ArrayList<GBObject> names) {
		super(context, R.layout.gblist, names);
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
			rowView = inflater.inflate(R.layout.gblist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.GBtxt);
			viewHolder.image = (LoadImage) rowView.findViewById(R.id.gbimage);
			viewHolder.user = (ImageView) rowView.findViewById(R.id.imguser);
			viewHolder.info = (TextView) rowView.findViewById(R.id.GBinfo);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final GBObject s = names.get(position);

		holder.image.setImageDrawable(s.getAvatar());
		holder.text.setText(s.getEinleitung());
		holder.info.setText(s.getVon().getUsername()+" am "+s.getTime());
		
		if(!s.getVon().getId().equals("none")){
			
				holder.info.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						UserPopUp Menu = new UserPopUp(context, s.getVon().getUsername(), s.getVon().getId());
						Menu.PopUp();
					}
				});
				
				holder.user.setImageResource(android.R.drawable.ic_menu_info_details);
				holder.user.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						UserPopUp Menu = new UserPopUp(context, s.getVon().getUsername(), s.getVon().getId());
						Menu.PopUp();
					}
				});
		} else {
			holder.info.setOnClickListener(null);			
			holder.user.setImageResource(0);
			holder.user.setOnClickListener(null);
		}
		
		return rowView;
	}
}
