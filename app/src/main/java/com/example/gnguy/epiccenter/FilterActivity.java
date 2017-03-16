package com.example.gnguy.epiccenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gnguy on 3/16/2017.
 */

// Create a filter fragment that opens a new activity when clicked
public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
    }

    // set filter fragment to open a menu of filters
    public static class EarthquakePreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.filter_main);
        }
    }
}
