package edu.ptit.xbmc.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import edu.ptit.xbmc.R;
import edu.ptit.xbmc.fragments.FragmentAlbumList;
import edu.ptit.xbmc.fragments.FragmentAllSong;
import edu.ptit.xbmc.fragments.FragmentArtistList;
import edu.ptit.xbmc.fragments.FragmentDirectRemote;
import edu.ptit.xbmc.fragments.FragmentNowPlaying;
import edu.ptit.xbmc.fragments.FragmentSettings;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.sp.SPUtils;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;

public class MainActivity extends FragmentActivity implements TabListener {
	ActionBar.Tab taballsong,taballAlbum,taballArtist,tabnowPlaying,tabsettings,tabdirectRemote;
	public static int ALBUM_CURRENT_SCREEN = Utils.FIRST_SCREEN, ARTIST_CURRENT_SCREEN = Utils.FIRST_SCREEN;;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Utils.getInstance().setActivity(this);
		actionbar.setStackedBackgroundDrawable(new ColorDrawable(0xffffff));
				
		taballsong = actionbar.newTab().setIcon(R.drawable.allsong);
		taballAlbum = actionbar.newTab().setIcon(R.drawable.album);
		taballArtist = actionbar.newTab().setIcon(R.drawable.artist);
		tabnowPlaying = actionbar.newTab().setIcon(R.drawable.nowplaying);
		tabsettings = actionbar.newTab().setIcon(R.drawable.setting);
		tabdirectRemote = actionbar.newTab().setIcon(R.drawable.icone_telecommande);
		FragmentAllSong frallsong = new FragmentAllSong();
//		FragmentAlbum fralbum = new FragmentAlbum();
//		FragmentArtist frartist = new FragmentArtist();		
		FragmentAlbumList fralbumlist = new FragmentAlbumList();
		FragmentArtistList fraristlist = new  FragmentArtistList();
		FragmentNowPlaying frnowPlaying = new FragmentNowPlaying();
		FragmentSettings frsettings = new FragmentSettings();
		FragmentDirectRemote frdirectRemote = new FragmentDirectRemote();
		
		Utils instance = Utils.getInstance();
		instance.FRAGMENT_ALBUM_LIST = fralbumlist;
		instance.FRAGMENT_ALL_SONG = frallsong;
		instance.FRAGMENT_ARTIST_LIST = fraristlist;
		instance.FRAGMENT_NOW_PLAYING = frnowPlaying;
		instance.FRAGMENT_SETTINGS = frsettings;
		instance.FRAGMENT_DIRECT_REMOTE = frdirectRemote;
		//Add them to layout
		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.add(R.id.fragment_container_allsong,instance.FRAGMENT_ALL_SONG, Constants.TAG_ALLSONG);
//		ft.add(R.id.fragment_container_album_list,instance.FRAGMENT_ALBUM_LIST, Constants.TAG_ALBUM);
//		ft.add(R.id.fragment_container_artist_list,instance.FRAGMENT_ARTIST_LIST, Constants.TAG_ARTIST);
//		ft.add(R.id.fragment_container_nowplaying,instance.FRAGMENT_NOW_PLAYING, Constants.TAG_NOW_PLAYING);
//		ft.add(R.id.fragment_container_direct_remote,instance.FRAGMENT_DIRECT_REMOTE, Constants.TAG_DIRECT_REMOTE);
//		ft.add(R.id.fragment_container_settings,instance.FRAGMENT_SETTINGS, Constants.TAG_SETTINGS);
		ft.hide(instance.FRAGMENT_ALBUM_LIST);
		ft.hide(instance.FRAGMENT_ALL_SONG);
		ft.hide(instance.FRAGMENT_ARTIST_LIST);
		ft.hide(instance.FRAGMENT_NOW_PLAYING);
		ft.hide(instance.FRAGMENT_DIRECT_REMOTE);
		ft.hide(instance.FRAGMENT_SETTINGS);
		ft.commit();
		//end
		taballsong.setTabListener(this);
		taballAlbum.setTabListener(this);
		taballArtist.setTabListener(this);		
		tabnowPlaying.setTabListener(this);
		tabsettings.setTabListener(this);
		tabdirectRemote.setTabListener(this);
		
		actionbar.addTab(taballsong.setText("").setTag(Constants.TAG_ALLSONG));
		actionbar.addTab(taballAlbum.setText("").setTag(Constants.TAG_ALBUM));
		actionbar.addTab(taballArtist.setText("").setTag(Constants.TAG_ARTIST));		
		actionbar.addTab(tabnowPlaying.setText("").setTag(Constants.TAG_NOW_PLAYING));
		actionbar.addTab(tabsettings.setText("").setTag(Constants.TAG_SETTINGS));
		actionbar.addTab(tabdirectRemote.setText("").setTag(Constants.TAG_DIRECT_REMOTE));
		
