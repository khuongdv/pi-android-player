//package edu.ptit.xbmc.tablistener;
//
//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;
//import android.app.FragmentTransaction;
//import android.util.Log;
//import edu.ptit.xbmc.R;
//import edu.ptit.xbmc.fragments.FragmentAlbum;
//import edu.ptit.xbmc.sp.Constants;
//import edu.ptit.xbmc.tools.Utils;
//
//public class TabListenerForAlbum implements TabListener {	
//	FragmentAlbum fa;
//	public TabListenerForAlbum(FragmentAlbum f){fa = f;		
//	}
//	Utils util = Utils.getInstance();
//	@Override
//	public void onTabReselected(Tab tab, FragmentTransaction ft) {
//
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		if(Utils.getInstance().FRAGMENT_ALBUM == null) {
//			Log.e("FRAGMNT ALBUM","NULL");
//		} else Log.e("FRAGMNT ALBUM","KHONG NULL");
//		if(fa.getFragmentManager().findFragmentByTag(Constants.TAG_ALBUM) == null){
//			ft.add(R.id.fragment_container, Utils.getInstance().FRAGMENT_ALBUM, Constants.TAG_ALBUM);
//		} else
//		ft.show(Utils.getInstance().FRAGMENT_ALBUM);
//		
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//		ft.hide(Utils.getInstance().FRAGMENT_ALBUM);
//	}
//
//}
