package de.meisterfuu.animexx.other;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Color;

public class SingleImage extends Activity  {

	TouchImageView Image;
	String URL;
	ImageDownloader ImageLoader = new ImageDownloader();


	@Override
    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        //setContentView(R.layout.image_single);
	        //Image = (TouchImageView) findViewById(R.id.imageView1);
	        
	        Image = new TouchImageView(this);
			
	        if (this.getIntent().hasExtra("URL")) {
			Bundle bundle = this.getIntent().getExtras();
			URL = bundle.getString("URL");
	        } else this.finish();
	        
	        ImageLoader.download(URL, Image, false);
	        setContentView(Image);
	        Image.setBackgroundColor(Color.BLACK);
	        
	    }
}
