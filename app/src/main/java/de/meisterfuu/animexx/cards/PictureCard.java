package de.meisterfuu.animexx.cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.fima.cardsui.objects.Card;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.other.ImageDownloaderCustom;
import de.meisterfuu.animexx.other.ImageSaveObject;


public class PictureCard extends Card {
	
	String url;
	ImageDownloaderCustom downloader;
	String name;

	public PictureCard(String url, String name, ImageDownloaderCustom downloader){
		super();		
		this.url = url;
		this.name = name;
		this.downloader = downloader;
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_picture, null);
		
		ImageView img = (ImageView) view.findViewById(R.id.card_picture_img);
		downloader.download(new ImageSaveObject(url, name), img);
		
		return view;
	}

    @Override
    public boolean convert(View convertCardView) {
        return false;
    }


}
