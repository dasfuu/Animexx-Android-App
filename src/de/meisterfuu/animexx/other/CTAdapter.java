package de.meisterfuu.animexx.other;

import java.util.ArrayList;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;
import de.meisterfuu.animexx.other.LoadImage;

public class CTAdapter extends ArrayAdapter<UserObject> {
	private final Activity context;
	public ArrayList<UserObject> names;
	private ArrayList<UserObject> all;

	static class ViewHolder {
		public TextView text;
		public LoadImage image;
	}

	public CTAdapter(Activity context, ArrayList<UserObject> names) {
		super(context, R.layout.contact_list, names);
		this.context = context;
		this.names = names;
		this.all = names;
	}
	
	public void filterUser(CharSequence s)
    {
		ArrayList<UserObject> filtered = new ArrayList<UserObject>();
        
		for(int i = 0; i < all.size(); i++) {
			if(all.get(i).getUsername().toLowerCase().contains( (""+s).toLowerCase() )) {
				filtered.add(all.get(i));
			}
		}
		
        if(filtered.isEmpty()) {
        	Request.doToast("Keine Treffer :/", context);
        }
        this.names = filtered;
        
        notifyDataSetChanged();
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
		if(position < names.size()){
			final UserObject s = names.get(position);
			holder.image.setImageDrawable(s.getPicture());
			holder.text.setText(s.getUsername());
		}

		
		return rowView;
	}
	
	@Override
	public int getCount(){
		return names.size();		
	}
	

	
}
