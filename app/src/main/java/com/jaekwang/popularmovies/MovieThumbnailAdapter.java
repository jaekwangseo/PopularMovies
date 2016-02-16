package com.jaekwang.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Custom adapter for Grid View
 * Created by Jaekwang on 2/11/2016.
 */
public class MovieThumbnailAdapter extends BaseAdapter{

    private final Object mLock = new Object();

    private Context mContext;
    private String[] imagePaths;

    public MovieThumbnailAdapter(Context context, String[] imagePaths) {
        this.mContext = context;
        this.imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        if (imagePaths == null) {
            return 0;
        } else {
            return imagePaths.length;
        }
    }

    @Override
    public String getItem(int position) {
        return imagePaths[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(270, 270));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        String path = getItem(position);

         Picasso.with(mContext).load(path).into(imageView);
        //imageView.setImageResource(mThumbIds[position]);
        return imageView;

    }
}
