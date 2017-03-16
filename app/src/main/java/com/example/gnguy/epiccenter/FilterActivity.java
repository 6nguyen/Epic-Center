package com.example.gnguy.epiccenter;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by gnguy on 3/16/2017.
 */

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
    }

    public static class EarthquakePreferenceFragment extends PreferenceFragment {

    }
}
