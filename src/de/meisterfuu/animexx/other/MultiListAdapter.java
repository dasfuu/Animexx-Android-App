package de.meisterfuu.animexx.other;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MultiListAdapter extends ArrayAdapter<MultiListObject> {

	private final Activity context;
	private ArrayList<MultiListObject> names;
	public ImageDownloader Images;

	static class ViewHolder {

		public TextView text, text_image, title;
		public RelativeLayout Layout_Picture;
		public LinearLayout Layout_BigPicture;
		public LinearLayout Layout_Title;
		public LinearLayout Layout_Normal;
		public LinearLayout Layout_Spacer;
		public ImageView Logo;
		public ImageView IMG;
	}


	public MultiListAdapter(Activity context, ArrayList<MultiListObject> names) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
		Images = new ImageDownloader();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.calist_multi, null);
			ViewHolder viewHolder = new ViewHolder();

			viewHolder.Layout_BigPicture = (LinearLayout) rowView.findViewById(R.id.big_picture);
			viewHolder.Layout_Title = (LinearLayout) rowView.findViewById(R.id.title);
			viewHolder.Layout_Normal = (LinearLayout) rowView.findViewById(R.id.text);
			viewHolder.Layout_Spacer = (LinearLayout) rowView.findViewById(R.id.spacer);
			viewHolder.Layout_Picture = (RelativeLayout) rowView.findViewById(R.id.picture);

			viewHolder.text = (TextView) rowView.findViewById(R.id.NormalText);
			viewHolder.text_image = (TextView) rowView.findViewById(R.id.ImageText);
			viewHolder.title = (TextView) rowView.findViewById(R.id.TitleText);

			viewHolder.IMG = (ImageView) rowView.findViewById(R.id.ImageView);
			viewHolder.Logo = (ImageView) rowView.findViewById(R.id.BigImageView);

			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final MultiListObject s = names.get(position);

		holder.Layout_BigPicture.setVisibility(View.GONE);
		holder.Layout_Title.setVisibility(View.GONE);
		holder.Layout_Normal.setVisibility(View.GONE);
		holder.Layout_Spacer.setVisibility(View.GONE);
		holder.Layout_Picture.setVisibility(View.GONE);

		if (s.getTyp() == MultiListObject.LOGO) {
			holder.Layout_BigPicture.setVisibility(View.VISIBLE);
			Images.download(s.getPicture(), holder.Logo);
		} else if (s.getTyp() == MultiListObject.PICTURE) {
			holder.Layout_Picture.setVisibility(View.VISIBLE);
			if(s.getPictureID() == 0) {
				Images.download(s.getPicture(), holder.IMG);
			} else {
				holder.IMG.setImageResource(s.getPictureID());
			}
			holder.text_image.setText(s.getText());
		} else if (s.getTyp() == MultiListObject.SPACER) {
			holder.Layout_Spacer.setVisibility(View.VISIBLE);
		} else if (s.getTyp() == MultiListObject.TEXT) {
			holder.Layout_Normal.setVisibility(View.VISIBLE);
			holder.text.setText(s.getText());
		} else if (s.getTyp() == MultiListObject.TITLE) {
			holder.Layout_Title.setVisibility(View.VISIBLE);
			holder.title.setText(s.getText());
		}

		return rowView;
	}


	@Override
	public int getCount() {
		return names.size();
	}
}
