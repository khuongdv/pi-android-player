package edu.ptit.xbmc.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import edu.ptit.xbmc.R;
import edu.ptit.xbmc.adapter.AllSongAdapter2;
import edu.ptit.xbmc.model.Album;
import edu.ptit.xbmc.model.Song;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;

public class FragmentAlbumDetail  extends Fragment implements OnItemClickListener{
	ListView lv;
//	EditText txtSearch;
//	ImageButton btnSearch;
	ArrayList<Song> list = new ArrayList<Song>();
	private int albumID;
//	private String title;
	public FragmentAlbumDetail setAlbumID(int id, String title){
		this.albumID = id;
//		this.title = title;
//		if(title != null) getActivity().setTitle(title);
		return this;
	}
	
	public FragmentAlbumDetail(){}
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_album_details, container,false);
		lv = (ListView) view.findViewById(R.id.allSongOfAlbumDetail);
//		txtSearch = (EditText) view.findViewById(R.id.txtSearchInAlbumDetail);
//		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInAlbumDetail);
//		btnSearch.setOnClickListener(this);
		lv.setOnItemClickListener(this);setRetainInstance(true);
		new UpdateView(getActivity()).execute("");
		return view;
	}
	// From old project
	private class UpdateView extends AsyncTask<String, Void, AllSongAdapter2> {
		Activity act;Album currentAlbum = new Album();
//		ProgressDialog dialog;

		public UpdateView(Activity a) {
			act = a;			
			currentAlbum.setAlbumid(albumID);
		}

		@Override
		protected AllSongAdapter2 doInBackground(String... params) {
			
//			PiConnector.getInstance(act).addSongOfAlbumToPlaylist(albumID);
			list = PiConnector.getInstance().getSongsOfAlbumByKeyword(currentAlbum,params[0]);// getAllSongsFromPI();
			AllSongAdapter2 ad = new AllSongAdapter2(act, list);
			return ad;
		}

		@Override
		protected void onPreExecute() {}

		@Override
		protected void onProgressUpdate(Void... params) {}

		protected void onPostExecute(AllSongAdapter2 result) {
//			dialog.dismiss();
			if (result != null)
				lv.setAdapter(result);
		}
	}
	/**
	 * Play a song that you 've clicked on it
	 * 
	 * @author Khuong
	 * 
	 */
	private class PlaySong extends AsyncTask<Integer, Void, Void> {
		int position;		
		public PlaySong(int position) {
			this.position = position;
		}

		@Override
		protected Void doInBackground(Integer... params) {
//			if (s != null) {
				try {
//					Toast.makeText(act, list.get(position).getName(), Toast.LENGTH_LONG).show();
//					PiConnector.getInstance(act).addSongOfAlbumToPlaylist(currentAlbum.getAlbumid());
					PiConnector.getInstance().playSongInListSongOfArtistOrAlbum(position);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
//				Log.i("nowplaying", "Moving...");
//			}
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(Void result) {			
			Utils.getInstance().switchTab(Constants.TAB_NOWPLAYING);
		}

	}
	// End
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		new PlaySong(position).execute();
	}

	
}
