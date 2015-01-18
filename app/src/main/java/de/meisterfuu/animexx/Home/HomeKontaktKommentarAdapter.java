package de.meisterfuu.animexx.Home;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.VerticalTextView;


public class HomeKontaktKommentarAdapter extends ArrayAdapter<HomeKontaktKommentarObject> {

	private final HomeKontaktKommentarFragment context;
	private ArrayList<HomeKontaktKommentarObject> names;
	public ImageDownloader Images;

	static class ViewHolder {

		public TextView text, txinfo;
		public VerticalTextView txtyp;
		public LinearLayout Color;
		public ImageView IMG;
		public ImageView Chat;
	}


	public HomeKontaktKommentarAdapter(HomeKontaktKommentarFragment context, ArrayList<HomeKontaktKommentarObject> names) {
		super(context.getActivity(), R.layout.home_kontakt_list_item, names);
		this.context = context;
		this.names = names;
	}


	public void refill(ArrayList<HomeKontaktKommentarObject> names) {
		this.names = names;
		Log.i("Anzahl", "Im Array sind " + names.size() + " Elemente.");
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getActivity().getLayoutInflater();
			rowView = inflater.inflate(R.layout.home_kontakt_list_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.CAtxt);
			viewHolder.txinfo = (TextView) rowView.findViewById(R.id.CAtxinfo);
			viewHolder.txtyp = (VerticalTextView) rowView.findViewById(R.id.txType);
			viewHolder.Color = (LinearLayout) rowView.findViewById(R.id.txColor);
			viewHolder.IMG = (ImageView) rowView.findViewById(R.id.webIMG2);
			viewHolder.Chat = (ImageView) rowView.findViewById(R.id.CAChatBubble);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final HomeKontaktKommentarObject s = names.get(position);
		
			holder.Color.setBackgroundResource(R.color.bg_blue2);
			holder.Chat.setVisibility(View.VISIBLE);			
	
			holder.txinfo.setText(s.getVon().getUsername());
			holder.text.setText(s.getComment());
			
			holder.IMG.setVisibility(View.GONE);			

			holder.txtyp.setText("");
		
		return rowView;
	}


	@Override
	public int getCount() {
		return names.size();
	}
}

