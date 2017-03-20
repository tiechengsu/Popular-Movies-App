package com.example.android.popularmovies.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.android.popularmovies.AsyncResponse;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by harry on 3/15/17.
 */

public class FetchMoviesTask extends AsyncTask<String,Void,List<Movie>>{

    private static final String TAG = FetchMoviesTask.class.getSimpleName();

    private static final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";

    private static final String SORT_BY = "sort_by";

    private static final String MOVIE_POSTER_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String MOVIE_POSTER_SIZE = "w185";

    private final Context mContext;

    public AsyncResponse mDelegate = null;

    private ProgressBar mLoadingIndicator;



    public FetchMoviesTask(Context context, AsyncResponse delegate){
        mDelegate = delegate;
        mContext = context;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        //if(android.os.Debug.isDebuggerConnected())
            //android.os.Debug.waitForDebugger();

        if(params.length==0) {
            return null;
        }

        String sortBy = params[0];
        URL movieQueryUrl;
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String movieJSONString = null;

        Uri movieQueryUri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(SORT_BY,sortBy)
                .appendQueryParameter(mContext.getString(R.string.api_key),mContext.getString(R.string.api_key_value))
                .build();

        try{
            movieQueryUrl = new URL(movieQueryUri.toString());
            Log.v(TAG,"URL: " + movieQueryUrl);
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

        try{
            urlConnection = (HttpURLConnection) movieQueryUrl.openConnection();
            InputStream in = urlConnection.getInputStream();

            StringBuffer stringBuffer = new StringBuffer();

            bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuffer.append(line+"\n");
            }

            if(stringBuffer.length() == 0){
                return null;
            }

            movieJSONString = stringBuffer.toString();

        }catch (IOException e){
            Log.e(TAG,"Error: " + e);
            return null;
        }finally {
            if(urlConnection!=null) {
                urlConnection.disconnect();
            }
            if(bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(TAG, "bufferedReader close error");
                }
            }
        }

        try{
            return extractData(movieJSONString);
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if(movies!=null){
            mDelegate.processFinish(movies);
        }
    }

    /**
     * Get result from JSON file
     * @param movieJSONString
     * @return
     * @throws JSONException
     */
    private List<Movie> extractData(String movieJSONString) throws JSONException{
        final String ARRAY_OF_MOVIES = "results";

        final String POSTER_PATH = "poster_path";
        final String BACKDROP_PATH = "backdrop_path";

        JSONObject movieJSON = new JSONObject(movieJSONString);
        JSONArray movieArray = movieJSON.getJSONArray(ARRAY_OF_MOVIES);
        List<Movie> movies = new ArrayList<>();

        for(int i=0; i<movieArray.length(); i++){
            JSONObject movie = movieArray.getJSONObject(i);
            int movieId = movie.getInt(MovieContract.MovieEntry.COLUMN_ID);
            String title = movie.getString(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
            String poster = MOVIE_POSTER_BASE_URL+MOVIE_POSTER_SIZE+movie.getString(POSTER_PATH);
            String backdrop = mContext.getString(R.string.movie_poster_url)+
                    mContext.getString(R.string.MOVIE_BACKDROP_SIZE)+movie.getString(BACKDROP_PATH);
            String overview = movie.getString(MovieContract.MovieEntry.COLUMN_OVERVIEW);
            String voteAverage = movie.getString(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE);
            String releaseDate = movie.getString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);


            movies.add(new Movie(movieId,title,poster,backdrop,overview,voteAverage,releaseDate));
        }

        return movies;
    }

}
