package com.example.gnguy.epiccenter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by gnguy on 3/9/2017.
 */

public class EarthquakeAsyncLoader extends AsyncTaskLoader<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeAsyncLoader.class.getName();
    private String mUrl;

    public EarthquakeAsyncLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.e(LOG_TAG, "TEST: Loading AsyncTaskLoader onStartLoading forceLoad();");
    }

    @Override
    public List<Earthquake> loadInBackground( ) {
        Log.e(LOG_TAG, "TEST: loadInBackground is called.");
        if (mUrl == null) {
            return null;
        }

        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }

}
