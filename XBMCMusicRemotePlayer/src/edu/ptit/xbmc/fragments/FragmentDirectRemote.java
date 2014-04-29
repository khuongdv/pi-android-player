package edu.ptit.xbmc.fragments;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class FragmentDirectRemote  extends Fragment implements OnClickListener{
	ImageButton btnUp, btnDown, btnLeft, btnRight, btnOk, btnHome,
	btnContextMenu, btnBack, btnGotoMusic;
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_direct_remote,container,false);	
		findViewssById(view);
		setRetainInstance(true);
		return view;
	}
	void findViewssById(View v){
		btnUp = (ImageButton) v.findViewById(R.id.btnRemoteUp);
		btnDown = (ImageButton)v. findViewById(R.id.btnRemoteDown);
		btnLeft = (ImageButton) v.findViewById(R.id.btnRemoteLeft);
		btnRight = (ImageButton) v.findViewById(R.id.btnRemoteRight);
		btnOk = (ImageButton)v. findViewById(R.id.btnRemoteOk);
		btnHome = (ImageButton)v. findViewById(R.id.btnRemoteHome);
		btnBack = (ImageButton) v.findViewById(R.id.btnRemoteBack);
		btnContextMenu = (ImageButton)v. findViewById(R.id.btnRemoteMenu);
		btnGotoMusic = (ImageButton)v. findViewById(R.id.btnRemoteMovetoAllSong);
		
		btnUp.setOnClickListener(this);
		btnDown.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnContextMenu.setOnClickListener(this);
		btnGotoMusic.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(btnUp)){
			sendRequest("Up");
		}
		
		if(v.equals(btnDown)){
			sendRequest("Down");
		}
		
		if(v.equals(btnLeft)){
			sendRequest("Left");			
		}
		
		if(v.equals(btnRight)){
			sendRequest("Right");			
		}
		
		if(v.equals(btnOk)){
			sendRequest("Select");			
		}
		
		if(v.equals(btnHome)){
			sendRequest("Home");
		}
		
		if(v.equals(btnBack)){
			sendRequest("Back");
		}
		
		if(v.equals(btnContextMenu)){
			sendRequest("ContextMenu");
		}
		
		if(v.equals(btnGotoMusic)){
			Utils.getInstance().switchTab(Constants.TAB_ALLSONG);
		}
	}
	void sendRequest(String method){
		new Requester(method).execute();		
	}
	class Requester extends AsyncTask<Void, Void, Void>{
		private String method;
		public Requester(String mt){
			method = mt;
		}
		@Override
		protected Void doInBackground(Void... params) {
			PiConnector.getInstance().sendInputMethod(method);
			return null;
		}
		
	}
}
