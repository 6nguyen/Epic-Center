package com.example.gnguy.epiccenter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Extraxt list of earthquake details
        ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes();

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakes
        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(adapter);

        // Set listView items to open earthquake url when clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the earthquake at the given position
                Earthquake currentEarthquake = adapter.getItem(position);

                // Parse mUrl to a readable Uri
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Set a new intent to open the web page using any browser
                // give option to user which browser will open, or default browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Open browser activity
                startActivity(browserIntent);
            }
        });

        // TODO Create an {@link AsyncTask} to perform the HTTP request to the given URL
        // on a background thread. When the result is received on the main UI thread,
        // then update the UI.
        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(EARTHQUAKE_REQUEST_URL);

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
     *  {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakeList from the response.
     */
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
         */
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
        */
        }


    }


}
