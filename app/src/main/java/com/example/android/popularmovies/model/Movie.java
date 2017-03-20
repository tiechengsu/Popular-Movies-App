package com.example.android.popularmovies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmovies.MovieListFragment;
import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by harry on 3/15/17.
 */


public class Movie implements Parcelable {
    private int mMovieId;
    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mVoteAverage;
    private String mReleaseDate;
    private String mBackDrop;

    public Movie(){}


    public Movie(int movieId, String title, String poster, String backdrop, String overview,
                 String voteAverage, String releaseDate) {
        mMovieId = movieId;
        mTitle = title;
        mPoster = poster;
        mBackDrop = backdrop;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mReleaseDate = releaseDate;
    }


    public int getMovieId(){ return mMovieId; }

    public String getTitle() {
        return mTitle;
    }

    public String getPoster() {
        return mPoster;
    }

    public String getBackDrop() { return mBackDrop; }

    public String getOverview() {
        return mOverview;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }




    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mPoster);
        dest.writeString(mBackDrop);
        dest.writeString(mOverview);
        dest.writeString(mVoteAverage);
        dest.writeString(mReleaseDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>(){
        public Movie createFromParcel(Parcel in){
            return new Movie(in);
        }

        public Movie[] newArray(int size){
            return new Movie[size];
        }
    };

    private Movie(Parcel in){
        mMovieId = in.readInt();
        mTitle = in.readString();
        mPoster = in.readString();
        mBackDrop = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = in.readString();
    }

    public Movie(Cursor cursor){
        mMovieId = cursor.getInt(MovieListFragment.COL_MOVIE_ID);
        mTitle = cursor.getString(MovieListFragment.COL_ORIGINAL_TITLE);
        mPoster = cursor.getString(MovieListFragment.COL_POSTER_PATH);
        mBackDrop = cursor.getString(MovieListFragment.COL_BACKDROP_PATH);
        mOverview = cursor.getString(MovieListFragment.COL_OVERVIEW);
        mVoteAverage= cursor.getString(MovieListFragment.COL_VOTE_AVERAGE);
        mReleaseDate = cursor.getString(MovieListFragment.COL_RELEASE_DATE);
    }

    //helper function to local database
    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COLUMN_ID,mMovieId);
        values.put(MovieEntry.COLUMN_ORIGINAL_TITLE,mTitle);
        values.put(MovieEntry.COLUMN_POSTER_PATH,mPoster);
        values.put(MovieEntry.COLUMN_BACKDROP_PATH,mBackDrop);
        values.put(MovieEntry.COLUMN_OVERVIEW,mOverview);
        values.put(MovieEntry.COLUMN_VOTE_AVERAGE,mVoteAverage);
        values.put(MovieEntry.COLUMN_RELEASE_DATE,mReleaseDate);
        return values;
    }
}
