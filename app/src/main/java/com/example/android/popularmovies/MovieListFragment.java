package com.example.android.popularmovies;


import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.GridView;


import com.example.android.popularmovies.adapter.MovieAdapter;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.network.FetchMoviesTask;
import com.example.android.popularmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harry on 3/15/17.
 */

public class MovieListFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener,
        LoaderManager.LoaderCallbacks<Cursor>{
    private final static String TAG = MovieListFragment.class.getSimpleName();
    private String sortBy;
    private final static String MOVIE_KEY = "movie_key";

    private static final int FETCH_FAVORITE_LOADER = 3;



    private MovieAdapter mMovieAdapter;

    ArrayList<Movie> mMovies = new ArrayList<>();

    private Cursor mCursor;


    private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_ID,
            MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_BACKDROP_PATH,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
    };

    public static final int COL_ID = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_ORIGINAL_TITLE = 2;
    public static final int COL_POSTER_PATH = 3;
    public static final int COL_BACKDROP_PATH = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_VOTE_AVERAGE = 6;
    public static final int COL_RELEASE_DATE = 7;


    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortBy = sharedPreferences.getString(getString(R.string.pref_sort_by_key),
                getString(R.string.pref_sort_by_popularity_value));
        getMovies();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState!=null){
            ArrayList<Movie> movies;
            movies = savedInstanceState.getParcelableArrayList(MOVIE_KEY);
            mMovies.clear();
            mMovies.addAll(movies);
        }
        setupSharedPreferences();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        mMovieAdapter = new MovieAdapter(getActivity(),R.layout.movie_list_item,
                R.id.movie_list_item_imageview,new ArrayList<String>());

        View view = inflater.inflate(R.layout.fragment_movie_list,container,false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_gridview);
        gridView.setAdapter(mMovieAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovies.get(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class).
                        putExtra(getString(R.string.movie_detail),movie);
                startActivity(intent);
            }
        });



        return view;
    }


    private void getMovies(){
        if(!sortBy.contentEquals(getString(R.string.pref_sort_by_favorite_value))) {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), new AsyncResponse() {
                @Override
                public void processFinish(List<Movie> movies) {
                    mMovies.clear();
                    mMovies.addAll(movies);
                    updateAdapter();
                }
            });
            fetchMoviesTask.execute(sortBy);
        }else {
            if(mCursor!=null&&mCursor.moveToFirst()){
                mMovies.clear();
                do{
                    Movie movie = new Movie(mCursor);
                    mMovies.add(movie);
                }while (mCursor.moveToNext());
                updateAdapter();
            }else {
                getLoaderManager().initLoader(FETCH_FAVORITE_LOADER, null, this);
            }
        }

    }

    private void updateAdapter(){
        mMovieAdapter.clear();
        for(Movie movie:mMovies){
            mMovieAdapter.add(movie.getPoster());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIE_KEY,mMovies);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_sort_by_key))){
            sortBy = sharedPreferences.getString(getString(R.string.pref_sort_by_key),
                    getString(R.string.pref_sort_by_popularity_value));
            getMovies();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getActivity()).
                unregisterOnSharedPreferenceChangeListener(this);
    }

    //for favorite

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieEntry.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor = data;
        if(mCursor!=null&&mCursor.moveToFirst()){
            mMovies.clear();
            do{
                Movie movie = new Movie(data);
                mMovies.add(movie);
            }while (mCursor.moveToNext());
            updateAdapter();
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursor = null;
    }
}
