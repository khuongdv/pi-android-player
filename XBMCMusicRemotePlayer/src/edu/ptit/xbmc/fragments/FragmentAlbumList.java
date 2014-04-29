package edu.ptit.xbmc.fragments;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import edu.ptit.xbmc.R;
import edu.ptit.xbmc.activities.MainActivity;
import edu.ptit.xbmc.adapter.AllAlbumAdapter;
import edu.ptit.xbmc.model.Album;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;

public class FragmentAlbumList extends Fragment implements OnItemClickListener,
OnClickListener {
	ListView lv;
	EditText txtSearch;
	Button btnLoadmore;
	AllAlbumAdapter adapter;
	View footerview;
	ArrayList<Album> list = new ArrayList<Album>();
	ImageButton btnSearch;
	@Override
	public void onActivityCreated(Bundle b){
		super.onActivityCreated(b);
		lv.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//		lv.setOnItemClickListener(this);
	}
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_album_list, container,false);
		lv = (ListView) view.findViewById(R.id.allAlbumListView);
		lv.setTag(Constants.TAG_ALBUM);setRetainInstance(true);
		txtSearch = (EditText) view.findViewById(R.id.txtSearchInAllAlbum);
		btnSearch = (ImageButton) view.findViewById(R.id.btnSearchInAllAlbum);
		btnSearch.setOnClickListener(this);
		lv.setOnItemClickListener(FragmentAlbumList.this);
		footerview = (View) ((LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.footer_for_listview2, null, false);
		btnLoadmore = (Button) footerview.findViewById(R.id.button_load_more2);
		btnLoadmore.setOnClickListener(this);
		lv.addFooterView(footerview);
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
					ArrayList<Album> _list = PiConnector.getInstance()
							.getSpecificAlbums(currentsize, currentsize + 19);
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
				adapter = new AllAlbumAdapter(getActivity(), list);
				lv.setAdapter(adapter);
				return;
			}
			ArrayList<Album> tmpList = new ArrayList<Album>();
			keyword = keyword.toLowerCase();
			for(int i = 0; i < list.size(); i++){
				Album tmpSong = list.get(i);
				if(tmpSong.getTitle().toLowerCase().contains(keyword))
				tmpList.add(tmpSong);
			}
			adapter = new AllAlbumAdapter(getActivity(), tmpList);
			lv.setAdapter(adapter);
		}
	
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
		new A(position).execute();
		// Show fragment albumdetail
		FragmentAlbumDetail fad =  new FragmentAlbumDetail().setAlbumID(list.get(position).getAlbumid(), 
				list.get(position).getAlbumtitle());
		Utils.getInstance().FRAGMENT_ALBUM_DETAIL = fad;
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(fad.isAdded() == false){			
			ft.add(R.id.fragment_container_albumdetail, fad, Constants.TAG_ALBUM_DETAIL);
		}
//		if(Utils.getInstance().FRAGMENT_ALBUM_LIST.isDetached() == false)
//			ft.detach(Utils.getInstance().FRAGMENT_ALBUM_LIST);
		((MainActivity)getActivity()).hideAll();
		ft.attach(fad);
//		ft.show(fad);
		ft.commit();
		MainActivity.ALBUM_CURRENT_SCREEN = Utils.SECOND_SCREEN;
//		Utils.getInstance().switchFragmentInAlbumTab(this, fad);
	}
	class A extends AsyncTask<Void, Void, Void> {
		
		private int position;

		public A( int pos) {			
			position = pos;
		}

		@Override
		protected Void doInBackground(Void... params) {
			PiConnector.getInstance().addSongOfAlbumToPlaylist(
					list.get(position).getAlbumid());
			
			return null;
		}

	}
	private class UpdateView extends AsyncTask<String, Void, AllAlbumAdapter> {
		 

		public UpdateView( ) {
			 
		}

		@Override
		protected void onProgressUpdate(Void... params) {
			// Oct 11
		}

		@Override
		protected AllAlbumAdapter doInBackground(String... params) {
			list = PiConnector.getInstance().getAlbumsByKeyword(params[0]);
			adapter = new AllAlbumAdapter(getActivity(), list);
			return adapter;
		}

		@Override
		protected void onPreExecute() {

		}

		protected void onPostExecute(AllAlbumAdapter result) {
			// dialog.dismiss();
			if (result != null)
				lv.setAdapter(result);
			if (lv.getAdapter().getCount() < 20)
				lv.removeFooterView(footerview);
		}
	}
	
}
