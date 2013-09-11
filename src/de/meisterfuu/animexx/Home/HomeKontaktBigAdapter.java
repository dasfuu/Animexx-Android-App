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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.fanworks.Fanart;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.ImageSaveObject;
import de.meisterfuu.animexx.other.SingleImage;
import de.meisterfuu.animexx.other.UserObject;


public class HomeKontaktBigAdapter extends ArrayAdapter<HomeKontaktObject> {

	private final HomeKontaktActivity context;
	private ArrayList<HomeKontaktObject> names;
	public ImageDownloaderCustom Images;

	static class ViewHolder {
		public TextView title, info, date, comment_txt, open_txt;
		public ImageView IMG, comment_img, open_img;
		public LinearLayout comment;
		public LinearLayout open;
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
			viewHolder.comment = (LinearLayout) rowView.findViewById(R.id.home_kontakt_big_comment);
			viewHolder.open = (LinearLayout) rowView.findViewById(R.id.home_kontakt_big_open);
			viewHolder.comment_img = (ImageView) rowView.findViewById(R.id.home_kontakt_big_comment_img);
			viewHolder.open_img = (ImageView) rowView.findViewById(R.id.card_picture_img);
			viewHolder.comment_txt = (TextView) rowView.findViewById(R.id.home_kontakt_big_comment_txt);
			viewHolder.open_txt = (TextView) rowView.findViewById(R.id.home_kontakt_big_open_txt);
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
			
			
			if(obj.getItem_name() != null && !obj.getItem_name().equals("")){
				holder.title.setText(obj.getItem_name());
			} else if(obj.getEvent_typ() == "mb") {
				holder.title.setText("Microblog von "+obj.getVon().getUsername());			
			}else{			
				holder.title.setText(obj.getEvent_name());
			}
			
			String temp = HomeKontaktHelper.getFullText(obj)+"\n";
			if(obj.getItem_author() != null){
				temp += obj.getItem_author().getUsername();
			} else {
				temp += obj.getVon().getUsername();
			}
			if(obj.getEmpholen_von() != null && obj.getEmpholen_von().size() > 0){
				temp += "(Empfohlen von: ";
				for (int i = 0; i < obj.getEmpholen_von().size(); i++) {
					temp += obj.getEmpholen_von().get(i).getUsername();
					if(i < obj.getEmpholen_von().size()-1) temp += ", ";
				}
				temp += ")";
			}
			holder.date.setText(obj.getDate_server());
			holder.info.setText(temp);
			
			if(obj.isCommentable()){		
				holder.comment_txt.setText(obj.getComment_count()+" Kommentare");
				holder.comment.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						context.showComments(obj.getId());
					}
					
				});
				holder.comment.setVisibility(View.VISIBLE);
				if(obj.getComment_count() > 0){
				} else {
					holder.comment_txt.setText("Kommentar schreiben");
				}
			} else {
				holder.comment.setVisibility(View.GONE);
			}
			
			if(obj.getLink() != null)
				holder.open.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					Uri uri = Uri.parse(obj.getLink());
					if(obj.getLink().contains("fanart")){
						Intent i=new Intent(context,Fanart.class);
						i.putExtra("fanwork_id", Long.parseLong(uri.getLastPathSegment()));
						context.startActivity(i);
						return;
					}
					try{
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						context.startActivity(intent);
					} catch(Exception e){
						Toast.makeText(context, "Das ist noch nicht möglich.", Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
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
