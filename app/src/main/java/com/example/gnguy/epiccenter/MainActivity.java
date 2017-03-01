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
        ArrayList<String> earthquakes = new ArrayList<String>();

        earthquakes.add("7.4        San Francisco       Jan 16, 2017");
        earthquakes.add("5.4        London              Sep 8, 2016");
        earthquakes.add("9.8        Tokyo               Dec 21, 2012");
        earthquakes.add("4.5        Mexico City         May 14, 2016");
        earthquakes.add("4.7        Moscow              Nov 19, 2016");
        earthquakes.add("5.3        Rio de Janeiro      Nov 19, 2016");
        earthquakes.add("4.9        Paris               Aug 29, 2016");

        // Find a reference to the {@link ListView} in the layout
        ListView listView = (ListView)findViewById(R.id.list_view);

        // Create a new {@link ArrayAdapter} of earthquakes
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, earthquakes);

        // Set the adapter onto the {@link ListVIew} so the list can be populated in the UI
        listView.setAdapter(adapter);


    }
}
