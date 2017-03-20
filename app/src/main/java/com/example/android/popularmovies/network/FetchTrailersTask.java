package com.example.android.popularmovies.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;



import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Trailer;

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
 * Created by harry on 3/16/17.
 */

public class FetchTrailersTask extends AsyncTaskLoader<List<Trailer>> {

    private final static String TAG = FetchTrailersTask.class.getSimpleName();

    private final Context mContext;

    private int mMovieId;

    List<Trailer> mTrailers;

    public FetchTrailersTask(Context context, int movieId){
        super(context);
        mContext = context;
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if(mTrailers!=null){
            deliverResult(mTrailers);
        }else{
            forceLoad();
        }
    }

    @Override
    public List<Trailer> loadInBackground() {


        String trailerJSONString;

        URL trailerQueryUrl;
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        Uri trailerQueryUri = Uri.parse(mContext.getString(R.string.base_url)+mMovieId+mContext.getString(R.string.video_key))
                .buildUpon().appendQueryParameter(mContext.getString(R.string.api_key),mContext.getString(R.string.api_key_value))
                .build();
        try{
            trailerQueryUrl = new URL(trailerQueryUri.toString());
            Log.v(TAG,"URL: " + trailerQueryUrl);
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

        try{
            urlConnection = (HttpURLConnection) trailerQueryUrl.openConnection();
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

            trailerJSONString = stringBuffer.toString();

        }catch (IOException e){
            Log.e(TAG,"Error: " + e);
            return null;
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(bufferedReader!=null){
                try{
                    bufferedReader.close();
                }catch (IOException e){
                    Log.e(TAG,"bufferedReader close error");
                    return null;
                }
            }
        }

        try{
            return extractData(trailerJSONString);
        }catch (JSONException e){
            Log.e(TAG,e.getMessage(),e);
            e.printStackTrace();
            return null;
        }


    }

    private List<Trailer> extractData(String trailerJSONString) throws JSONException{
        JSONObject trailerJSON = new JSONObject(trailerJSONString);
        JSONArray trailerArray = trailerJSON.getJSONArray("results");

        mTrailers = new ArrayList<>();
        for(int i=0; i<trailerArray.length(); i++){
            JSONObject trailer = trailerArray.getJSONObject(i);
            if(trailer.getString("site").contentEquals("YouTube")){
                Trailer trailerModel = new Trailer(trailer);
                mTrailers.add(trailerModel);
            }
        }
        return mTrailers;
    }

    @Override
    public void deliverResult(List<Trailer> data) {
        mTrailers = data;
        super.deliverResult(data);
    }
}
