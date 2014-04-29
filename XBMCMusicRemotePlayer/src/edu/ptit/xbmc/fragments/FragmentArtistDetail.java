package edu.ptit.xbmc.fragments;

import java.util.ArrayList;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.adapter.AllSongAdapter2;
import edu.ptit.xbmc.model.Artist;
import edu.ptit.xbmc.model.Song;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class FragmentArtistDetail extends Fragment implements OnItemClickListener, OnClickListener{
	ListView lv;
	ImageButton btnSearch;
	Button btnLoadmore;
	EditText txtKeyword;View fview;
	ArrayList<Song> list = new ArrayList<Song>();
	Artist currentArtist;int artistID = 0;
	public FragmentArtistDetail setArtistID(int id, String title){
		this.artistID = id;
//		getActivity().setTitle(title);
		return this;
	}
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_artist_details, container,false); 
		lv = (ListView) view.findViewById(edu.ptit.xbmc.R.id.allSongOfArtistDetail);
		lv.setOnItemClickListener(this);setRetainInstance(true);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInArtistDetail);
		btnSearch.setOnClickListener(this);
		txtKeyword = (EditText) view.findViewById(R.id.txtSearchInArtistDetail);
		fview = (View) ((LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer_for_listview_artistdetail, null, false);
		btnLoadmore = (Button) fview.findViewById(R.id.button_load_more_artistdetail);
		btnLoadmore.setOnClickListener(this);
		new UpdateView().execute("");
		return view;
	}
	/*
	 * New thread to update listview
	 */
	private class UpdateView extends AsyncTask<String, Void, AllSongAdapter2> {
		
		public UpdateView() {
		
			currentArtist = new Artist();
			currentArtist.setArtistID(artistID);
		}

		@Override
		protected AllSongAdapter2 doInBackground(String... params) {
			
			PiConnector.getInstance().addSongOfArtistToPlaylist(artistID);
			list = PiConnector.getInstance().getSongsOfArtistByKeyword(currentArtist,params[0]);// getAllSongsFromPI();
			AllSongAdapter2 ad = new AllSongAdapter2(getActivity(), list);
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
	
	private class PlaySong extends AsyncTask<Integer, Void, Void> {
		
		int position;
		Song s;
		public PlaySong(int position, Song s) {
			
			this.s = s;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Integer... params) {
			if (s != null) {
				try {
					PiConnector.getInstance().playSongInListSongOfArtistOrAlbum(position);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Log.i("nowplaying", "Moving...");
			}
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(Void result) {
			Utils.getInstance().switchTab(Constants.TAB_NOWPLAYING);
		}

	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		// TODO Auto-generated method stub
		new PlaySong(position, (Song)list.get(position)).execute();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
