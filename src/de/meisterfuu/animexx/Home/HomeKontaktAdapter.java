package de.meisterfuu.animexx.Home;

import java.util.ArrayList;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.SingleImage;
import de.meisterfuu.animexx.other.VerticalTextView;

public class HomeKontaktAdapter extends ArrayAdapter<HomeKontaktObject> {

	private final HomeKontaktFragment context;
	private ArrayList<HomeKontaktObject> names;
	public ImageDownloader Images;

	static class ViewHolder {

		public TextView text, txinfo;
		public VerticalTextView txtyp;
		public LinearLayout Color;
		public ImageView IMG;
		public ImageView Chat;
	}


	public HomeKontaktAdapter(HomeKontaktFragment context, ArrayList<HomeKontaktObject> names) {
		super(context.getActivity(), R.layout.home_kontakt_list_item, names);
		this.context = context;
		this.names = names;
	}


	public void refill(ArrayList<HomeKontaktObject> names) {
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
		final HomeKontaktObject s = names.get(position);
		if (!s.isMulti_item()) {
			holder.text.setText(HomeKontaktHelper.getFullText(s));
			holder.txinfo.setText(Helper.DateToString(s.getDate_server(), false) + ", " + s.getVon().getUsername());
			// holder.txinfo.setText(""+s.getDate_server_ts());
			
			holder.txtyp.setText(HomeKontaktHelper.getSideText(s));

			if (s.getItem_image() != null && s.getItem_image() != "" && s.getItem_image() != "null") {
				Images.download(s.getItem_image(), holder.IMG);
				holder.IMG.setVisibility(View.VISIBLE);
			} else {
				holder.IMG.setVisibility(View.GONE);
			}

			if (s.getEvent_typ().equals("mb")) {
				holder.Color.setBackgroundResource(R.color.bg_blue);
				holder.Chat.setVisibility(View.VISIBLE);
			} else {
				holder.Color.setBackgroundResource(R.color.bg_lightgreen);
				holder.Chat.setVisibility(View.GONE);
			}

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					//Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://animexx.onlinewelten.com" + s.getLink()));
					//newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
					//context.startActivity(newIntent);
					((HomeKontaktActivity)context.getActivity()).showDetail(s.json);
					
				}

			});

			holder.IMG.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putString("URL", s.getItem_image().replaceAll(".thum.jpg", ".jpg"));
					Intent newIntent = new Intent(context.getActivity(), SingleImage.class);
					newIntent.putExtras(bundle);
					context.startActivity(newIntent);
				}

			});

		} else {

			holder.Color.setBackgroundResource(R.color.bg_blue2);
			holder.Chat.setVisibility(View.GONE);
			
			if (s.getGrouped_by() == 1) {
				holder.txinfo.setText(Helper.DateToString(s.getDate_server(), false) + ", " + s.getItems()[0].getVon().getUsername());
				holder.text.setText("" + s.getItems()[0].getEvent_name());
			} else {
				holder.txinfo.setText(Helper.DateToString(s.getDate_server(), false));
				holder.text.setText("" + s.getItems()[0].getItem_name());
			}

			if (s.getItems()[0].getItem_image() != null && s.getItems()[0].getItem_image() != "" && s.getItems()[0].getItem_image() != "null") {
				Images.download(s.getItems()[0].getItem_image(), holder.IMG);
				holder.IMG.setVisibility(View.VISIBLE);
			} else {
				holder.IMG.setVisibility(View.GONE);
			}

			holder.txtyp.setText(HomeKontaktHelper.getSideText(s.getItems()[0]));

			rowView.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					((HomeKontaktActivity) context.getActivity()).showMulti(s.data);
				}

			});
		}
		return rowView;
	}


	@Override
	public int getCount() {
		return names.size();
	}
}
