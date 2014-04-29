package edu.ptit.xbmc.adapter;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.model.Album;
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

public class AllAlbumAdapter extends BaseAdapter {
	private  ArrayList<Album> list;
	private Activity activity;
	public AllAlbumAdapter (Activity a,ArrayList<Album> l){
		//super();
		list = l;
		activity = a;
	}
	public void setList(ArrayList<Album> l){
		list = l;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
		if(vi == null){
			vi = inflater.inflate(R.layout.list_row_for_album_list_new, null);
		}
		//ImageView thumb = (ImageView) vi.findViewById(R.id.thumbnail);
		TextView albumTitle = (TextView)vi.findViewById(R.id.txtArtistNameInArtistList);
		TextView artistOfAlbum = (TextView) vi.findViewById(R.id.descriptionOfArtistInartistList);
		TextView txtDescriptionOfAlbum = (TextView) vi.findViewById(R.id.txtDescriptionOfAlbum);
		TextView yearOfAlbum = (TextView) vi.findViewById(R.id.txtArtistIDInartistList);
		ImageView albumArtInAlbumList = (ImageView) vi.findViewById(R.id.albumArtInArtistList);
		//-----------------------------------------------------------------
		Album album = list.get(position);
		if(album == null) {			
			return null;
		}
		albumTitle.setText(album.getAlbumtitle());
		artistOfAlbum.setText(album.getDisplayartist());
		txtDescriptionOfAlbum.setText(album.getDescription());
		yearOfAlbum.setText(album.getYear()== 0?"":album.getYear() + "");
		
		try {
			String url = PiConnector.getInstance().getPiURL() + "/image/" + URLEncoder.encode(album.getThumbnail() ,"utf-8");
			if(url.length() > (PiConnector.getInstance().getPiURL() + "/image/").length())
				new DownloadImageTask(albumArtInAlbumList).execute(url);
			Log.d("url-album-art", url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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
