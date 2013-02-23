package de.meisterfuu.animexx.ENS;

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


public class ENSAdapter extends ArrayAdapter<ENSObject> {

	private final Activity context;
	private ArrayList<ENSObject> names;
	private boolean isAusgang;

	static class ViewHolder {

		public TextView text, txinfo;
		public VerticalTextView txtyp;
		public LinearLayout Color;
		public ImageView IMG2;
	}


	public ENSAdapter(Activity context, ArrayList<ENSObject> names, boolean isAusgang) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
		this.isAusgang = isAusgang;
	}


	public void refill(ArrayList<ENSObject> names) {
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
		final ENSObject s = names.get(position);
		holder.text.setText(s.getTitle());
		if (!s.isFolder()){
			if(isAusgang){
				String an = "Abgemeldet";
				if(s.getAnArray().length > 0) an = s.getAnArray()[0].getUsername();
				holder.txinfo.setText("An " + an + " am " + s.getTime());			
			} else {
				holder.txinfo.setText("Von " + s.getVon().getUsername() + " am " + s.getTime());
			}
		}
		holder.IMG2.setVisibility(View.GONE);

		holder.txtyp.setBackgroundResource(R.color.bg_blue2);
		holder.txtyp.setText("");

		if (s.isSystem()) {
			holder.txtyp.setText("Sys");
			holder.txtyp.setBackgroundResource(R.color.bg_green);
		}

		if (s.isUnread()) {
			holder.txtyp.setText("Neu");
			holder.txtyp.setBackgroundResource(R.color.header_bg);
		}

		if (s.isFolder()) {
			holder.txtyp.setText("Ordner");
			if(s.getAnVon().equals("an")) holder.txinfo.setText(s.getSignatur()+" ungelesene ENS"); else holder.txinfo.setText(" ");
			if(s.getENS_id() == 1 || s.getENS_id() == 2) {
				//holder.IMG2.setVisibility(View.VISIBLE);
			}
			holder.txtyp.setBackgroundResource(R.color.bg_lightgreen);
		}


		return rowView;
	}

	@Override
	public int getCount(){
		return names.size();
	}
}
