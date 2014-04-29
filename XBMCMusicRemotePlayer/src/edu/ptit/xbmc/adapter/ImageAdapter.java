package edu.ptit.xbmc.adapter;

import edu.ptit.xbmc.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

 
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
 
    // Keep all Images in array
    public Integer[] mThumbIds = {
          R.drawable.allsong,R.drawable.album,R.drawable.artist
          ,R.drawable.setting
    };
    public String[] mLabels = {
            "All Song","Albums","Artists","Settings"
      };
 
    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }
 
    @Override
    public int getCount() {
        return mThumbIds.length;
    }
 
    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) mContext
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     
    		View gridView;
     
    		if (convertView == null) {
     
    			gridView = new View(mContext);
     
    			// get layout from mobile.xml
    			gridView = inflater.inflate(R.layout.grid_view_cell, null);
     
    			// set value into textview
    			TextView textView = (TextView) gridView
    					.findViewById(R.id.grid_item_label);
    			textView.setText(mLabels[position]);
     
    			// set image based on selected text
    			ImageView imageView = (ImageView) gridView
    					.findViewById(R.id.grid_item_image);
     
    			String mobile = mLabels[position];
//     /"All Song","Albums","Artists","Settings"
    			if (mobile.equals("All Song")) {
    				imageView.setImageResource(R.drawable.allsong);
    			} else if (mobile.equals("Albums")) {
    				imageView.setImageResource(R.drawable.album);
    			} else if (mobile.equals("Artists")) {
    				imageView.setImageResource(R.drawable.artist);
    			} else {
    				imageView.setImageResource(R.drawable.setting);
    			}
     
    		} else {
    			gridView = (View) convertView;
    		}
     
    		return gridView;
    }
 
}