package com.example.skyplayer;

public class Music {

	private long id;
	
	private String url;
	
	private long size;
	
	private String title;
	
	private int lasttime;
	
	private String artist;
	
	/**����id*/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**·��*/
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**��С*/
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**����*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**ʱ������*/
	public int getLasttime() {
		return lasttime;
	}

	public void setLasttime(int lasttime) {
		this.lasttime = lasttime / 1000;
	}

	/**����*/
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
