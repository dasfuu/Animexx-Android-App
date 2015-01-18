package de.meisterfuu.animexx.Home;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragment;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloader;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeKontaktDetailFragment extends SherlockFragment implements Refreshable {
	
	HomeKontaktObject obj;
	
	ImageView img;
	TextView title, author, date, type, comment_date;
	Button open, comment;
	ImageDownloader img_loeader;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_home_kontakt_detail_fragment, null);
		title = (TextView) view.findViewById(R.id.home_kontakt_detail_title);
		author = (TextView) view.findViewById(R.id.home_kontakt_detail_author);
		date = (TextView) view.findViewById(R.id.home_kontakt_detail_date);
		type = (TextView) view.findViewById(R.id.home_kontakt_detail_info);
		comment_date = (TextView) view.findViewById(R.id.home_kontakt_detail_comment_date);
		
		open = (Button) view.findViewById(R.id.home_kontakt_detail_open);
		comment = (Button) view.findViewById(R.id.home_kontakt_detail_comment);
		
		img = (ImageView) view.findViewById(R.id.fragment_microblog_img);
		
		img_loeader = new ImageDownloader();
		
		if (getArguments() != null && getArguments().getCharArray("data") != null) {

			obj = new HomeKontaktObject();
			try {
				obj.parseJSON(new JSONObject(new String(getArguments().getCharArray("data"))));				
				
				if(obj.getItem_image_big() != null){
					img.setVisibility(View.VISIBLE);
					img_loeader.download(obj.getItem_image(), img, false);
					img_loeader.download(obj.getItem_image_big(), img, true);
				} else {
					img.setVisibility(View.GONE);
				}
				
				title.setText(obj.getItem_name());
				if(obj.getItem_author() != null){
					author.setText(obj.getItem_author().getUsername());
				} else {
					author.setText(obj.getVon().getUsername());
				}
				date.setText(obj.getDate_server());
				type.setText(HomeKontaktHelper.getFullText(obj));
				
				if(obj.isCommentable()){		
					comment.setText(obj.getComment_count()+" Kommentare");
					comment.setOnClickListener(new OnClickListener(){

						public void onClick(View v) {
							((HomeKontaktActivity)getActivity()).showComments(obj.getId());
						}
						
					});
					comment_date.setText(obj.getComment_date());
					comment.setVisibility(View.VISIBLE);
					if(obj.getComment_count() > 0){
						comment_date.setVisibility(View.VISIBLE);
					} else {
						comment_date.setVisibility(View.GONE);
						comment.setText("Schreibe den ersten Kommentar!");
					}
				} else {
					comment.setVisibility(View.GONE);
					comment_date.setVisibility(View.GONE);
				}
				
				if(obj.getLink() != null)
				open.setOnClickListener(new OnClickListener(){

					public void onClick(View v) {
						Uri uri = Uri.parse(obj.getLink());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);						
					}
					
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} else {

		}
		
		return view;
	}

	public static HomeKontaktDetailFragment newInstance(String data) {
		HomeKontaktDetailFragment myFragment = new HomeKontaktDetailFragment();

		Bundle args = new Bundle();
		args.putCharArray("data", data.toCharArray());
		myFragment.setArguments(args);

		return myFragment;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
