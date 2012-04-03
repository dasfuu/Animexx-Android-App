package de.meisterfuu.animexx.GB;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.profil.UserPopUp;

public class GBAdapter extends ArrayAdapter<GBObject> {
	private final Activity context;
	private final GBObject[] names;

	static class ViewHolder {
		public WebView text,imgA;
		public ImageView image;
		public TextView von, time;
	}

	public GBAdapter(Activity context, GBObject[] names) {
		super(context, R.layout.gblist, names);
		this.context = context;
		this.names = names;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.gblist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text =  (WebView)   rowView.findViewById(R.id.gbentry);
			viewHolder.image = (ImageView) rowView.findViewById(R.id.img);
			viewHolder.von =   (TextView)  rowView.findViewById(R.id.von);
			viewHolder.time =  (TextView)  rowView.findViewById(R.id.time);
			viewHolder.imgA =  (WebView)   rowView.findViewById(R.id.imgA);
			rowView.setTag(viewHolder);
		}
  
		ViewHolder holder = (ViewHolder) rowView.getTag();
		final GBObject s = names[position];		  
		//String data = "<body><img src=\"file:///android_asset/large_image.png\"/></body>";    
		if (s.avatar != "" && s.avatar != "null" && s.avatar != null) holder.imgA.loadUrl(s.avatar);
		else holder.imgA.setVisibility(View.GONE);
		holder.text.loadDataWithBaseURL("fake://fake.de", s.text, "text/html", "UTF-8", null);
		holder.imgA.loadUrl(s.avatar);
		holder.image.setImageResource(R.drawable.mail);
		holder.von.setText(s.von);
		holder.time.setText(s.time);
		holder.von.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	UserPopUp Menu = new UserPopUp(context, s.von, s.von_id);
            	Menu.PopUp();
            	
            	/*
				Bundle bundle = new Bundle();
				bundle.putString("id",s.von_id);
				Intent newIntent = new Intent(context.getApplicationContext(), GBViewList.class);
				newIntent.putExtras(bundle);
				context.startActivity(newIntent);
				*/
				
               }
           });      
   
		return rowView;
	}
}
