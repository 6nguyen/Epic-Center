package com.example.gnguy.epiccenter;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.AsyncTask;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** Adapter for the earthquakeList */
    private EarthquakeAdapter mAdapter;

    /** ID number for AsyncTaskLoader */
    private static final int LOADER_ID = 1;

    /** URL for the earthquake data from the USGS dataset */
    private static final String EARTHQUAKE_REQUEST_URL =
        // USGS http request for a max of 10 earthquakes with min mag 3, starting from Jan 1, 2017
        "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2017-01-01";
        // Test http url request for no earthquakes fetched, ie earthquakes starting from Jan 1, 2100
        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2100-01-01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);
        /**if (listView == null){
            listView.setEmptyView(findViewById(R.id.empty_view));
        } */

        // Create a new {@link ArrayAdapter} of earthquakeList
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(mAdapter);

        // Get a reference to the LoaderManager, to interact with loaders
        // Initiate the loader with parameters loaderID, bypass the bundle, and pass in this
        // activity for LoaderCallbacks param.
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        Log.e(LOG_TAG, "TEST: LoaderManager initialized with initLoader");

        // Set listView items to open earthquake url when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the earthquake at the given position
                Earthquake currentEarthquake = mAdapter.getItem(position);

                // Parse mUrl to a readable Uri
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Set a new intent to open the web page using any browser
                // give option to user which browser will open, or default browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Open browser activity
                startActivity(browserIntent);
            }
        });

        // Create an {@link AsyncTask} to perform the HTTP request to the given URL
        // on a background thread. When the result is received on the main UI thread,
        // then update the UI.
        //EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        //task.execute(EARTHQUAKE_REQUEST_URL);
        EarthquakeAsyncLoader task = new EarthquakeAsyncLoader(MainActivity.this, EARTHQUAKE_REQUEST_URL);
        task.onStartLoading();

    }

    /**
     *  Update the UI with the given earthquake list information
     */
    private void updateUI(List<Earthquake> earthquakeData){
        mAdapter.clear();
        if (earthquakeData != null && !earthquakeData.isEmpty()){
            mAdapter.addAll(earthquakeData);
        }
    }


    /**
     * Three abstract callback methods necessary for AsyncTaskLoader     *
     */
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
        Log.e(LOG_TAG, "TEST: Loader created onCreateLoader.");
        return new EarthquakeAsyncLoader(this, EARTHQUAKE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        mAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            Log.e(LOG_TAG, "TEST: Finished loader onLoadFinished");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "TEST: Loader reset.  onLoaderReset.");
    }


    /**
     *  {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakeList from the response.
     */ /**
    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }

            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        /**
         * This method is invoked on the main UI thread after the background work has been
         * completed.
         */ /**
        @Override
        protected void onPostExecute(List<Earthquake> earthquakeData) {
            mAdapter.clear();
            if (earthquakeData != null && !earthquakeData.isEmpty()) {
                mAdapter.addAll(earthquakeData);
            }

           /*
            if (result == null) {
                return;
            }
            updateUI(result);
        }
        */ /*
        }
    }  */



}
