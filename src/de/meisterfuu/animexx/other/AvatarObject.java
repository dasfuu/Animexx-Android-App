package de.meisterfuu.animexx.other;



public class AvatarObject {

	String url;
	long id;


	public AvatarObject(String url, long id) {
		this.setUrl(url);
		this.setId(id);
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}

}
