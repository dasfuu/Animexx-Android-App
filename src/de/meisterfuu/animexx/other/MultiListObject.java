package de.meisterfuu.animexx.other;

import android.view.View.OnClickListener;

public class MultiListObject {

	private String text;
	private String info;
	private String pictureURL;
	private int pictureID;
	private int typ = 0;
	private OnClickListener onclicklistener;

	public static final int NORMAL = 0;
	public static final int LOGO = 1;
	public static final int PICTURE = 2;
	public static final int TEXT = 3;
	public static final int TITLE = 4;
	public static final int SPACER = 5;


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public String getPicture() {
		return pictureURL;
	}


	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
		pictureID = 0;
	}

	
	public void setPictureID(int pictureID) {
		this.pictureID = pictureID;
	}
	
	public int getPictureID(){
		return pictureID;
	}

	public int getTyp() {
		return typ;
	}


	public void setTyp(int typ) {
		this.typ = typ;
	}


	public OnClickListener getOnclicklistener() {
		return onclicklistener;
	}


	public void setOnclicklistener(OnClickListener onclicklistener) {
		this.onclicklistener = onclicklistener;
	}

}
