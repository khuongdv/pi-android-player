package edu.ptit.xbmc.model;

public class Album {
	private String description, title, displayartist,thumbnail;
	private int year, albumid;
	public Album(){}
	
	//----Getters and setters----
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlbumtitle() {
		return title;
	}

	public void setAlbumtitle(String title) {
		this.title = title;
	}

	public String getDisplayartist() {
		return displayartist;
	}

	public void setDisplayartist(String displayartist) {
		this.displayartist = displayartist;
	}

	public int getAlbumid() {
		return albumid;
	}

	public void setAlbumid(int albumid) {
		this.albumid = albumid;
	}

	public void setArtistArray(String[] artist) {
		// TODO Auto-generated method stub
		StringBuffer art = new StringBuffer();
		for(int i = 0; i < artist .length; i++){
			art.append(artist[i]);
		}
		displayartist = art.toString();
	}
	
}
