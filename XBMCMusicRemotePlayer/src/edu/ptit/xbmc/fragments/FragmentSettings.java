package edu.ptit.xbmc.fragments;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.model.Settings;
import edu.ptit.xbmc.sp.Constants;
import edu.ptit.xbmc.sp.SPUtils;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentSettings extends Fragment implements OnClickListener{
	EditText txtPiURL, txtPiPort;
	Button btnSave;
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_settings,container,false);
		setRetainInstance(true);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		txtPiURL  = (EditText)view. findViewById(R.id.txtPiURL);
		txtPiPort  = (EditText) view.findViewById(R.id.txtPiPort);
		btnSave.setOnClickListener(this);
		String piURL = SPUtils.getPiUrlFromSP(getActivity());
		int piPort = SPUtils.getPiPortFromSP(getActivity());
		if(piURL.equals("") == false) txtPiURL.setText(piURL);
		if(piPort >-1) txtPiPort.setText(piPort+"");
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.equals(btnSave)){
			if(txtPiURL.getText() == null 
					|| txtPiURL.getText().toString().equals("") 
					|| txtPiPort.getText() == null 
					|| txtPiPort.getText().toString().equals("")) {
				Toast.makeText(getActivity(), "Please Enter Pi URL and port", Toast.LENGTH_LONG).show();
				return;
			}
			new CheckURLAndPort(getActivity()).execute();
		}
	}
	
	private class CheckURLAndPort extends AsyncTask<Void, Void, Boolean>{
		Activity act;
		public CheckURLAndPort(Activity a){
			act = a;
			Toast.makeText(act, "Start checking...", Toast.LENGTH_SHORT).show();
		}
		@Override
		protected Boolean doInBackground(Void... params) {
			Boolean check = PiConnector.getInstance().checkURLandPort(
					txtPiURL.getText().toString(),
					Integer.parseInt(txtPiPort.getText().toString()));			
			return check;
		}
		@Override
		protected void onPostExecute (Boolean result){			
			if(result == true){
				Settings settings = new Settings();			
				settings.setPiPort(Integer.parseInt(txtPiPort.getText().toString()));
				settings.setPiUrl(txtPiURL.getText().toString());			
				// khuongdv Oct 3
				
				SPUtils.insertSettingsToSP(getActivity(), settings);
				PiConnector.getInstance().setPiURL("http://" + settings.getPiUrl() + ":" + settings.getPiPort());
				Utils.getInstance().FRAGMENT_ALL_SONG = new FragmentAllSong();
				Utils.getInstance().switchTab(Constants.TAB_ALLSONG);
				//khuongdv Oct 3				
			}
			else {
				Toast.makeText(act, "@@@@", Toast.LENGTH_LONG).show();
			}
		}
	}
}
