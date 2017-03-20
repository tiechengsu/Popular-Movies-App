package com.example.android.popularmovies.network;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;

import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.model.Review;

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
 * Created by harry on 3/17/17.
 */


public class FetchReviewsTask extends AsyncTaskLoader<List<Review>> {

    private final static String TAG = FetchReviewsTask.class.getSimpleName();

    private final Context mContext;

    private int mMovieId;

    List<Review> mReviews;

    public FetchReviewsTask(Context context, int movieId){
        super(context);
        mContext = context;
        mMovieId = movieId;
    }

    @Override
    protected void onStartLoading() {
        if(mReviews!=null){
            deliverResult(mReviews);
        }else{
            forceLoad();
        }
    }

    @Override
    public List<Review> loadInBackground() {

        if(android.os.Debug.isDebuggerConnected())
            android.os.Debug.waitForDebugger();



        String ReviewJSONString;

        URL ReviewQueryUrl;
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        Uri ReviewQueryUri = Uri.parse(mContext.getString(R.string.base_url)+mMovieId+mContext.getString(R.string.review_key))
                .buildUpon().appendQueryParameter(mContext.getString(R.string.api_key),mContext.getString(R.string.api_key_value))
                .build();
        try{
            ReviewQueryUrl = new URL(ReviewQueryUri.toString());
            Log.v(TAG,"URL: " + ReviewQueryUrl);
        }catch (MalformedURLException e){
            e.printStackTrace();
            return null;
        }

        try{
            urlConnection = (HttpURLConnection) ReviewQueryUrl.openConnection();
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

            ReviewJSONString = stringBuffer.toString();

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
            return extractData(ReviewJSONString);
        }catch (JSONException e){
            Log.e(TAG,e.getMessage(),e);
            e.printStackTrace();
            return null;
        }


    }

    private List<Review> extractData(String ReviewJSONString) throws JSONException{
        JSONObject ReviewJSON = new JSONObject(ReviewJSONString);
        JSONArray ReviewArray = ReviewJSON.getJSONArray("results");

        mReviews = new ArrayList<>();
        for(int i=0; i<ReviewArray.length(); i++){
            JSONObject review = ReviewArray.getJSONObject(i);
            mReviews.add(new Review(review));

        }
        return mReviews;
    }

    @Override
    public void deliverResult(List<Review> data) {
        mReviews = data;
        super.deliverResult(data);
    }
}