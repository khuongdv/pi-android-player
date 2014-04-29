package edu.ptit.xbmc.model;


public class Artist {
	String artistName, description;		
	int artistID ;
	String thumbnail;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public boolean isThumbnailAvailable(){
		if(thumbnail == null ) return false;
		if(thumbnail .equals("")) return false;
		return true;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getArtistID() {
		return artistID;
	}

	public void setArtistID(int artistID) {
		this.artistID = artistID;
	}

	public Artist() {
		super();
	}
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	
	
}
