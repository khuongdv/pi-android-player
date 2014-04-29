//package edu.ptit.xbmc.fragments;
//
//import edu.ptit.xbmc.R;
//import edu.ptit.xbmc.sp.Constants;
//import edu.ptit.xbmc.tools.Utils;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class FragmentArtist extends Fragment {
//	@Override
//	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
//		View view = inf.inflate(R.layout.fragment_artist, container,false);
//		FragmentArtistList fal = new  FragmentArtistList();
//		Utils.getInstance().FRAGMENT_ARTIST_LIST = fal;
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.add(R.id.fragment_container,fal,Constants.TAG_ARTIST_LIST);
//		ft.attach(fal);
//		ft.commit();
////		Utils.getInstance().switchFragmentInArtistTab(this, new FragmentArtistList());
//		setRetainInstance(true);
//		return view;	
//	}
//}
