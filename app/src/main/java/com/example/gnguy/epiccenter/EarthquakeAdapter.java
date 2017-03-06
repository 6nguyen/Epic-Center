package com.example.gnguy.epiccenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gnguy on 3/1/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list_view, parent, false);
        }

        // Find the earthquake at the given position
        Earthquake currentEarthquake = getItem(position);


        // Format and display magnitutde to one decimal place
        double magnitude = currentEarthquake.getMagnitutude();
        DecimalFormat magFormatter = new DecimalFormat("0.0");
        String formattedMag = magFormatter.format(magnitude);

        // Find the TextView with the ID magnitude
        TextView magnitudeView = (TextView)listItemView.findViewById(R.id.magnitude);
        // Display magnitude of current earthquake into above TextView
        magnitudeView.setText(formattedMag);


        // Convert the String mLocation into two Strings, a primary location and location offset
        String location = new String(currentEarthquake.getLocation());
            String locationOffset = null;
            String primaryLocation = null;
            if (location.contains(" of ")){
                int endIndex = location.indexOf(" of") + 4;
                locationOffset = location.substring(0,endIndex);
                primaryLocation = location.substring(endIndex, location.length());
            } else {
                locationOffset = "Near the ";
                primaryLocation = location;
            }

            // Location offset
            TextView locationOffsetView = (TextView)listItemView.findViewById(R.id.location_offset);
            locationOffsetView.setText(locationOffset);

            // Primary location
            TextView primaryView = (TextView)listItemView.findViewById(R.id.primary_location);
            primaryView.setText(primaryLocation);


        // Convert the long mTimeInMilliseconds to a String with date format
        Date dateObject = new Date(currentEarthquake.getmTimeInMilliseconds());
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
            String date = dateFormatter.format(dateObject);

            // Find the TextView with the ID date
            TextView dateView = (TextView)listItemView.findViewById(R.id.date);
            // Display date of current earthquake into above TextView
            dateView.setText(date);

            // Convert the timeInMilliseconds to a String with time format
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm a");
            String time = timeFormatter.format(dateObject);

            // Find the TextView with the ID time
            TextView timeView = (TextView)listItemView.findViewById(R.id.time);
            // Display time of current earthquake into above TextView
            timeView.setText(time);


        return listItemView;

    }

}
