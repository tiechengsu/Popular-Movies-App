package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by harry on 3/18/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID +                    " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                        MovieEntry.COLUMN_ID +              " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_ORIGINAL_TITLE +  " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_POSTER_PATH +     " TEXT, " +
                        MovieEntry.COLUMN_BACKDROP_PATH +   " TEXT, " +
                        MovieEntry.COLUMN_OVERVIEW +        " TEXT, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE +    " TEXT, " +
                        MovieEntry.COLUMN_RELEASE_DATE +    " TEXT); ";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
