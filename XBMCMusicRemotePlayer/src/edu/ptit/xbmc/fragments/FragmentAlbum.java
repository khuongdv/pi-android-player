//package edu.ptit.xbmc.fragments;
//
//import android.annotation.SuppressLint;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import edu.ptit.xbmc.R;
//import edu.ptit.xbmc.sp.Constants;
//import edu.ptit.xbmc.tools.Utils;
//
//public class FragmentAlbum extends Fragment {
//	@SuppressLint("NewApi")
//	@Override
//	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
//		View view = inf.inflate(R.layout.fragment_album, container,false);
//		FragmentAlbumList fal = new  FragmentAlbumList();
//		Utils.getInstance().FRAGMENT_ALBUM_LIST = fal;
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.add(R.id.fragment_container,fal,Constants.TAG_ALBUM_LIST);
//		ft.attach(fal);
//		ft.commit();
////		Utils.getInstance().switchFragmentInAlbumTab(this, Utils.getInstance().FRAGMENT_ALBUM_LIST);
//		setRetainInstance(true);
//		return view;	
//	}
//}