		new CheckSettingInSP(this).execute();
	}
	public void hideAll(){
		Utils instance = Utils.getInstance();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.hide(instance.FRAGMENT_ALBUM_LIST);
		ft.hide(instance.FRAGMENT_ALL_SONG);
		ft.hide(instance.FRAGMENT_ARTIST_LIST);
		ft.hide(instance.FRAGMENT_NOW_PLAYING);
		ft.hide(instance.FRAGMENT_DIRECT_REMOTE);
		ft.hide(instance.FRAGMENT_SETTINGS);
		if(instance.FRAGMENT_ALBUM_DETAIL != null) ft.hide(instance.FRAGMENT_ALBUM_DETAIL);
		if(instance.FRAGMENT_ARTIST_DETAIL != null) ft.hide(instance.FRAGMENT_ARTIST_DETAIL);
		ft.commit();
	}
	// From old project
	
	class CheckSettingInSP extends AsyncTask<Void, Void, Boolean> {
		private Activity act;

		public CheckSettingInSP(Activity a) {
			act = a;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			boolean b = SPUtils.checkSettings(act);
			return b;
		}

		@Override
		public void onPostExecute(Boolean check) {
			if (check == false) {
				// Move to Setting tabs
				Utils.getInstance().switchTab(Constants.TAB_SETTINGS);
			}			
		}}
	// End
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction _ft) {
		return;
	}
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction _ft) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(tab.equals(taballsong)){
			ft.show(Utils.getInstance().FRAGMENT_ALL_SONG);
			AsyncTask<Void, Void, Void> asc = new AsyncTask<Void, Void, Void>(){

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					if(Utils.getInstance().ALLSONG_LIST != null)
					PiConnector.getInstance().refreshSonglist();
					return null;
				}
				
			};
			asc.execute();
		}	
		
		// if tab album is selected
		if(tab.equals(taballAlbum)){
			if(ALBUM_CURRENT_SCREEN == Utils.FIRST_SCREEN){
				ft.show(Utils.getInstance().FRAGMENT_ALBUM_LIST);
			}
			else{
				ft.show(Utils.getInstance().FRAGMENT_ALBUM_DETAIL);
			}
		}
		// if tab artist is selected
		if(tab.equals(taballArtist)){
			if(ARTIST_CURRENT_SCREEN == Utils.FIRST_SCREEN){
				ft.show(Utils.getInstance().FRAGMENT_ARTIST_LIST);
			}
			else{
				ft.show(Utils.getInstance().FRAGMENT_ARTIST_DETAIL);
			}
		}
		if(tab.equals(tabdirectRemote)){
			ft.show(Utils.getInstance().FRAGMENT_DIRECT_REMOTE);			
		}
		if(tab.equals(tabnowPlaying)){
			ft.show(Utils.getInstance().FRAGMENT_NOW_PLAYING);			
		}
		if(tab.equals(tabsettings)){
			ft.show(Utils.getInstance().FRAGMENT_SETTINGS);			
		}
		ft.commit();
	}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction _ft) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(tab.equals(taballsong)){
		    ft.hide(Utils.getInstance().FRAGMENT_ALL_SONG);
		}
		if(tab.equals(taballAlbum)){
			ft.hide(Utils.getInstance().FRAGMENT_ALBUM_LIST);
			if(Utils.getInstance().FRAGMENT_ALBUM_DETAIL != null)
				ft.hide(Utils.getInstance().FRAGMENT_ALBUM_DETAIL);
		}
		if(tab.equals(taballArtist)) {
			ft.hide(Utils.getInstance().FRAGMENT_ARTIST_LIST);
			if(Utils.getInstance().FRAGMENT_ARTIST_DETAIL != null)
				ft.hide(Utils.getInstance().FRAGMENT_ARTIST_DETAIL);
		}
		if(tab.equals(tabdirectRemote))
			ft.hide(Utils.getInstance().FRAGMENT_DIRECT_REMOTE);
		if(tab.equals(tabnowPlaying)) 
			ft.hide(Utils.getInstance().FRAGMENT_NOW_PLAYING);
		if(tab.equals(tabsettings)) 
			ft.hide(Utils.getInstance().FRAGMENT_SETTINGS);
		ft.commit();
	}
	
	@Override
	public void onBackPressed(){
		Tab currentTab = getActionBar().getSelectedTab();
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		if(currentTab.equals(taballsong) || currentTab.equals(tabnowPlaying) 
				|| currentTab.equals(tabsettings) || currentTab.equals(tabdirectRemote)){
			super.onBackPressed();
		}
		if(currentTab.equals(taballAlbum)){
			if(ALBUM_CURRENT_SCREEN == Utils.FIRST_SCREEN){
				super.onBackPressed();
			}
			if(ALBUM_CURRENT_SCREEN == Utils.SECOND_SCREEN){
				ALBUM_CURRENT_SCREEN = Utils.FIRST_SCREEN;
				ft.hide(Utils.getInstance().FRAGMENT_ALBUM_DETAIL);
				ft.show(Utils.getInstance().FRAGMENT_ALBUM_LIST);
			}
		}
		if(currentTab.equals(taballArtist)){
			if(ARTIST_CURRENT_SCREEN == Utils.FIRST_SCREEN){
				super.onBackPressed();
			}
			if(ARTIST_CURRENT_SCREEN == Utils.SECOND_SCREEN){
				ARTIST_CURRENT_SCREEN = Utils.FIRST_SCREEN;
				ft.hide(Utils.getInstance().FRAGMENT_ARTIST_DETAIL);
				ft.show(Utils.getInstance().FRAGMENT_ARTIST_LIST);
			}
		}
		ft.commit();
	}
	
}
