package edu.ptit.xbmc.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.model.Song;
import edu.ptit.xbmc.tools.PiConnector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AllSongAdapter2 extends BaseAdapter {
	private  ArrayList<Song> list;
	private Activity activity;
	public AllSongAdapter2 (Activity a,ArrayList<Song> l){
		//super();
		list = l; 
		activity = a;
		// Oct 8
		Collections.sort(list, new Comparator<Song>() {

			@Override
			public int compare(Song lhs, Song rhs) {
				// TODO Auto-generated method stub
				return lhs.getSongid() - rhs.getSongid();
			}
		});
		// End Oct 8
	}
	public void setList(ArrayList<Song> l){
		list = l;
		// Oct 8
		Collections.sort(list, new Comparator<Song>() {

			@Override
			public int compare(Song lhs, Song rhs) {
				// TODO Auto-generated method stub
				return lhs.getSongid() - rhs.getSongid();
			}
		});
		// End Oct 8
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
		if(vi == null){
			vi = inflater.inflate(R.layout.list_row, null);
		}
		//ImageView thumb = (ImageView) vi.findViewById(R.id.thumbnail);
		TextView title = (TextView) vi.findViewById(R.id.title);
		TextView artist = (TextView) vi.findViewById(R.id.artist);
		TextView duration = (TextView) vi.findViewById(R.id.duration);
		ImageView thumbnail = (ImageView) vi.findViewById(R.id.allSongThumbnail);
		Song song = list.get(position);
		if(song == null) {
			//Log.e("nulll", "null song");
			return null;
		}
		title.setText(song.getName());
		artist.setText(song.getFormatedArtist());
		duration.setText(song.getFormatedDuration());
//		try {
//			String url = PiConnector.getInstance().getPiURL() + "/image/" + URLEncoder.encode(song.getThumbnail(), "utf-8");
//			if(url.length() > (PiConnector.getInstance().getPiURL() + "/image/").length() ) new DownloadImageTask(thumbnail).execute(url);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		
		return vi;
	}
	//khuongdv start sep28
//	private Bitmap decodeFile(InputStream in){
//	        //Decode image size
//	        BitmapFactory.Options o = new BitmapFactory.Options();
//	        o.inJustDecodeBounds = true;
//	        BitmapFactory.decodeStream(in,null,o);
//
//	        //The new size we want to scale to
//	        final int REQUIRED_SIZE=70;
//
//	        //Find the correct scale value. It should be the power of 2.
//	        int scale=1;
//	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
//	            scale*=2;
//
//	        //Decode with inSampleSize
//	        BitmapFactory.Options o2 = new BitmapFactory.Options();
//	        o2.inSampleSize=scale;
//	        return BitmapFactory.decodeStream(in, null, o2);	    
//	}
//	public int dpToPx(int dp) {
//	    DisplayMetrics displayMetrics = activity.getContext().getResources().getDisplayMetrics();
//	    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
//	    return px;
//	}
	//khuongdv end sep28
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
		ImageView imgView;
		public DownloadImageTask(ImageView img){this.imgView = img;}
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			String urlDisplay = urls[0];
			Bitmap icon = null;
			InputStream in = null ;
			try {
				in = new URL(urlDisplay).openStream();
				icon = BitmapFactory.decodeStream(in);
				icon = Bitmap.createScaledBitmap(icon,64,64,false); 
			} catch (Exception e) {
				e.printStackTrace();
			}
			//khuongdv start sep28
			finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			//khuongdv end sep28
			return icon;
		}
		protected void onPostExecute(Bitmap result){
			if(result == null) return;
			imgView.setImageBitmap(result);
		}
	}

}
