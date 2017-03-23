package com.gnguyen.android.epiccenter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by gnguy on 3/16/2017.
 */

// Create a filter fragment that opens a new activity when clicked
public class FilterActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_activity);
    }


    // set filter fragment to open a menu of filters
    public static class EarthquakePreferenceFragment extends PreferenceFragment
                                                implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.filter_main);

            Preference minMagnitude = findPreference(getString(R.string.filter_min_mag_key));
            bindPreferenceSummaryToValue(minMagnitude);

            Preference maxMagnitude = findPreference(getString(R.string.filter_max_mag_key));
            bindPreferenceSummaryToValue(maxMagnitude);

            Preference startingFrom = findPreference(getString(R.string.filter_start_time_key));
            bindPreferenceSummaryToValue(startingFrom);

            Preference orderBy = findPreference(getString(R.string.filter_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }

        // Set up Preference to detect when a preference is changed and display the current
        // preference setting as the summary
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary(stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference){
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
