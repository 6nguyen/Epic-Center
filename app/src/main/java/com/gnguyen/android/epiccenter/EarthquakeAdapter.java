package com.gnguyen.android.epiccenter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by gnguy on 3/1/2017.
 */

/**
 *  ****************************************************************************************
 *   Layout and text formatting for earthquake data
 *  ****************************************************************************************
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakeList) {
        super(context, 0, earthquakeList);
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

        // Set color of magnitude circle
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitutude());
        magnitudeCircle.setColor(magnitudeColor);



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
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM d, yyyy");
            String date = dateFormatter.format(dateObject);

            // Find the TextView with the ID date
            TextView dateView = (TextView)listItemView.findViewById(R.id.date);
            // Display date of current earthquake into above TextView
            dateView.setText(date);

            // Convert the timeInMilliseconds to a String with time format
            SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm:ss a");
            String time = timeFormatter.format(dateObject);

            // Find the TextView with the ID time
            TextView timeView = (TextView)listItemView.findViewById(R.id.time);
            // Display time of current earthquake into above TextView
            timeView.setText(time);


        return listItemView;

    }

    /**
     * getMagnitudeColor sets the magnitude circle background color
     * @return an int value corresponding with the magnitudeColorResourceId for a given mag
     */
    private int getMagnitudeColor(double mag){
        int magnitudeColorResourceId;
        int roundedMag = (int)Math.round(mag);

        switch(roundedMag){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        // ContextCompat.getColor converts the color resource ID into an actual integer color value
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
