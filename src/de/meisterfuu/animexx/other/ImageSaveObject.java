package de.meisterfuu.animexx.other;


public class ImageSaveObject{

	String url;
	String name;
	
	public ImageSaveObject(String url, String name) {
		super();
		this.url = url;
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString(){
		return this.getUrl();
	}
	
}
