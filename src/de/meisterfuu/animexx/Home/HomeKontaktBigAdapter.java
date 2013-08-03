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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.ImageSaveObject;
import de.meisterfuu.animexx.other.SingleImage;


public class HomeKontaktBigAdapter extends ArrayAdapter<HomeKontaktObject> {

	private final HomeKontaktActivity context;
	private ArrayList<HomeKontaktObject> names;
	public ImageDownloaderCustom Images;

	static class ViewHolder {
		public TextView title, info, date;
		public ImageView IMG;
		public Button comment;
		public Button open;
	}


	public HomeKontaktBigAdapter(HomeKontaktActivity context, ArrayList<HomeKontaktObject> names) {
		super(context, R.layout.home_kontakt_item_big, names);
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
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.home_kontakt_item_big, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.title = (TextView) rowView.findViewById(R.id.home_kontakt_big_title);
			viewHolder.date = (TextView) rowView.findViewById(R.id.home_kontakt_big_date);
			viewHolder.info = (TextView) rowView.findViewById(R.id.home_kontakt_big_info);
			viewHolder.IMG = (ImageView) rowView.findViewById(R.id.home_kontakt_big_image);
			viewHolder.comment = (Button) rowView.findViewById(R.id.home_kontakt_big_comment);
			viewHolder.open = (Button) rowView.findViewById(R.id.home_kontakt_big_open);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final HomeKontaktObject obj = names.get(position);
		if (!obj.isMulti_item()) {
			
			
			if(obj.getItem_image_big() != null){
				holder.IMG.setVisibility(View.VISIBLE);
				ImageSaveObject imgObj = new ImageSaveObject(obj.getItem_image_big(), obj.getId());
				Images.download(imgObj,holder.IMG, true);
			} else {
				holder.IMG.setVisibility(View.GONE);
			}
			
			
			holder.title.setText(obj.getItem_name());
			String temp = HomeKontaktHelper.getFullText(obj)+"\n";
			if(obj.getItem_author() != null){
				temp += obj.getItem_author().getUsername();
			} else {
				temp += obj.getVon().getUsername();
			}
			holder.date.setText(obj.getDate_server());
			holder.info.setText(temp);
			
			if(obj.isCommentable()){		
				holder.comment.setText(obj.getComment_count()+" Kommentare");
				holder.comment.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						context.showComments(obj.getId());
					}
					
				});
				holder.comment.setVisibility(View.VISIBLE);
				if(obj.getComment_count() > 0){
				} else {
					holder.comment.setText("Schreibe den ersten Kommentar!");
				}
			} else {
				holder.comment.setVisibility(View.GONE);
			}
			
			if(obj.getLink() != null)
				holder.open.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					Uri uri = Uri.parse(obj.getLink());
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					context.startActivity(intent);						
				}
				
			});

			holder.IMG.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putString("URL", obj.getItem_image_big().replaceAll(".thum.jpg", ".jpg"));
					Intent newIntent = new Intent(context, SingleImage.class);
					newIntent.putExtras(bundle);
					context.startActivity(newIntent);
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
