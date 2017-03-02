package com.example.gnguy.epiccenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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

        // Find the TextView with the ID magnitude
        TextView magnitudeView = (TextView)listItemView.findViewById(R.id.magnitude);
        // Display magnitude of current earthquake into above TextView
        magnitudeView.setText(currentEarthquake.getMagnitutude());

        // Find the TextView with the ID location
        TextView locationView = (TextView)listItemView.findViewById(R.id.location);
        // Display location of current earthquake into above TextView
        locationView.setText(currentEarthquake.getLocation());

        // Find the TextView with the ID date
        TextView dateView = (TextView)listItemView.findViewById(R.id.date);
        // Display date of current earthquake into above TextView
        dateView.setText(currentEarthquake.getDate());

        return listItemView;

    }

}
