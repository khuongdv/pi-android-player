package edu.ptit.xbmc.adapter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.model.Artist;
import edu.ptit.xbmc.tools.PiConnector;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllArtistAdapter extends BaseAdapter {
	private  ArrayList<Artist> list;
	private Activity activity;
	public AllArtistAdapter (Activity a,ArrayList<Artist> l){
		list = l;
		activity = a;
	}
	public void setList(ArrayList<Artist> l){
		list = l;
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
			vi = inflater.inflate(R.layout.list_row_for_artist_list_new, null);
		}
		//ImageView thumb = (ImageView) vi.findViewById(R.id.thumbnail);
		TextView artistNameInArtistList = (TextView)vi.findViewById(R.id.artistNameInArtistList);
		TextView descriptionOfArtistInartistList = 
				(TextView) vi.findViewById(R.id.descriptionOfArtistInartistList);
		ImageView albumArtInArtistList = (ImageView) vi.findViewById(R.id.albumArtInArtistList);
		//-----------------------------------------------------------------
		Artist artist = list.get(position);
		if(artist == null) {
			Log.e("nulll", "null album");
			return null;
		}
		artistNameInArtistList.setText(artist.getArtistName());
		descriptionOfArtistInartistList.setText((artist.getDescription()==null ||artist.getDescription().equals(""))?"No description":artist.getDescription());
		
		try {
			String url = PiConnector.getInstance().getPiURL() + "/image/" + URLEncoder.encode(artist.getThumbnail() ,"utf-8");
			if(url.length() > (PiConnector.getInstance().getPiURL() + "/image/").length())
				new DownloadImageTask(albumArtInArtistList).execute(url);
			Log.d("url-album-art", url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return vi;
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
		ImageView imgView;
		public DownloadImageTask(ImageView img){this.imgView = img;}
		
		@Override
		protected Bitmap doInBackground(String... urls) {
			String urlDisplay = urls[0];
			Bitmap icon = null;
			try {
				InputStream in = new URL(urlDisplay).openStream();
				icon = BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return icon;
		}
		protected void onPostExecute(Bitmap result){
			if(result == null) return;
			imgView.setImageBitmap(result);
		}
	}
}
