package de.meisterfuu.animexx.other;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.meisterfuu.animexx.R;

public class AvatarAdapter extends ArrayAdapter<AvatarObject> {

	private final Activity context;
	public ArrayList<AvatarObject> names;

	static class ViewHolder {

		public WebView IMG;
		public TextView choose;
	}


	public AvatarAdapter(Activity context, ArrayList<AvatarObject> names) {
		super(context, R.layout.avatar_picker_item, names);
		this.context = context;
		this.names = names;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.avatar_picker_item, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.IMG = (WebView) rowView.findViewById(R.id.webIMG);
			viewHolder.choose = (TextView) rowView.findViewById(R.id.txchoose);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		if (position < names.size()) {
			final AvatarObject s = names.get(position);
			if (s != null) {
				holder.IMG.loadUrl(s.getUrl());
			}
			holder.IMG.setClickable(false);
			holder.IMG.setLongClickable(false);
			holder.IMG.setFocusable(false);
			holder.IMG.setFocusableInTouchMode(false);
			if (s == null) {
				holder.IMG.setVisibility(View.INVISIBLE);
				holder.choose.setText("Kein Avatar");
			} else {
				holder.IMG.setVisibility(View.VISIBLE);
				holder.choose.setText("Wählen");
			}
		}

		return rowView;
	}


	@Override
	public int getCount() {
		return names.size();
	}

}
