package com.example.gnguy.epiccenter;

/**
 * Created by gnguy on 3/1/2017.
 */

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;

    // Constructor
    public Earthquake(double mag, String location, long time){
        mMagnitude = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
    }

    // Accessor Methods
    public double getMagnitutude(){return mMagnitude; }
    public String getLocation(){return mLocation;}
    public long getmTimeInMilliseconds(){return mTimeInMilliseconds;}

}
