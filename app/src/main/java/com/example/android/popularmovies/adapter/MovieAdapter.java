package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harry on 3/15/17.
 */

public class MovieAdapter extends ArrayAdapter<String>{
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private int mLayoutId;
    private int mImageViewId;
    private final Movie mLock = new Movie();
    private final List<String> mUrls;


    public MovieAdapter(Context context, int layoutId, int imageViewID, List<String> urls){
        super(context,0,urls);
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
        mLayoutId = layoutId;
        mImageViewId = imageViewID;
        mUrls = urls;
    }

    @Override
    public void add(@Nullable String url) {
        synchronized (mLock){
            mUrls.add(url);
        }
        notifyDataSetChanged();
    }

    @Override
    public void clear(){
        synchronized (mLock){
            mUrls.clear();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String url;
        View view = convertView;
        if(view==null){
            view = mLayoutInflater.inflate(mLayoutId,parent,false);
        }
        ImageView imageView = (ImageView) view.findViewById(mImageViewId);
        url = mUrls.get(position);
        Picasso.with(mContext).load(url).into(imageView);
        return view;
    }


}
