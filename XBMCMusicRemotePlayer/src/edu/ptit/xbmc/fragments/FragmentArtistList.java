package edu.ptit.xbmc.fragments;

import java.util.ArrayList;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.activities.MainActivity;
import edu.ptit.xbmc.adapter.AllArtistAdapter;
import edu.ptit.xbmc.model.Artist;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentArtistList  extends Fragment implements OnItemClickListener,
OnClickListener{
	ListView lv;
	EditText txtSearch;
	Button btnLoadmore;
	ImageButton btnSearch;
	View footerview;
	AllArtistAdapter adapter;
	ArrayList<Artist> list = new ArrayList<Artist>();
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_artist_list, container,false);
		lv = (ListView) view.findViewById(R.id.allArtistListView);
		lv.setOnItemClickListener(this);setRetainInstance(true);
		// Add 3-11
		txtSearch = (EditText) view.findViewById(R.id.txtSearchInAllArtist);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInAllArtist);
		btnSearch.setOnClickListener(this);
				// End 3-11
		// Oct 29
		footerview = (View) ((LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer_for_listview1, null, false);
		Button btnLoadmore = (Button) footerview
				.findViewById(R.id.button_load_more1);
		btnLoadmore.setOnClickListener(this);
		lv.addFooterView(footerview);
		// End Oct 29

		new UpdateView().execute("");
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.equals(btnLoadmore)) {
			final int currentsize = lv.getAdapter().getCount();
			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					ArrayList<Artist> _list = PiConnector.getInstance()
							.getSpecificArtists(currentsize, currentsize + 19);
					if (_list != null && _list.size() > 0) {
						list.addAll(_list);

						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								adapter.notifyDataSetChanged();
							}
						});
					}
					else{
						
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								lv.removeFooterView(footerview);
							}
						});
					}
				}
			});
			thread.start();
			}
			if(v.equals(btnSearch)){
				String keyword  = txtSearch.getText().toString();
				if(keyword == null || keyword.equals("")){ 
					adapter = new AllArtistAdapter(getActivity(), list);
					lv.setAdapter(adapter);
					return;
				}
				ArrayList<Artist> tmpList = new ArrayList<Artist>();
				keyword = keyword.toLowerCase();
				for(int i = 0; i < list.size(); i++){
					Artist tmpSong = list.get(i);
					if(tmpSong.getArtistName().toLowerCase().contains(keyword))
					tmpList.add(tmpSong);
				}
				adapter = new AllArtistAdapter(getActivity(), tmpList);
				lv.setAdapter(adapter);
			}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		new A(position).execute();	
		FragmentArtistDetail fad =  new FragmentArtistDetail().setArtistID(list.get(position).getArtistID(), 
				list.get(position).getArtistName());
		Utils.getInstance().FRAGMENT_ARTIST_DETAIL = fad;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(fad.isAdded() == false){			
			ft.add(R.id.fragment_container_artist_detail, fad, Constants.TAG_ARTIST_DETAIL);
		}
//		if(Utils.getInstance().FRAGMENT_ARTIST_LIST.isDetached() == false)
//			ft.detach(Utils.getInstance().FRAGMENT_ARTIST_LIST);
		((MainActivity)getActivity()).hideAll();
		ft.attach(fad);
//		ft.show(fad);
		ft.commit();
		MainActivity.ARTIST_CURRENT_SCREEN = Utils.SECOND_SCREEN;
//		Utils.getInstance().switchFragmentInArtistTab(this, fad);
	}
	class A extends AsyncTask<Void, Void, Void>{
		private int position;
		public A(int pos){ position = pos;}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			PiConnector.getInstance().addSongOfArtistToPlaylist(
					list.get(position).getArtistID());
			return null;
		}
		
	}
	private class UpdateView extends AsyncTask<String, Void, AllArtistAdapter> {
		public UpdateView() {			
		}

		@Override
		protected void onProgressUpdate(Void... params) {
		}

		@Override
		protected AllArtistAdapter doInBackground(String... params) {
			list = PiConnector.getInstance().getArtistsByKeyword(params[0]);// getAllSongsFromPI();
			adapter = new AllArtistAdapter(getActivity(), list);
			return adapter;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(AllArtistAdapter result) {
			// dialog.dismiss();
			if (result != null)
				lv.setAdapter(result);
			 if (lv.getAdapter().getCount() < 20)
			 lv.removeFooterView(footerview);
		}

	}
}
