package com.example.android.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harry on 3/16/17.
 */

public class Trailer {
    private String mId;
    private String mKey;
    private String mName;
    private String mSite;
    private String mType;

    public Trailer(){

    }

    public Trailer(JSONObject trailer) throws JSONException{
        mId = trailer.getString("id");
        mKey = trailer.getString("key");
        mName = trailer.getString("name");
        mSite = trailer.getString("site");
        mType = trailer.getString("type");
    }

    public String getId(){ return mId;}
    public String getKey(){ return mKey;}
    public String getName() { return mName;};
    public String getSite() { return mSite;};
    public String getType() { return mType;};

}
