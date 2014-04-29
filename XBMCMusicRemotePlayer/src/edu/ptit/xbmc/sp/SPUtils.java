package edu.ptit.xbmc.sp;

import edu.ptit.xbmc.model.Settings;
import edu.ptit.xbmc.tools.PiConnector;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.widget.Toast;

/**
 * Shared Preference Utilities
 * @author Khuong
 *
 */
public class SPUtils {
	/**
	 * The unique instance of this class
	 */
	private static SPUtils instance = null;
	public static SPUtils getInstance() {
		if (instance == null) {
			instance = new SPUtils();
		}
		return instance;
	}

	private SPUtils() {}
	
	public static boolean checkSettings(Context context){
		
		try {
			SharedPreferences sp = 
					context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			String piUrl = sp.getString(Constants.STR_PI_URL, null);
			if(piUrl == null) {				
				return false;
			}
			int piPort = sp.getInt(Constants.STR_PI_PORT, 0);
			boolean check = PiConnector.getInstance().checkURLandPort(piUrl, piPort);			
			return check;
		} catch (Exception e) {					
			return false;
		}
	}
	public static void insertSettingsToSP(Context context,Settings settings){
		Log.i("URL in SPUtils", "http://" + settings.getPiUrl() + ":" + settings.getPiPort());
		try {
			SharedPreferences sp = 
					context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			if(settings.getPiUrl() != null) editor.putString(Constants.STR_PI_URL, settings.getPiUrl());
			if(settings.getPiPort() > 0) editor.putInt(Constants.STR_PI_PORT, settings.getPiPort());
			editor.apply();			
			Toast.makeText(context, "Insert successful", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Khuongdv start oct4
	public static String getPiUrlFromSP(Context context){
		String piUrl = "";
		SharedPreferences sp = 
				context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		piUrl = sp.getString(Constants.STR_PI_URL, "");
		return piUrl;
	}
	public static int getPiPortFromSP(Context context){
		SharedPreferences sp = 
				context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		int piPort = sp.getInt(Constants.STR_PI_PORT, -1);
		return piPort;
	}
	//khuongdv end oct4
}

