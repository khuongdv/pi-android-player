package edu.ptit.xbmc.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import edu.ptit.xbmc.model.Album;
import edu.ptit.xbmc.model.Artist;
import edu.ptit.xbmc.model.Settings;
import edu.ptit.xbmc.model.Song;

public class PiConnector {
	ArrayList<Song> allSong = new ArrayList<Song>();
	ArrayList<Album> albumList = new ArrayList<Album>();
	ArrayList<Artist> artistList = new ArrayList<Artist>();
	Album currentAlbum;
	Artist currentArtist;
	// khuongdv start oct 7
//	Context context;

	// khuongdv end oct 7

	public void setPiURL(String piURL) {
		this.piURL = piURL;
		allSong = null;
		albumList = null;
		artistList = null;		
	}

	/**
	 * @param args
	 */
	private static PiConnector instance = null;
	private String piURL = null;

	public String getPiURL() {
		return piURL;
	}

	private PiConnector() {	
		audioLibraryScan();
//		allSong = getAllSongsFromPI();
		albumList = getAlbumsFromPI();
		artistList = getArtistsFromPI();
	}

	
	/**
	 * Scan library for new item
	 */
	public void audioLibraryScan() {
		String LIBRARY_SCAN = "{\"jsonrpc\":\"2.0\",\"id\":\"scan\",\"method\":\"AudioLibrary.Scan\"}";
		sendRequest(LIBRARY_SCAN);
		// khuongdv start sep28
//		allSong = getAllSongsFromPI();
		albumList = getAlbumsFromPI();
		artistList = getArtistsFromPI();
		// khuongdv end sep28
	}

	public static PiConnector getInstance() {
		if (instance == null)
			instance = new PiConnector();
		return instance;
	}

	public Album getCurrentAlbum() {
		return currentAlbum;
	}

	public void setCurrentAlbum(Album currentAlbum) {
		this.currentAlbum = currentAlbum;
	}

	public Artist getCurrentArtist() {
		return currentArtist;
	}

	public void setCurrentArtist(Artist currentArtist) {
		this.currentArtist = currentArtist;
	}

	public ArrayList<Song> getAllSongs() {
		return allSong;
	}

	public void setAllSong(ArrayList<Song> allSong) {
		this.allSong = allSong;
	}

	
	public void playsong(int i) {
		String PLAY_SONG = "{\"jsonrpc\": \"2.0\", \"id\" : 1, "
				+ "\"method\": \"Player.Open\","
				+ "\"params\":{\"item\": {\"playlistid\":0,\"position\":" + i
				+ "}}}";
		sendRequest(PLAY_SONG);
	}

	// Methods for settings

