package edu.ptit.xbmc.model;

import java.io.Serializable;

public class Song implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6722683265243705995L;
	String name;
	String[] artist;
	String album;
	String thumbnail;
	int duration, songid, albumid, track;

	public int getTrack() {
		return track;
	}

	public void setTrack(int track) {
		this.track = track;
	}

	public int getAlbumid() {
		return albumid;
	}

	public void setAlbumid(int albumid) {
		this.albumid = albumid;
	}

	public Song() {
		duration = 0;
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public String formatDuration() {
		String ret = "";
		ret += (duration / 60) + ":";
		ret += duration % 60;
		return ret;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getArtist() {
		return artist;
	}

	public void setArtist(String[] artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public int getDuration() {
		return duration;
	}

	public String getFormatedDuration() {
		String ret = "";
		ret += (duration / 60);
		ret += ":";
		int du = duration % 60;
		if (du < 10)
			ret += "0";
		ret += du;
		return ret;

	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getSongid() {
		return songid;
	}

	public void setSongid(int songid) {
		this.songid = songid;
	}

	public String getFormatedArtist() {
		// String first = artist == null? "unknown":artist[0];
		// return first;
		String ret = "";
		if (artist == null || artist.length == 0 || artist[0] == "")
			return "Unknown";
		for (int i = 0; i < artist.length - 1; i++) {
			ret += artist[i] + " ft. ";
		}
		ret += artist[artist.length - 1];
		return ret;
	}

}
