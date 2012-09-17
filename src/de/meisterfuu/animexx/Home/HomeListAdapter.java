package de.meisterfuu.animexx.Home;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloader;
import de.meisterfuu.animexx.other.SingleImage;
import de.meisterfuu.animexx.other.VerticalTextView;

public class HomeListAdapter extends ArrayAdapter<HomeListObject> {

	private final Activity context;
	private ArrayList<HomeListObject> names;
	public ImageDownloader Images;

	static class ViewHolder {

		public TextView text, txinfo;
		public VerticalTextView txtyp;
		public LinearLayout Color;
		//public WebView IMG;
		public ImageView IMG2;
	}


	public HomeListAdapter(Activity context, ArrayList<HomeListObject> names) {
		super(context, R.layout.enslist, names);
		this.context = context;
		this.names = names;
	}


	public void refill(ArrayList<HomeListObject> names) {
		this.names = names;
		Log.i("Anzahl", "Im Array sind " + names.size() + " Elemente.");
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.calist, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) rowView.findViewById(R.id.CAtxt);
			viewHolder.txinfo = (TextView) rowView.findViewById(R.id.CAtxinfo);
			viewHolder.txtyp = (VerticalTextView) rowView.findViewById(R.id.txType);
			viewHolder.Color = (LinearLayout) rowView.findViewById(R.id.CAcolor);
			//viewHolder.IMG = (WebView) rowView.findViewById(R.id.webIMG);
			viewHolder.IMG2 = (ImageView) rowView.findViewById(R.id.webIMG2);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final HomeListObject s = names.get(position);
		holder.text.setText(s.getFinishedText());
		holder.txinfo.setText(Helper.DateToString(s.getTime(), false));

		//holder.IMG.setVisibility(View.GONE);
		if(s.getImgURL() != null && s.getImgURL() != "" && s.getImgURL() != "none"){
			//holder.IMG.loadData("", "", "");
			//holder.IMG.loadUrl(s.getImgURL());
			//holder.IMG.setBackgroundResource(R.color.custom_theme_color);
			Images.download(s.getImgURL(), holder.IMG2);
			holder.IMG2.setVisibility(View.VISIBLE);
		} else {
			holder.IMG2.setVisibility(View.GONE);
		}

		if (s.getTyp() == HomeListObject.KONTAKTE) {
			holder.txtyp.setText("Kontakte");
			holder.txtyp.setBackgroundResource(R.color.bg_purple);
		} else if (s.getTyp() == HomeListObject.WEBLOGKOMMENTAR) {
			holder.txtyp.setText("Weblog Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_red);
		} else if (s.getTyp() == HomeListObject.FANARTKOMMENTAR) {
			holder.txtyp.setText("Fanart Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_blue);
		} else if (s.getTyp() == HomeListObject.DOJINSHIKOMMENTAR) {
			holder.txtyp.setText("Dojinshi Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_green);
		} else if (s.getTyp() == HomeListObject.FANFICKOMMENTAR) {
			holder.txtyp.setText("Fanfic Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_blue2);
		} else if (s.getTyp() == HomeListObject.FANWORKKOMMENTAR) {
			holder.txtyp.setText("Fanwork Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_lightred);
		} else if (s.getTyp() == HomeListObject.UMFRAGEKOMMENTAR) {
			holder.txtyp.setText("Umfrage Kommentar");
			holder.txtyp.setBackgroundResource(R.color.bg_green2);
		} else if (s.getTyp() == HomeListObject.COSPLAY) {
			holder.txtyp.setText("Cosplay");
			holder.txtyp.setBackgroundResource(R.color.bg_lightgreen);
		}

		rowView.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Intent newIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getURL()));
				newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP	| Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_FROM_BACKGROUND);
				context.startActivity(newIntent);
			}

		});

		holder.IMG2.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("URL", s.getImgURL().replaceAll(".thum.jpg", ".jpg"));
				Intent newIntent = new Intent(context.getApplicationContext(),
						SingleImage.class);
				newIntent.putExtras(bundle);
				context.startActivity(newIntent);
			}

		});

		return rowView;
	}

	@Override
	public int getCount(){
		return names.size();
	}
}