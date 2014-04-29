package edu.ptit.xbmc.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import android.app.Activity;
import edu.ptit.xbmc.fragments.FragmentAlbumDetail;
import edu.ptit.xbmc.fragments.FragmentAlbumList;
import edu.ptit.xbmc.fragments.FragmentAllSong;
import edu.ptit.xbmc.fragments.FragmentArtistDetail;
import edu.ptit.xbmc.fragments.FragmentArtistList;
import edu.ptit.xbmc.fragments.FragmentDirectRemote;
import edu.ptit.xbmc.fragments.FragmentNowPlaying;
import edu.ptit.xbmc.fragments.FragmentSettings;
import edu.ptit.xbmc.model.Song;

public class Utils {
	private static Utils instance = null;
//	public FragmentAlbum FRAGMENT_ALBUM;
	public int ALBUM_SCREEN_NO = FIRST_SCREEN;
	public List<Song> ALLSONG_LIST;
//	public FragmentArtist FRAGMENT_ARTIST;
	public int ARTIST_SCREEN_NO = FIRST_SCREEN;
	public FragmentAlbumList FRAGMENT_ALBUM_LIST;
	public FragmentAlbumDetail FRAGMENT_ALBUM_DETAIL;
	public FragmentAllSong FRAGMENT_ALL_SONG;
	public FragmentArtistList FRAGMENT_ARTIST_LIST;
	public FragmentArtistDetail FRAGMENT_ARTIST_DETAIL;
	public FragmentDirectRemote FRAGMENT_DIRECT_REMOTE;
	public FragmentNowPlaying FRAGMENT_NOW_PLAYING;
	public FragmentSettings FRAGMENT_SETTINGS;
	public static final int FIRST_SCREEN = 0;
	public static final int SECOND_SCREEN = 1;
	Activity act;
	public void setActivity(Activity ac){
		act = ac;
	}
	private Utils() {
	}
	public static String getFormatedDuration(int duration){
		String ret = "";
		ret += (duration / 60);
		ret += ":";
		int du = duration % 60;
		if(du < 10) ret += "0";
		ret += du;
		return ret;		
	}
	public static Utils getInstance() {
		if (instance == null)
			instance = new Utils();
		return instance;
	}

	/**
	 * Method to convert song object into a byte array
	 * 
	 */
	public static byte[] serialize(Song obj) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(out);
			os.writeObject(obj);
			os.close();
		} catch (Exception e) {
		}
		return out.toByteArray();
	}
	/**
	 * Retrieve song object from byte array
	 */
	public static Song deserialize(byte[] data) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		Song s = (Song) is.readObject();
		is.close();
		in.close();
		return s;
	}
	// new project
//	public void switchFragmentInArtistTab(Fragment root,
//			Fragment fragment) {
//		FragmentTransaction ft = root.getFragmentManager().beginTransaction();
//		ft.replace(R.id.artist_content_wrapper, fragment);
//		ft.commit();
//		if(fragment instanceof FragmentArtistList) ARTIST_SCREEN_NO = FIRST_SCREEN;
//		else ARTIST_SCREEN_NO = SECOND_SCREEN;
//	}
//	public void switchFragmentInAlbumTab(Fragment root,
//			Fragment fragment) {
//		// TODO Auto-generated method stub
//		FragmentTransaction ft = root.getFragmentManager().beginTransaction();
//		ft.replace(R.id.album_content_wrapper, fragment);
//		ft.commit();
//		if(fragment instanceof FragmentAlbumList) ALBUM_SCREEN_NO = FIRST_SCREEN;
//		else ALBUM_SCREEN_NO = SECOND_SCREEN;
//	}
	public void switchTab(int position){
		act.getActionBar().setSelectedNavigationItem(position);
	}
}
