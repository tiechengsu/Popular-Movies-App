package com.example.android.popularmovies;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by harry on 3/15/17.
 */

/**
 * in order to separate the asynctask from the activity, we implement a callback
 * refer to http://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a
 */
public interface AsyncResponse {
    public void processFinish(List<Movie> movies);
}
