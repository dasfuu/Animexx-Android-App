package de.meisterfuu.animexx.other;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;

import de.meisterfuu.animexx.Helper;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.Request;


public class MultiSherlockListActivity extends SherlockListActivity {

	protected ArrayList<MultiListObject> Array = new ArrayList<MultiListObject>();	
	protected MultiListAdapter adapter;
	

	
	protected void addText(String text){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.TEXT);
		tempObject.setText(text);
		Array.add(tempObject);
	}
	
	protected void addText(String text, OnClickListener listener){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.TEXT);
		tempObject.setText(text);
		tempObject.setOnclicklistener(listener);
		Array.add(tempObject);
	}

	
	protected void addTitle(String title){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.TITLE);
		tempObject.setText(title);
		Array.add(tempObject);
	}
	
	protected void addTitle(String title, OnClickListener listener){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.TITLE);
		tempObject.setText(title);
		tempObject.setOnclicklistener(listener);
		Array.add(tempObject);
	}
	
	protected void addSpacer(){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.SPACER);
		Array.add(tempObject);
	}
	
	protected void addLogo(String PictureURL){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.LOGO);
		tempObject.setPictureURL(PictureURL);
		Array.add(tempObject);
	}
	
	protected void addLogo(String PictureURL, OnClickListener listener){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.LOGO);
		tempObject.setPictureURL(PictureURL);
		tempObject.setOnclicklistener(listener);
		Array.add(tempObject);
	}
	
	protected void addPicture(String PictureURL, String text){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.PICTURE);
		tempObject.setPictureURL(PictureURL);
		tempObject.setText(text);
		Array.add(tempObject);
	}
	
	protected void addPicture(String PictureURL, String text, OnClickListener listener){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.PICTURE);
		tempObject.setPictureURL(PictureURL);
		tempObject.setText(text);
		tempObject.setOnclicklistener(listener);
		Array.add(tempObject);
	}
	
	protected void addPicture(int PictureID, String text, OnClickListener listener){
		MultiListObject tempObject = new MultiListObject();
		tempObject.setTyp(MultiListObject.PICTURE);
		tempObject.setPictureID(PictureID);
		tempObject.setText(text);
		tempObject.setOnclicklistener(listener);
		Array.add(tempObject);
	}
	
}