	public boolean checkURLandPort(String surl, int port) {
		String fullURL = "http://" + surl + ":" + port + "/jsonrpc";
		try {
			// Oct 12
			String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"JSONRPC.Ping\"}";
			StringBuffer resp = sendRequest(fullURL, REQUEST);
			JSONObject JREsp = new JSONObject(resp.toString());
			String pong = JREsp.optString("result", null);
			if (pong != null && pong.trim().equals("pong")) {
				this.setPiURL("http://" + surl + ":" + port);
				return true;
			}
			// End Oct 12
		} catch (Exception e) {
			 Log.e("ERRORORROROR", e.getMessage());
//			e.printStackTrace();
			return false;
		}

		return false;
	}
//	/**
//	 * Get all song from Pi
//	 * 
//	 * @return
//	 */
//	public ArrayList<Song> getAllSongsFromPI() {
//		// Oct 11
//		/**
//		 * This block do these tasks: Clear all items in playlist.(1) Get all
//		 * song in library (2) Add all of them to playlist. (3) Get all song
//		 * using Playlist.GetItems (4)
//		 */
//		String CLEARPLAYLIST = "{\"jsonrpc\":\"2.0\",\"id\":\"clear_playlist\","
//				+ "\"method\":\"Playlist.Clear\","
//				+ "\"params\":{\"playlistid\":0}}";
//		sendRequest(CLEARPLAYLIST); // (1)
//
//		// (2)
//		String GETALLSONGREQUEST = "{\"jsonrpc\": \"2.0\", \"method\": \"AudioLibrary.GetSongs\","
//				+ " \"params\": { \"limits\": { \"start\" : 0 }, \"properties\": [ ]}, \"id\": \"getsongs1\"}";
//
//		try {
//
//			StringBuffer response = new StringBuffer();
//			response = sendRequest(GETALLSONGREQUEST);
//			JSONObject jO = new JSONObject(response.toString());
//			JSONObject result = jO.optJSONObject("result");
//			JSONArray array = result.optJSONArray("songs");
//			JSONObject t = new JSONObject();
//			// StringBuffer batch = new StringBuffer("");
//			// Oct 15
//			List<Song> tmplist = new ArrayList<Song>();
//			Song s;
//			for (int i = 0; i < array.length() - 1; i++) {
//				t = array.getJSONObject(i);
//				s = new Song();
//				s.setSongid(t.optInt("songid", -1));
//				s.setName(t.optString("label", ""));
//				tmplist.add(s);
//			}
//			Collections.sort(tmplist, new Comparator<Song>() {
//
//				@Override
//				public int compare(Song lhs, Song rhs) {
//					return lhs.getName().compareTo(rhs.getName());
//				}
//			});
//			String str = "";
//			for (int i = 0; i < tmplist.size(); i++) {
//				int id = tmplist.get(i).getSongid();
//				str = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\","
//						+ "\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
//						+ id + "}}}";
//				sendRequest(str);
//			}
//			// End oct 27
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//
//		// End Oct 11
//		// (4)
//		allSong = new ArrayList<Song>();
//		String REQUEST = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.GetItems\","
//				+ "\"params\":{\"playlistid\":0,"
//				+ "\"limits\":{\"start\":0, \"end\":19},\"properties\":[\"artist\",\"duration\",\"albumid\",\"album\",\"track\",\"thumbnail\"]"
//				+
//				// "\"sort\":{\"order\":\"ascending\",\"method\":\"label\",\"ignorearticle\":true}},"
//				// +
//				"},\"id\":\"getsongs\"}";
//		try {
//			StringBuffer response = new StringBuffer();
//			response = sendRequest(REQUEST);
//			// Log.e("RES",response.toString());
//			JSONObject jO = new JSONObject(response.toString());
//			JSONObject result = jO.optJSONObject("result");
//			JSONArray array = result.optJSONArray("items");
//			JSONObject t = new JSONObject();
//
//			for (int i = 0; i < array.length(); i++) {
//				t = array.getJSONObject(i);
//				Song s = new Song();
//				s.setSongid(t.optInt("songid"));
//				s.setAlbum(t.optString("album"));
//				s.setDuration(t.optInt("duration"));
//				s.setAlbumid(t.optInt("albumid"));
//				s.setName(t.optString("label"));
//				s.setTrack(t.optInt("track", 0));
//				s.setThumbnail(t.optString("thumbnail"));
//				JSONArray artistJSON = t.optJSONArray("artist");
//				String[] artist = new String[artistJSON.length()];
//				for (int j = 0; j < artistJSON.length(); j++) {
//					artist[j] = artistJSON.optString(j);
//				}
//				s.setArtist(artist);
//				allSong.add(s);
//			}
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		// if (allSong != null && allSong.size() > 0)
//		// addToPlaylist();
//
//		return allSong;
//	}
	// Oct 27
	public void refreshSonglist(){
		String CLEARPLAYLIST = "{\"jsonrpc\":\"2.0\",\"id\":\"clear_playlist\","
				+ "\"method\":\"Playlist.Clear\","
				+ "\"params\":{\"playlistid\":0}}";
		sendRequest(CLEARPLAYLIST); // (1)
		
		String str = "";
		for (int i = 0; i < Utils.getInstance().ALLSONG_LIST.size(); i++) {
			int id = Utils.getInstance().ALLSONG_LIST.get(i).getSongid();
			str = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\","
					+ "\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
					+ id + "}}}";
			sendRequest(str);
		}
		
		
	}
	public void addAllSongToPlaylist(){
		/**
		 * This block do these tasks: Clear all items in playlist.(1) Get all
		 * song in library (2) Add all of them to playlist. (3) Get all song
		 * using Playlist.GetItems (4)
		 */
		String CLEARPLAYLIST = "{\"jsonrpc\":\"2.0\",\"id\":\"clear_playlist\","
				+ "\"method\":\"Playlist.Clear\","
				+ "\"params\":{\"playlistid\":0}}";
		sendRequest(CLEARPLAYLIST); // (1)

		// (2)
		String GETALLSONGREQUEST = "{\"jsonrpc\": \"2.0\", \"method\": \"AudioLibrary.GetSongs\","
				+ " \"params\": { \"limits\": { \"start\" : 0 }, \"properties\": [ ]}, \"id\": \"getsongs1\"}";

		try {

			StringBuffer response = new StringBuffer();
			response = sendRequest(GETALLSONGREQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("songs");
			JSONObject t = new JSONObject();
			// StringBuffer batch = new StringBuffer("");
			// Oct 15
			List<Song> tmplist = new ArrayList<Song>();
			Log.e("So bai hat", array.length() + "");
			Song s;
			for (int i = 0; i < array.length() - 1; i++) {
				t = array.getJSONObject(i);
				s = new Song();
				s.setSongid(t.optInt("songid", -1));
				s.setName(t.optString("label", ""));
				tmplist.add(s);
			}
			Collections.sort(tmplist, new Comparator<Song>() {

				@Override
				public int compare(Song lhs, Song rhs) {
					return lhs.getName().compareTo(rhs.getName());
				}
			});
			Utils.getInstance().ALLSONG_LIST = tmplist;
			String str = "";
			for (int i = 0; i < Utils.getInstance().ALLSONG_LIST.size(); i++) {
				int id = Utils.getInstance().ALLSONG_LIST.get(i).getSongid();
				str = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.Add\","
						+ "\"params\":{\"playlistid\":0,\"item\":{\"songid\":"
						+ id + "}}}";
				sendRequest(str);
			}
			
			// End oct 27
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
		public ArrayList<Song> getSpecificSongs(int start, int end) {
			if (allSong == null || allSong.size() <= 0) {				
				addAllSongToPlaylist();
			}
			ArrayList<Song> list = new ArrayList<Song>();
			String REQUEST = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.GetItems\","
					+ "\"params\":{\"playlistid\":0,"
					+ "\"limits\":{\"start\":"
					+ start
					+ ", \"end\":"
					+ end
					+ "},\"properties\":[\"artist\",\"duration\",\"albumid\",\"album\",\"track\",\"thumbnail\"]"
					+ "},\"id\":\"getsongs\"}";
			try {
				StringBuffer response = new StringBuffer();
				response = sendRequest(REQUEST);
				// Log.e("RES",response.toString());
				JSONObject jO = new JSONObject(response.toString());
				JSONObject result = jO.optJSONObject("result");
				JSONArray array = result.optJSONArray("items");
				JSONObject t = new JSONObject();
//				Log.e("So bai hat trong getspecifics",array.length() + "");
				for (int i = 0; i < array.length(); i++) {
					t = array.getJSONObject(i);
					Song s = new Song();
					s.setSongid(t.optInt("songid"));
					s.setAlbum(t.optString("album"));
					s.setDuration(t.optInt("duration"));
					s.setAlbumid(t.optInt("albumid"));
					s.setName(t.optString("label"));
					s.setTrack(t.optInt("track", 0));
					s.setThumbnail(t.optString("thumbnail"));
					JSONArray artistJSON = t.optJSONArray("artist");
					String[] artist = new String[artistJSON.length()];
					for (int j = 0; j < artistJSON.length(); j++) {
						artist[j] = artistJSON.optString(j);
					}
					s.setArtist(artist);
					list.add(s);
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return list;
		}

		// End Oct 27
	// --- Get songlist by keyword
//	public ArrayList<Song> getSongsByKeyword(String key) {
//
//		if (allSong == null || allSong.size() <= 0) {
//			Log.e("ALLSONG.GETCOUNT", "Vi pham dieu kien!");
//			getAllSongsFromPI();
//		}
//		if (key.equals("") || key == null) {
//			Log.e("ALLSONG.GETCOUNT", allSong.size() + "");
//			return allSong;
//		}
//		ArrayList<Song> list = new ArrayList<Song>();
//		key = key.toLowerCase();
//		for (Song s : allSong) {
//			if (s.getName().toLowerCase().contains(key)
//					|| s.getFormatedArtist().toLowerCase().contains(key))
//				list.add(s);
//		}
//		return list;
//	}

	private ArrayList<Album> getAlbumsFromPI() {
		albumList = new ArrayList<Album>();
		try {

			StringBuffer response = new StringBuffer();

			String REQUEST = "{\"jsonrpc\": \"2.0\", \"method\": \"AudioLibrary.GetAlbums\","
					+ " \"params\": { \"limits\": { \"start\" : 0,\"end\":19 },"
					+ "\"properties\": [ \"description\",\"artist\",\"genre\",\"rating\",\"title\",\"year\",\"thumbnail\"], "
					+ "\"sort\": { \"order\": \"ascending\", \"method\": \"artist\"} }, \"id\": \"getalbums\"}";

			response = sendRequest(REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("albums");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				t = array.getJSONObject(i);
				JSONArray artistJSON = t.optJSONArray("artist");
				String[] artist = new String[artistJSON.length()];
				for (int j = 0; j < artistJSON.length(); j++) {
					artist[j] = artistJSON.optString(j);
				}
				t = array.getJSONObject(i);
				Album temp = new Album();
				temp.setAlbumid(t.optInt("albumid"));
				temp.setAlbumtitle(t.optString("title"));
				temp.setDescription(t.optString("description"));
				temp.setYear(t.optInt("year"));
				temp.setThumbnail(t.optString("thumbnail"));
				temp.setArtistArray(artist);
				albumList.add(temp);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return albumList;
	}

	public ArrayList<Album> getAlbumsByKeyword(String string) {
		string = string.toLowerCase();
		if (albumList == null || albumList.size() < 1) {
			albumList = getAlbumsFromPI();
		}
		ArrayList<Album> list = new ArrayList<Album>();
		for (Album a : albumList) {
			if (a.getAlbumtitle().toLowerCase().contains(string)
					|| a.getDescription().toLowerCase().contains(string)
					|| a.getDisplayartist().toLowerCase().contains(string)) {
				list.add(a);
			}
		}
		return list;
	}

	public ArrayList<Artist> getArtistsByKeyword(String string) {
		ArrayList<Artist> list = new ArrayList<Artist>();
		if (artistList == null || artistList.size() < 1) {
			artistList = getArtistsFromPI();
		}
		string = string.toLowerCase();
		for (Artist a : artistList) {
			if (a.getArtistName().toLowerCase().contains(string)) {
				list.add(a);
			}
		}
		return list;
	}

	// ____________________Requester_______________________

	private StringBuffer sendRequest(String REQUEST) {
		StringBuffer response = new StringBuffer();
		URL url = null;
		HttpURLConnection urlConnection = null;
		DataOutputStream das = null;
		try {
			url = new URL(piURL + "/jsonrpc");
			// Log.e("URL", piURL + "/jsonrpc");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type",
					"application/json; charset=utf-8");
			urlConnection.setRequestProperty("Content-Language", "vi-VN");
			urlConnection.setUseCaches(false);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			das = new DataOutputStream(urlConnection.getOutputStream());

			das.writeBytes(REQUEST);
			InputStream is = urlConnection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			is.close();
			das.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			Log.i("Exception", " " + e.getCause() + " " + e.getMessage());
		}
		return response;
	}

	// OCt 12
	// Another requester
	private StringBuffer sendRequest(String fullURL, String REQUEST) {
		StringBuffer response = new StringBuffer();
		URL url = null;
		HttpURLConnection urlConnection = null;
		DataOutputStream das = null;
		try {
			url = new URL(fullURL);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type",
					"application/json; charset=utf-8");
			urlConnection.setRequestProperty("Content-Language", "vi-VN");
			urlConnection.setUseCaches(false);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			das = new DataOutputStream(urlConnection.getOutputStream());
			das.writeBytes(REQUEST);
			InputStream is = urlConnection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			is.close();
			das.close();
			urlConnection.disconnect();
		} catch (Exception e) {
			Log.e("SENDREQUEST", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// End Oct 12

	/**
	 * Get current state of active player percentage, artist, title, thumbnail
	 * 
	 * @return
	 */
	public Hashtable<String, String> getCurrentStateOfActivePlayer() {
		Hashtable<String, String> ret = new Hashtable<String, String>();
		String QUERY = "{\"jsonrpc\":\"2.0\",\"id\":\"updatePlayer\","
				+ "\"method\":\"Player.GetProperties\","
				+ "\"params\":{\"playerid\":0,\"properties\":[\"playlistid\",\"position\",\"time\",\"percentage\"]}}";
		StringBuffer sb = sendRequest(QUERY);
		try {
			JSONObject JSON = new JSONObject(sb.toString());
			JSONObject error = JSON.optJSONObject("error");
			if (error != null) {
				ret.put("error", error.optString("message"));
				return ret;
			}
			JSONObject result1 = JSON.optJSONObject("result");
			if (result1 != null) {
				ret.put("percentage", result1.optDouble("percentage") + "");
				JSONObject time1 = result1.optJSONObject("time");
				ret.put("minutes", time1.optInt("minutes") + "");
				ret.put("seconds", time1.optInt("seconds") + "");
				// get current item
				String QUERY2 = "{\"jsonrpc\":\"2.0\",\"id\":0,\"method\":\"Player.GetItem\",\"params\":{\"playerid\":0,\"properties\":[\"title\",\"artist\",\"thumbnail\",\"duration\",\"albumlabel\"]}}";
				StringBuffer result2 = sendRequest(QUERY2);
				JSONObject JSON2 = new JSONObject(result2.toString());
				JSONObject resultjson = JSON2.optJSONObject("result");
				if (resultjson != null) {
					JSONObject item = resultjson.optJSONObject("item");
					if (item != null) {
						JSONArray artist_array = item.optJSONArray("artist");
						String artist = "";
						for (int i = 0; i < artist_array.length(); i++) {
							artist += artist_array.optString(i);
						}
						ret.put("artist", artist);
						ret.put("title", item.optString("title"));
						ret.put("duration", item.optInt("duration") + "");
						ret.put("thumbnail", item.optString("thumbnail"));
						// Log.e("URL Thumbnail in PiConnector",item.optString("thumbnail"));
					}
				}
				Settings settings = new Settings();
				settings = getCurrentSettings();
				// start Oct
				int id = getCurrentSongId();
				ret.put("currentsongid", id + "");
				// End Oct 8
				if (settings != null) {
					ret.put("repeat", settings.getRepeat() + "");
					ret.put("shuffled", settings.isShuffled() + "");
					ret.put("volume", settings.getVolume() + "");
				}
			}
		} catch (JSONException e) {
			ret.put("error", "exception");
			// e.printStackTrace();
		}
		return ret;
	}

	// Oct 8
	public int getCurrentSongId() {
		int ret = 0;
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"currendID\",\"method\":\"Player.GetItem\",\"params\":{\"playerid\":0,\"properties\":[\"setid\"]}}";
		StringBuffer sb = sendRequest(REQUEST);
		try {
			JSONObject JSON = new JSONObject(sb.toString());
			JSONObject result = JSON.optJSONObject("result");
			if (result != null) {
				JSONObject item = result.optJSONObject("item");
				if (item != null) {
					ret = item.optInt("id");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return 0;
		}
		return ret;
	}

	// end Oct 8
	/**
	 * Get shuffled, repeat, volume...
	 */
	public Settings getCurrentSettings() {
		Settings settings = new Settings();
		String QUERY = "[{\"jsonrpc\":\"2.0\",\"id\":\"getvolume\",\"method\":\"Application.GetProperties\",\"params\":{\"properties\":[\"volume\",\"muted\"]}},{\"jsonrpc\":\"2.0\",\"id\":\"getplayerproperties\",\"method\":\"Player.GetProperties\",\"params\":{\"playerid\":0,\"properties\":[\"repeat\",\"shuffled\"]}}]";
		StringBuffer response = sendRequest(QUERY);
		try {
			JSONArray BATCH_RESPONSE = new JSONArray(response.toString());
			JSONObject JSON_VOLUME = BATCH_RESPONSE.getJSONObject(0);
			JSONObject JSON_STATES = BATCH_RESPONSE.getJSONObject(1);
			// Solving volume
			if (JSON_VOLUME.optString("id").equals("getvolume")) {
				JSONObject tmp = JSON_VOLUME.optJSONObject("result");
				if (tmp != null) {
					boolean isMuted = tmp.optBoolean("muted");
					// Log.i("muted", isMuted + "");
					int volume = tmp.optInt("volume");
					// Log.i("volume",volume + "");
					settings.setMuted(isMuted);
					settings.setVolume(volume);
				}
			}

			// Solving States

			if (JSON_STATES.optString("id").equals("getplayerproperties")) {
				JSONObject tmp = JSON_STATES.optJSONObject("result");
				if (tmp != null) {

					String repeat = tmp.optString("repeat");
					// Log.i("repeat",repeat + "");
					boolean shuffled = tmp.optBoolean("shuffled");
					// Log.i("shuffled",shuffled + "");
					settings.setShuffled(shuffled);
					settings.setRepeate(repeat);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return settings;
	}

	/**
	 * Set volume
	 * 
	 * @param volume
	 */
	public void setVolume(int volume) {
		String SET_VOLUME = "{\"jsonrpc\":\"2.0\",\"id\":\"setvolume\",\"method\": \"Application.SetVolume\","
				+ "\"params\":{\"volume\":" + volume + "}}";
		sendRequest(SET_VOLUME);
	}

	/**
	 * Get artist list
	 */
	private ArrayList<Artist> getArtistsFromPI() {
		String GET_ARTIST_REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"getartists\","
				+ "\"method\":\"AudioLibrary.GetArtists\",\"params\":{\"limits\":{\"start\":0,\"end\":19},"
				+ "\"properties\":[\"description\",\"thumbnail\"]}}";
		artistList = new ArrayList<Artist>();
		try {
			StringBuffer response = sendRequest(GET_ARTIST_REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("artists");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				// t la mot artist
				t = array.getJSONObject(i);
				Artist artist = new Artist();
				artist.setArtistID(t.optInt("artistid"));
				artist.setArtistName(t.optString("artist"));
				artist.setThumbnail(t.optString("thumbnail", ""));
				Log.i("THUMBNAILLLLLLLL", t.optString("thumbnail", ""));
				artist.setDescription(t.optString("description"));
				artistList.add(artist);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return artistList;
	}

	/**
	 * Play next song
	 */
	public void playNextSong() {
		if (allSong == null)
			getAlbumsFromPI();
		String REQUEST = "";
		REQUEST = "{\"jsonrpc\":\"2.0\",\"method\":\"Player.GoTo\",\"params\":{\"playerid\":0,\"to\":\"next\"}}";
		sendRequest(REQUEST);
	}

	/**
	 * Play previous song
	 */
	public void playPreviousSong() {
		String REQUEST = "";
		REQUEST = "{\"jsonrpc\":\"2.0\",\"method\":\"Player.GoTo\",\"params\":{\"playerid\":0,\"to\":\"previous\"}}";
		sendRequest(REQUEST);
	}

	/**
	 * Forward
	 */
	public void playForward() {

	}

	/**
	 * Rewind
	 */
	public void playRewind() {

	}

	// khuongdv start oct 4
	/**
	 * 
	 * @return speed: 0 = pause, 1 = playing
	 */
	public int togglePlayPause() {
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"pp\",\"method\":\"Player.PlayPause\",\"params\":{\"playerid\":0}}";
		StringBuffer sb = sendRequest(REQUEST);
		try {
			JSONObject jo = new JSONObject(sb.toString());
			JSONObject result = jo.optJSONObject("result");
			if (result == null)
				return 0;
			int ret = 0;
			ret = result.optInt("speed");
			return ret;
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}
	}

	// khuongdv end oct 4
	// Oct 8 start
	public ArrayList<Song> getSongsOfArtistByKeyword(Artist currentArtist2,
			String string) {
		ArrayList<Song> list = new ArrayList<Song>();
		string = string.toLowerCase();
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"getsongofartist\",\"method\":\"AudioLibrary.GetSongs\",\"params\":{\"properties\":[\"title\",\"artist\","
				+ "\"genre\",\"track\",\"duration\",\"year\",\"albumid\",\"album\"],\"filter\":{\"artistid\":"
				+ currentArtist2.getArtistID() + "}}}";
		;
		try {
			StringBuffer response = new StringBuffer();
			response = sendRequest(REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("songs");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				t = array.getJSONObject(i);
				Song s = new Song();
				s.setSongid(t.optInt("songid"));
				s.setAlbum(t.optString("album"));
				s.setDuration(t.optInt("duration"));
				s.setAlbumid(t.optInt("albumid"));
				s.setName(t.optString("label"));
				s.setThumbnail(t.optString("thumbnail"));
				JSONArray artistJSON = t.optJSONArray("artist");
				String[] artist = new String[artistJSON.length()];
				for (int j = 0; j < artistJSON.length(); j++) {
					artist[j] = artistJSON.optString(j);
				}
				s.setArtist(artist);
				if (s.getName().toLowerCase().contains(string))
					list.add(s);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public ArrayList<Song> getSongsOfAlbumByKeyword(Album currentAlbum2,
			String string) {
		addSongOfAlbumToPlaylist(currentAlbum2.getAlbumid());
		ArrayList<Song> list = new ArrayList<Song>();
		string = string.toLowerCase();
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"getsongofartist\",\"method\":\"AudioLibrary.GetSongs\",\"params\":{\"properties\":[\"title\",\"artist\","
				+ "\"genre\",\"track\",\"duration\",\"year\",\"albumid\",\"album\"],\"filter\":{\"albumid\":"
				+ currentAlbum2.getAlbumid() + "}}}";
		;
		try {
			StringBuffer response = new StringBuffer();
			response = sendRequest(REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("songs");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				t = array.getJSONObject(i);
				Song s = new Song();
				s.setSongid(t.optInt("songid"));
				s.setAlbum(t.optString("album"));
				s.setDuration(t.optInt("duration"));
				s.setAlbumid(t.optInt("albumid"));
				s.setName(t.optString("label"));
				s.setThumbnail(t.optString("thumbnail"));
				JSONArray artistJSON = t.optJSONArray("artist");
				String[] artist = new String[artistJSON.length()];
				for (int j = 0; j < artistJSON.length(); j++) {
					artist[j] = artistJSON.optString(j);
				}
				s.setArtist(artist);
				if (s.getName().toLowerCase().contains(string))
					list.add(s);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public void toggleShuffled() {
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"toggleShuffled\",\"method\":\"Player.SetShuffle\",\"params\":{\"playerid\":0,\"shuffle\":\"toggle\"}}";
		sendRequest(REQUEST);
	}

	public void cycleRepeat() {
		String REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"toggleRepeat\",\"method\":\"Player.SetRepeat\",\"params\":{\"playerid\":0,\"repeat\":\"cycle\"}}";
		sendRequest(REQUEST);
	}

	// Oct 8 end
	// Oct 11
	public void addSongOfArtistToPlaylist(int artistID) {
		String CLEARPLAYLIST = "{\"jsonrpc\":\"2.0\",\"id\":\"clear_playlist\","
				+ "\"method\":\"Playlist.Clear\","
				+ "\"params\":{\"playlistid\":0}}";
		sendRequest(CLEARPLAYLIST);
		String ADD_ARTIST_TO_PLAYLIST = "{\"jsonrpc\":\"2.0\", \"id\":\"addalbumtoplaylist\","
				+ "\"method\": \"Playlist.Add\","
				+ "\"params\":{\"playlistid\":0,\"item\":{\"artistid\":"
				+ artistID + "}}}";
		sendRequest(ADD_ARTIST_TO_PLAYLIST);
	}

	public void addSongOfAlbumToPlaylist(int albumID) {
		String CLEARPLAYLIST = "{\"jsonrpc\":\"2.0\",\"id\":\"clear_playlist\","
				+ "\"method\":\"Playlist.Clear\","
				+ "\"params\":{\"playlistid\":0}}";
		sendRequest(CLEARPLAYLIST);
		String ADD_ARTIST_TO_PLAYLIST = "{\"jsonrpc\":\"2.0\", \"id\":\"addalbumtoplaylist\","
				+ "\"method\": \"Playlist.Add\","
				+ "\"params\":{\"playlistid\":0,\"item\":{\"albumid\":"
				+ albumID + "}}}";
		sendRequest(ADD_ARTIST_TO_PLAYLIST);
	}

	public void playSongInListSongOfArtistOrAlbum(int position) {

		String REQUEST = "{\"jsonrpc\":\"2.0\", \"id\":\"playartist\","
				+ "\"method\": \"Player.Open\","
				+ "\"params\":{\"item\":{\"playlistid\":0,\"position\":"
				+ position + "}}}";
		sendRequest(REQUEST);
	}

	// End oct11

//	public ArrayList<Song> getSongOfPage(int page) {
//		if (page == 1) {
//			return getAllSongsFromPI();
//		}
//		if (allSong == null || allSong.size() <= 0) {
//			getAllSongsFromPI();
//		}
//		page = page - 1;
//		ArrayList<Song> ret = new ArrayList<Song>();
//		String REQUEST = "{\"jsonrpc\":\"2.0\",\"method\":\"Playlist.GetItems\",\"params\":"
//				+ "{\"playlistid\":0,\"limits\":"
//				+ "{\"start\":"
//				+ (20 * page)
//				+ ", \"end\":"
//				+ (20 * page + 20 - 1)
//				+ "},\"properties\":"
//				+ "[\"artist\",\"duration\",\"albumid\",\"album\",\"track\",\"thumbnail\"]},"
//				+ "\"id\":\"getsongs\"}";
//		try {
//			StringBuffer response = new StringBuffer();
//			response = sendRequest(REQUEST);
//			// Log.e("RES",response.toString());
//			JSONObject jO = new JSONObject(response.toString());
//			JSONObject result = jO.optJSONObject("result");
//			JSONArray array = result.optJSONArray("items");
//			JSONObject t = new JSONObject();
//
//			for (int i = 0; i < array.length(); i++) {
//				t = array.getJSONObject(i);
//				Song s = new Song();
//				s.setSongid(t.optInt("songid"));
//				s.setAlbum(t.optString("album"));
//				s.setDuration(t.optInt("duration"));
//				s.setAlbumid(t.optInt("albumid"));
//				s.setName(t.optString("label"));
//				s.setTrack(t.optInt("track", 0));
//				s.setThumbnail(t.optString("thumbnail"));
//				JSONArray artistJSON = t.optJSONArray("artist");
//				String[] artist = new String[artistJSON.length()];
//				for (int j = 0; j < artistJSON.length(); j++) {
//					artist[j] = artistJSON.optString(j);
//				}
//				s.setArtist(artist);
//				ret.add(s);
//			}
//
//		} catch (Exception ex) {
//			Log.e("Error khi load page", "@@");
//			ex.printStackTrace();
//		}
//
//		return ret;
//	}

	

	public ArrayList<Artist> getSpecificArtists(int start, int end) {

		String GET_ARTIST_REQUEST = "{\"jsonrpc\":\"2.0\",\"id\":\"getartists\","
				+ "\"method\":\"AudioLibrary.GetArtists\",\"params\":{\"limits\":{\"start\":"
				+ start
				+ ",\"end\":"
				+ end
				+ "},"
				+ "\"properties\":[\"description\",\"thumbnail\"]}}";
		ArrayList<Artist> artistList1 = new ArrayList<Artist>();
		try {
			StringBuffer response = sendRequest(GET_ARTIST_REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("artists");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				// t la mot artist
				t = array.getJSONObject(i);
				Artist artist = new Artist();
				artist.setArtistID(t.optInt("artistid"));
				artist.setArtistName(t.optString("artist"));
				artist.setThumbnail(t.optString("thumbnail", ""));
				artist.setDescription(t.optString("description"));
				artistList1.add(artist);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return artistList1;
	}

	public ArrayList<Album> getSpecificAlbums(int start, int end) {
		ArrayList<Album> _albumList = new ArrayList<Album>();
		try {

			StringBuffer response = new StringBuffer();

			String REQUEST = "{\"jsonrpc\": \"2.0\", \"method\": \"AudioLibrary.GetAlbums\","
					+ " \"params\": { \"limits\": { \"start\" : "+start +",\"end\":"+end+" },"
					+ "\"properties\": [ \"description\",\"artist\",\"genre\",\"rating\",\"title\",\"year\",\"thumbnail\"], "
					+ "\"sort\": { \"order\": \"ascending\", \"method\": \"artist\"} }, \"id\": \"getalbums\"}";

			response = sendRequest(REQUEST);
			JSONObject jO = new JSONObject(response.toString());
			JSONObject result = jO.optJSONObject("result");
			JSONArray array = result.optJSONArray("albums");
			JSONObject t = new JSONObject();

			for (int i = 0; i < array.length(); i++) {
				t = array.getJSONObject(i);
				JSONArray artistJSON = t.optJSONArray("artist");
				String[] artist = new String[artistJSON.length()];
				for (int j = 0; j < artistJSON.length(); j++) {
					artist[j] = artistJSON.optString(j);
				}
				t = array.getJSONObject(i);
				Album temp = new Album();
				temp.setAlbumid(t.optInt("albumid"));
				temp.setTitle(t.optString("title"));
				temp.setAlbumtitle(t.optString("title"));
				temp.setDescription(t.optString("description"));
				temp.setYear(t.optInt("year"));
				temp.setThumbnail(t.optString("thumbnail"));
				temp.setArtistArray(artist);
				_albumList.add(temp);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return _albumList;
	}

	public void sendInputMethod(String method) {
		// TODO Auto-generated method stub
		String QUERY = "{\"jsonrpc\":\"2.0\",\"method\":\"Input."+method+"\"}";
		sendRequest(QUERY);
	}
}
