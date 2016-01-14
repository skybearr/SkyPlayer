package com.example.skyplayer;

public class Music {

	private long id;
	
	private String url;
	
	private long size;
	
	private String title;
	
	private int lasttime;
	
	private String artist;
	
	/**音乐id*/
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**路径*/
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**大小*/
	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	/**标题*/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**时长：秒*/
	public int getLasttime() {
		return lasttime;
	}

	public void setLasttime(int lasttime) {
		this.lasttime = lasttime / 1000;
	}

	/**歌手*/
	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
