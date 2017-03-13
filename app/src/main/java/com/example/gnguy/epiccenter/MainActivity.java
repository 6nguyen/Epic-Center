package com.example.gnguy.epiccenter;

import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    /** Short cut for log tag parameter */
    private static final String LOG_TAG = MainActivity.class.getName();

    /** Adapter for the earthquakeList */
    private EarthquakeAdapter mAdapter;

    /** ID number for AsyncTaskLoader */
    private static final int LOADER_ID = 1;

    /** Empty textView for http requests that query empty earthquake results */
    private TextView mEmptyView;

    /** Loading indicator circle that displays progress */
    private ProgressBar mIndicatorCircle;

    /** URL for the earthquake data from the USGS dataset */
    private static final String EARTHQUAKE_REQUEST_URL =
        /** USGS http request for a max of 10 earthquakes with min mag 3, starting from Jan 1, 2017 */
        "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2017-01-01";
        /** Test http url request for no earthquakes fetched (mEmptyView) */
        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2100-01-01";
        /** Test http request with a lot of earthquake data to load (mIndicatorCircle) */
        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=100&starttime=2017-01-01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "TEST: MainActivity onCreate method called.");
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakeList
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());


        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(mAdapter);

        // Find a reference to the mEmptyView
        mEmptyView = (TextView)findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyView);

        // Find a reference to the mIndicatorCircle
        mIndicatorCircle = (ProgressBar)findViewById(R.id.progress_indicator);

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


        // Get a reference to the LoaderManager, to interact with loaders
        // Initiate the loader with parameters loaderID, bypass the bundle, and pass in this
        // activity for LoaderCallbacks param.
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
        Log.e(LOG_TAG, "TEST: LoaderManager initialized with initLoader");

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
            mIndicatorCircle.setVisibility(GONE);
            mAdapter.addAll(data);
        }
        // If no earthquake data was queried
        mIndicatorCircle.setVisibility(GONE);
        mEmptyView.setText(R.string.empty_view_string);
        Log.e(LOG_TAG, "TEST: Finished loader onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "TEST: Loader reset.  onLoaderReset.");
    }



}
