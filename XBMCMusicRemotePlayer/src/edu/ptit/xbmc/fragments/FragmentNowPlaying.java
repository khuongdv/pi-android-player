package edu.ptit.xbmc.fragments;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import edu.ptit.xbmc.R;
import edu.ptit.xbmc.model.Song;
import edu.ptit.xbmc.tools.PiConnector;
import edu.ptit.xbmc.tools.Utils;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class FragmentNowPlaying extends Fragment implements OnClickListener, OnSeekBarChangeListener{
	String currentRepeat = "1";
	String currentShuffled = "false";
	Song currentSong = new Song();
	TextView tvSongname;
	TextView tvCurrentTime, tvDuration;
	ImageView imageViewNowplayingThumbnail;

	SeekBar progressbar, volumeprogress;

	// Buttons
	ImageButton btnPrev, btnNext, btnPlay, btnRepeat, btnShuffle, btnVolume;
	Song tmpSong;
	int currentPosition = 0;
	Timer timer;
	
	public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState){
		View view = inf.inflate(R.layout.fragment_now_playing_new, container,false);	
		//From old project
		setRetainInstance(true);
		tvSongname = (TextView) view.findViewById(R.id.nowplaying_title_of_song);
		tvCurrentTime = (TextView) view.findViewById(R.id.nowplaying_timer_mediatime);
		tvDuration = (TextView) view.findViewById(R.id.nowplaying_timer_durationtime);
		progressbar = (SeekBar) view.findViewById(R.id.nowplaying_progress);
		btnNext = (ImageButton) view.findViewById(R.id.nowplaying_next);
		btnShuffle = (ImageButton) view.findViewById(R.id.nowplaying_shuffled);
		btnRepeat = (ImageButton) view.findViewById(R.id.nowplaying_repeat);
		btnPrev = (ImageButton) view.findViewById(R.id.nowplaying_previous);
		btnPlay = (ImageButton) view.findViewById(R.id.nowplaying_playpause);
		imageViewNowplayingThumbnail = (ImageView) view.findViewById(R.id.nowplaying_thumbnail);
		// Set OnclickListener for buttons
		btnNext.setOnClickListener(this);
		btnShuffle.setOnClickListener(this);
		btnRepeat.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		// added
		volumeprogress = (SeekBar) view.findViewById(R.id.nowplaying_seekbar_volume);
		volumeprogress.setOnSeekBarChangeListener(this);
		btnVolume = (ImageButton) view.findViewById(R.id.nowplaying_volume);
		btnVolume.setOnClickListener(this);
		timer = new Timer();
		timer.schedule(new RequestCurrentState(), 0, 1000);
		//End
		return view;
	}
	// From old
	private void toggleVisibleOfVolumeSeekbar(){
		if(volumeprogress.getVisibility() == View.VISIBLE){
			volumeprogress.setVisibility(View.GONE);
		}
		else volumeprogress.setVisibility(View.VISIBLE);
	}
	class RequestCurrentState extends TimerTask {
		@Override
		public void run() {
			new Requester().execute();
		}
	}
	class Requester extends AsyncTask<Void, Void, Hashtable<String, String>> {

		@Override
		protected Hashtable<String, String> doInBackground(Void... params) {
			Hashtable<String, String> result = PiConnector.getInstance()
					.getCurrentStateOfActivePlayer();
			if (result != null) {
				if (result.containsKey("error")) {
					return null;
				}
			}
			String thumbnail = result.get("thumbnail");
			if (thumbnail != null && thumbnail.length() > 0) {
				try {
					String url = (PiConnector.getInstance().getPiURL()
							+ "/image/" + URLEncoder.encode(thumbnail, "utf-8"));
					if (url != null)
						result.put("url", url);
//					Log.e("URL of thumbnail", url);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Hashtable<String, String> result) {
			try {
				//Start Oct 8
				String currentSongId = result.get("currentsongid");
				try{
				int id = Integer.parseInt(currentSongId);
				currentSong.setSongid(id);
				}catch(Exception ex){
					
				}				
				//End oct 8
				String title = result.get("title");
				String url = result.get("url");
				String artist = result.get("artist");
				String percentage = result.get("percentage");
				String isShuffled = result.get("shuffled");
				String repeat = result.get("repeat");
				int seconds = Integer.parseInt(result.get("seconds"));
				int minutes = Integer.parseInt(result.get("minutes"));
				int duration = Integer.parseInt(result.get("duration"));
				int volume = Integer.parseInt(result.get("volume"));
				volumeprogress.setProgress(volume);
				if (repeat != null) {
					 currentRepeat = repeat; // added Oct 8
					 if (repeat.contains("0")) {
					 btnRepeat.setBackgroundResource(R.drawable.img_btn_repeat_none);
					 } else if(repeat.contains("1"))
					 btnRepeat
					 .setBackgroundResource(R.drawable.img_btn_repeat_one);
					 else if(repeat.contains("3")) btnRepeat.setBackgroundResource(R.drawable.img_btn_repeat_all);
				}
				if (isShuffled != null) {
					// Log.e("SHUFFLED", isShuffled);
					currentShuffled = isShuffled; // added Oct 8
					if (isShuffled.equalsIgnoreCase("false")) {
						btnShuffle.setBackgroundResource(R.drawable.img_btn_shuffle);
					} else
						btnShuffle
								.setBackgroundResource(R.drawable.img_btn_shuffle_pressed);
				}
				float fpercentage = Float.parseFloat(percentage);
				int ipercentage = (int) fpercentage;
				progressbar.setProgress(ipercentage);
				if((title + " - " + artist).length() > 40)
				tvSongname.setText((title + " - " + artist).substring(0, 40) + "...");
				else tvSongname.setText((title + " - " + artist));
				tvDuration.setText(Utils.getFormatedDuration(duration));
				tvCurrentTime.setText(minutes + ":" + seconds);
				if (url != null)
					new DownloadImageTask(imageViewNowplayingThumbnail)
							.execute(url);
				// Log.i("urlthumbnail", "" + url);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		ImageView imgView;

		public DownloadImageTask(ImageView img) {
			this.imgView = img;
		}

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

		protected void onPostExecute(Bitmap result) {
			if (result == null)
				return;
			imgView.setImageBitmap(result);
		}
	}
	private void playNext() {
		new PlayNext().execute();
	}

	class PlayNext extends AsyncTask<Boolean, Void, Void> {		
		public PlayNext(){
		}
		@Override
		protected Void doInBackground(Boolean... params) {
			PiConnector.getInstance().playNextSong();
			return null;
		}

	}

	private void playPrevious() {
		new PlayPrevious().execute();
	}

	class PlayPrevious extends AsyncTask<Boolean, Void, Void> {

		@Override
		protected Void doInBackground(Boolean... params) {
			PiConnector.getInstance().playPreviousSong();
			return null;
		}

	}

	private void toggleShuffled() {
		new ToggleShuffled().execute();
	}

	// Oct 8
	class ToggleShuffled extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			PiConnector.getInstance().toggleShuffled();
			return null;
		}

	}

	// End Oct 8
	/**
	 * This method will change from repeat-none to repeat-all and vice versa. To
	 * change repeat mode to repeat-one, go to settings screen
	 */
	private void toggleRepeat() {
		new ToggleRepeat().execute();

	}

	// Khuongdv oct 8 start
	class ToggleRepeat extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			PiConnector.getInstance().cycleRepeat();
			return null;
		}

	}

	// Khuongdv Oct 8 end
	// khuongdv oct 4 start
	private void playPause() {
		new PlayPauseHandler().execute();
	}

	class PlayPauseHandler extends AsyncTask<Boolean, Void, Integer> {

		@Override
		protected Integer doInBackground(Boolean... params) {
			
			int speed = PiConnector.getInstance().togglePlayPause();
			return speed;
		}

		@Override
		protected void onPostExecute(Integer speed) {
			Log.e("END SEND PP REQUEST. RESULT = " + speed, "OK");
			if (speed == 1) {
				btnPlay.setBackgroundResource(R.drawable.img_btn_pause);
			} else
				btnPlay.setBackgroundResource(R.drawable.img_btn_play);
		}
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
	}
	class ChangeVolume extends AsyncTask<Void, Void, Void>{
		Activity act;
		int volume;
		public ChangeVolume(Activity ac, int vol){
			act = ac;
			volume = vol;
		}
		@Override
		protected Void doInBackground(Void... params) {
			PiConnector.getInstance().setVolume(volume);
			return null;
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		int volume = volumeprogress.getProgress();
		new ChangeVolume(getActivity(),volume).execute();
	}
	//End
	@Override
	public void onClick(View v) {
		if (v.equals(btnNext)) {
			playNext();
		}
		if (v.equals(btnPlay)) {
			playPause();
		}
		if (v.equals(btnPrev)) {
			playPrevious();
		}
		if (v.equals(btnRepeat)) {
			toggleRepeat();
		}
		if (v.equals(btnShuffle)) {
			toggleShuffled();
		}
		if(v.equals(btnVolume)){
			toggleVisibleOfVolumeSeekbar();
		}
	}

	
}
