package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harry on 3/16/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{

    private static final String TAG = TrailerAdapter.class.getSimpleName();

    private Context mContext;

    private List<Trailer> mTrailers;

    private final Trailer mLock = new Trailer();

    public TrailerAdapter(List<Trailer> trailers){
        mTrailers = trailers;
    }

    public void add(Trailer trailer){
        synchronized (mLock){
            mTrailers.add(trailer);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        synchronized (mLock){
            mTrailers.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.trailer_list_item,parent,false);
        TrailerViewHolder trailerViewHolder = new TrailerViewHolder(view);
        return trailerViewHolder;
    }



    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Log.d(TAG,"#"+position);
        Trailer trailer = mTrailers.get(position);
        String url = mContext.getString(R.string.youtube_thumbnail_url,trailer.getKey());
        Picasso.with(mContext).load(url).into(holder.imageView);

        holder.textView.setText(trailer.getName());
    }


    @Override
    public int getItemCount() {
        return mTrailers.size();
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ImageView imageView;
        public final TextView textView;

        public TrailerViewHolder(View itemView){
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.trailer_image);
            textView = (TextView) itemView.findViewById(R.id.trailer_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Trailer trailer = mTrailers.get(position);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mContext.getString(R.string.youtube_trailer_url)+trailer.getKey()));
            mContext.startActivity(intent);
        }
    }
}
