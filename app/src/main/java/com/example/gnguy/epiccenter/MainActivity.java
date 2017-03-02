package com.example.gnguy.epiccenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fake list of earthquake locations
        ArrayList<Earthquake> earthquakes = new ArrayList<Earthquake>();

        earthquakes.add(new Earthquake("7.4","San Francisco","Jan 16, 2017"));
        earthquakes.add(new Earthquake("5.4","Tustin","Sep 8, 2016"));
        earthquakes.add(new Earthquake("9.8","Los Angeles","Dec 21, 2012"));
        earthquakes.add(new Earthquake("9.9", "Tustin", "May 14, 1992"));
        earthquakes.add(new Earthquake("4.5", "San Jose", "Mar 24, 2015"));
        earthquakes.add(new Earthquake("5.1", "Sacramento", "Nov 19, 2016"));
        earthquakes.add(new Earthquake("4.6", "Liverpool", "Dec 24, 2014"));

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakes
        EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(adapter);


    }
}
