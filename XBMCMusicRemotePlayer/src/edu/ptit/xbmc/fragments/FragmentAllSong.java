package edu.ptit.xbmc.fragments;

import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.Toast;
import edu.ptit.xbmc.R;
import edu.ptit.xbmc.adapter.AllSongAdapter2;
import edu.ptit.xbmc.model.Song;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;

public class FragmentAllSong extends Fragment implements OnClickListener,
		OnItemClickListener {
	ImageButton btnSearch;
	View view;
	// From old project
	ListView lv;
	AlertDialog ald;
	EditText txtSearch;
	AllSongAdapter2 adapter;
	Button btnLoadmore;
	ArrayList<Song> list = new ArrayList<Song>();
	View fview;

	// end declaration
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container,
			Bundle savedInstanceState) {
		view = inf.inflate(R.layout.fragment_song_list, container, false);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInAllSong);
		btnSearch.setOnClickListener(this);
		// From old project
		setRetainInstance(true);
		Log.e("CREATE NEW OBJECT", "Uhm");
		lv = (ListView) view.findViewById(R.id.allSong);
		lv.setOnItemClickListener(this);
		fview = (View) ((LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer_for_listview, null, false);
		if (fview == null)
			Log.e("FOOTER NULL", "---");
		btnLoadmore = (Button) fview.findViewById(R.id.button_load_more);
		btnLoadmore.setOnClickListener(this);
		lv.addFooterView(fview);
		txtSearch = (EditText) view.findViewById(R.id.txtSearchInAllSong);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInAllSong);
		btnSearch.setOnClickListener(this);
		new UpdateView().execute("");
		// loadmore();
		// end old project
		return view;
	}

	// from old project

	private class UpdateView extends AsyncTask<String, Void, AllSongAdapter2> {
		private Activity act;

		public UpdateView() {
			act = getActivity();
		}

		@Override
		protected AllSongAdapter2 doInBackground(String... params) {
			// list = PiConnector.getInstance().getSongsByKeyword(params[0]);//
			// getAllSongsFromPI();
			ArrayList<Song> _list = PiConnector.getInstance().getSpecificSongs(
					0, 19);
			if (_list != null && _list.size() > 0)
				list.addAll(_list);
			adapter = new AllSongAdapter2(act, list);
			return adapter;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... params) {
		}

		protected void onPostExecute(AllSongAdapter2 result) {
			if (result != null) {
				lv.setAdapter(result);
				// if (lv.getAdapter().getCount() < 20)
				// lv.removeFooterView(fview);
			} else {

			}
		}
	}

	/**
	 * Play a song that you 've just clicked on it
	 * 
	 * @author Khuong
	 * 
	 */
	private class PlaySong extends AsyncTask<Integer, Void, Void> {
		Activity act;
		int position;

		public PlaySong(Activity a, int position) {
			act = a;
			this.position = position;
		}

		@Override
		protected Void doInBackground(Integer... params) {
			try {
				PiConnector.getInstance().playsong(position);
				Toast.makeText(act, "" + position, Toast.LENGTH_SHORT).show();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(Void result) {
			switchToTab(Constants.TAB_NOWPLAYING);
		}

	}

	void loadmore() {
		final int currentsize = lv.getAdapter().getCount();
		
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Song> _list = PiConnector.getInstance()
						.getSpecificSongs(currentsize, currentsize + 19);
				if (_list != null && _list.size() > 0) {
					list.addAll(_list);

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});
				} else {
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							lv.removeFooterView(fview);
						}
					});
				}
			}
		});
		thread.start();
	}

	// end old project
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(btnLoadmore)) {
			loadmore();
		}
		if (v.equals(btnSearch)) {
			String keyword = txtSearch.getText().toString();
			if (keyword == null || keyword.equals("")) {
				adapter = new AllSongAdapter2(getActivity(), list);
				lv.setAdapter(adapter);
				return;
			}
			ArrayList<Song> tmpList = new ArrayList<Song>();
			keyword = keyword.toLowerCase();
			for (int i = 0; i < list.size(); i++) {
				Song tmpSong = list.get(i);
				if (tmpSong.getName().toLowerCase().contains(keyword))
					tmpList.add(tmpSong);
			}
			adapter = new AllSongAdapter2(getActivity(), tmpList);
			lv.setAdapter(adapter);

		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		new PlaySong(getActivity(), position).execute();
	}

	public void switchToTab(int index) {
		getActivity().getActionBar().setSelectedNavigationItem(index);
	}

}
