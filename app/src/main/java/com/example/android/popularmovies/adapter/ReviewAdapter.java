package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;


import java.util.List;

/**
 * Created by harry on 3/17/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private Context mContext;

    private List<Review> mReviews;

    private final Review mLock = new Review();

    public ReviewAdapter(List<Review> reviews){
        mReviews = reviews;
    }

    public void add(Review review){
        synchronized (mLock){
            mReviews.add(review);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        synchronized (mLock){
            mReviews.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(R.layout.review_list_item,parent,false);
        ReviewAdapter.ReviewViewHolder reviewViewHolder = new ReviewAdapter.ReviewViewHolder(view);
        return reviewViewHolder;
    }



    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        Log.d(TAG,"#"+position);
        Review review = mReviews.get(position);

        holder.contentView.setText(review.getContent());
        holder.authorView.setText(review.getAuthor());
    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }


    class ReviewViewHolder extends RecyclerView.ViewHolder{
        public final TextView contentView;
        public final TextView authorView;

        public ReviewViewHolder(View itemView){
            super(itemView);
            contentView = (TextView) itemView.findViewById(R.id.review_content);
            authorView = (TextView) itemView.findViewById(R.id.review_author);

        }

    }
}
