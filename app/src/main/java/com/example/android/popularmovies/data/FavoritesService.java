package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;
import com.example.android.popularmovies.model.Movie;

/**
 * Created by harry on 3/18/17.
 */

public class FavoritesService {

    private final Context mContext;

    public FavoritesService(Context context){
        mContext = context.getApplicationContext();
    }

    public void addToFavorites(Movie movie){
        mContext.getContentResolver().insert(MovieEntry.CONTENT_URI, movie.toContentValues());
    }

    public void removeFromFavorites(Movie movie){
        mContext.getContentResolver().delete(MovieEntry.CONTENT_URI,MovieEntry.COLUMN_ID+"="+movie.getMovieId(),null);
    }

    public boolean isFavorite(Movie movie){
        boolean favorite = false;
        Cursor cursor = mContext.getContentResolver().query(
                MovieEntry.CONTENT_URI,
                null,
                MovieEntry.COLUMN_ID+"="+movie.getMovieId(),
                null,
                null
        );
        if(cursor!=null){
            favorite = cursor.getCount()!=0;
            cursor.close();
        }
        return favorite;
    }
}
