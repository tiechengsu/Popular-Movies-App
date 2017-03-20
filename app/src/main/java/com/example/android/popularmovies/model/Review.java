package com.example.android.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harry on 3/17/17.
 */

public class Review {
    private String mId;
    private String mAuthor;
    private String mContent;

    public Review(){}

    public Review(JSONObject review) throws JSONException{
        mId = review.getString("id");
        mAuthor = review.getString("author");
        mContent = review.getString("content");
    }

    public String getId(){ return mId; }
    public String getAuthor() { return mAuthor; }
    public String getContent() { return mContent; }
}
