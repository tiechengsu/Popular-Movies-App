package com.example.android.popularmovies;




import android.app.Fragment;
import android.app.LoaderManager;


import android.content.Intent;
import android.content.Loader;

import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapter.ReviewAdapter;
import com.example.android.popularmovies.adapter.TrailerAdapter;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Review;
import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.network.FetchReviewsTask;
import com.example.android.popularmovies.network.FetchTrailersTask;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 3/15/17.
 */

public class DetailActivityFragment extends Fragment {
    private final static String TAG = DetailActivityFragment.class.getSimpleName();

    private static final int FETCH_TRAILERS_LOADER = 1;
    private static final int FETCH_REVIEW_LOADER = 2;

    private CardView mTrailersCardView;
    private CardView mReviewsCardView;

    private RecyclerView mTrailersView;
    private RecyclerView mReviewsView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private View rootView;



    public DetailActivityFragment(){

    }




    Movie mMovie;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_detail_fragment,container,false);


        mTrailersCardView = (CardView) rootView.findViewById(R.id.trailer_cardview);
        mReviewsCardView = (CardView) rootView.findViewById(R.id.review_cardview);

        mTrailersView = (RecyclerView) rootView.findViewById(R.id.detail_trailers);
        mReviewsView = (RecyclerView) rootView.findViewById(R.id.detail_review);


        mTrailerAdapter = new TrailerAdapter(new ArrayList<Trailer>());
        mReviewAdapter = new ReviewAdapter(new ArrayList<Review>());



        mTrailersView.setAdapter(mTrailerAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        mTrailersView.setLayoutManager(layoutManager);
        mReviewsView.setAdapter(mReviewAdapter);
        mReviewsView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Intent intent = getActivity().getIntent();
        if(intent!=null&&intent.hasExtra(getString(R.string.movie_detail))){
            mMovie = intent.getParcelableExtra(getString(R.string.movie_detail));
            updateLayout(rootView);
        }

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(FETCH_TRAILERS_LOADER,null,FetchTrailerListener);
        loaderManager.initLoader(FETCH_REVIEW_LOADER,null,FetchReviewListener);

        return rootView;
    }

    private void updateLayout(View view){
        TextView title = (TextView) view.findViewById(R.id.movie_title);
        ImageView poster = (ImageView) view.findViewById(R.id.poster);
        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        TextView synopsis = (TextView) view.findViewById(R.id.synopsis);

        title.setText(mMovie.getTitle());
        Picasso.with(getActivity()).load(mMovie.getPoster()).into(poster);
        releaseDate.setText(mMovie.getReleaseDate());
        rating.setText(mMovie.getVoteAverage());
        rating.setTextColor(getColor(Double.parseDouble(mMovie.getVoteAverage())));
        synopsis.setText(mMovie.getOverview());
    }

    private int getColor(double vote_average){
        if(vote_average>=9.0) return ContextCompat.getColor(getActivity(),R.color.vote_perfect);
        else if(vote_average>=7.0) return ContextCompat.getColor(getActivity(),R.color.vote_good);
        else if(vote_average>=5.0) return ContextCompat.getColor(getActivity(),R.color.vote_normal);
        else return ContextCompat.getColor(getActivity(),R.color.vote_bad);
    }


    //callback for FetchTrailer
    private LoaderManager.LoaderCallbacks<List<Trailer>> FetchTrailerListener = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            return new FetchTrailersTask(getActivity(),mMovie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            if(data!=null&&data.size()>0){
                mTrailersCardView.setVisibility(rootView.VISIBLE);
                if(mTrailerAdapter!=null){
                    mTrailerAdapter.clear();
                    for(Trailer trailer:data){
                        mTrailerAdapter.add(trailer);
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };

    //callback for FetchReview
    private LoaderManager.LoaderCallbacks<List<Review>> FetchReviewListener = new LoaderManager.LoaderCallbacks<List<Review>>() {
        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            return new FetchReviewsTask(getActivity(),mMovie.getMovieId());
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            if(data!=null&&data.size()>0){
                mReviewsCardView.setVisibility(rootView.VISIBLE);
                if(mReviewAdapter!=null){
                    mReviewAdapter.clear();
                    for(Review review:data){
                        mReviewAdapter.add(review);
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    };


}
