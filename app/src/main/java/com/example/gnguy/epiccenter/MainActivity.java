package com.example.gnguy.epiccenter;

import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    /** Shortcut for log tag parameter */
    private static final String LOG_TAG = MainActivity.class.getName();

    /** Adapter for the earthquakeList */
    private EarthquakeAdapter mAdapter;

    /** ID number for AsyncTaskLoader */
    private static final int LOADER_ID = 1;

    /** Empty textView for http requests that query empty earthquake results */
    private TextView mEmptyView;

    /** Empty textView for when no internet connection is detected */
    private TextView mNoInternetView;

    /** Loading indicator circle that displays progress */
    private ProgressBar mIndicatorCircle;


    /** URL for the earthquake data from the USGS dataset */
    private static final String EARTHQUAKE_REQUEST_URL =
        /** Base USGS http request to be appended with user filters*/
        "https://earthquake.usgs.gov/fdsnws/event/1/query";

        /** Test http url request for no earthquakes fetched (mEmptyView) */
        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2100-01-01";

        /** Test http request with a lot of earthquake data to load (mIndicatorCircle) */
        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=1000&starttime=2017-01-01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(LOG_TAG, "TEST: MainActivity onCreate method called.");
        setContentView(R.layout.activity_main);

        // Initialize a ConnectivityManager to check whether there is an internet connection
        // Create boolean to store connectivity status
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakeList
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());


        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(mAdapter);

        // Find a reference to the mEmptyView
        mEmptyView = (TextView)findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyView);

        /*
        // Spinner for Filter Menu
        Spinner orderSpinner = (Spinner)findViewById(R.id.order_spinner);
        orderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, parent.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */


        // Find a reference to the mIndicatorCircle and set the color to pale blue
        mIndicatorCircle = (ProgressBar)findViewById(R.id.progress_indicator);
        mIndicatorCircle.getIndeterminateDrawable().setColorFilter(Color.parseColor("#80DAEB"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

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
        // if there is an internet connection: Initiate the loader with parameters loaderID,
        // bypass the bundle, and pass in this activity for LoaderCallbacks param.
        // if there is NO internet connection: display the mNoInternetView
        LoaderManager loaderManager = getLoaderManager();
        if (isConnected) {
            loaderManager.initLoader(LOADER_ID, null, this);
            Log.e(LOG_TAG, "TEST: LoaderManager initialized with initLoader");
        } else {
            //loaderManager.initLoader(LOADER_ID, null, this);
            mIndicatorCircle.setVisibility(GONE);
            mEmptyView.setText(R.string.no_internet_connection);
            Log.e(LOG_TAG, "TEST: No internet connection, display mNoInternetView");
        }

    }


    // Override method to inflate the FilterActivity (App bar menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Set up FilterActivity to respond to clicks on the main menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.action_filter){
            Intent filterIntent = new Intent(this, FilterActivity.class);
            startActivity(filterIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Set up minMagnitude value to user input, otherwise default value
        String minMagnitude = sharedPrefs.getString(
                                                getString(R.string.filter_min_mag_key),
                                                getString(R.string.filter_min_mag_default));
        // Set up maxMagnitude value to user input, otherwise default value
        String maxMagnitude = sharedPrefs.getString(
                                                getString(R.string.filter_max_mag_key),
                                                getString(R.string.filter_max_mag_default));

        // Set up startTime value to user input, otherwise default value
        String startTime = sharedPrefs.getString(
                                                getString(R.string.filter_start_time_key),
                                                getString(R.string.filter_start_time_default));

        // Set up orderBy value to user input, otherwise default value
        String orderBy = sharedPrefs.getString(
                                                getString(R.string.filter_order_by_key),
                                                getString(R.string.filter_order_by_default));

        Uri baseUri = Uri.parse(EARTHQUAKE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        //"https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=3&limit=10&starttime=2100-01-01";
        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("orderby",orderBy);
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("limit","20");
        uriBuilder.appendQueryParameter("starttime",startTime);
        uriBuilder.appendQueryParameter("maxmag",maxMagnitude);

        Log.e(LOG_TAG, "TEST: Loader created onCreateLoader.");
        return new EarthquakeAsyncLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> data) {
        mIndicatorCircle.setVisibility(GONE);

        // If no earthquake data was queried, display mEmptyView
        mEmptyView.setText(R.string.empty_view_string);
        Log.e(LOG_TAG, "TEST: Finished loader onLoadFinished");

        // Remove all previous earthquake data
        mAdapter.clear();

        // If there is earthquake data to display
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        mAdapter.clear();
        Log.e(LOG_TAG, "TEST: Loader reset.  onLoaderReset.");
    }



}
