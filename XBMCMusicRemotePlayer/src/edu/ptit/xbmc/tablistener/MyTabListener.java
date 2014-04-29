//package edu.ptit.xbmc.tablistener;
//
//import android.app.ActionBar.Tab;
//import android.app.ActionBar.TabListener;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
//import edu.ptit.xbmc.R;
//import edu.ptit.xbmc.activities.MainActivity;
//
//public class MyTabListener implements TabListener {
//	public Fragment fragment;
//	public MainActivity context;
//	
//	public MyTabListener(Fragment fr, MainActivity con){
//		fragment = fr;
//		context = con;
//	}
//	@Override
//	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onTabSelected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
////		ft.replace(R.id.fragment_container, fragment);
//		Fragment _fragment = context.getFragmentManager().findFragmentByTag(tab.getTag().toString());
//		if(_fragment == null){
//			ft.replace(R.id.fragment_container, fragment);
//		}
//		else{
//			ft.show(fragment);
//		}
//	}
//
//	@Override
//	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
//		// TODO Auto-generated method stub
//		if(fragment != null) ft.hide(fragment);
//	}
//
//}
