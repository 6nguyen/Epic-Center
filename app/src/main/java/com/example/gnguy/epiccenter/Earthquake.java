package com.example.gnguy.epiccenter;

/**
 * Created by gnguy on 3/1/2017.
 */

/**
 *  ****************************************************************************************
 *   Earthquake Java class allows for simple extraction and manipulation of desired fields
 *  ****************************************************************************************
 */

public class Earthquake {

    private double mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;
    private String mUrl;

    // Constructor
    public Earthquake(double mag, String location, long time, String url){
        mMagnitude = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
        mUrl = url;
    }

    // Accessor Methods
    public double getMagnitutude(){return mMagnitude; }
    public String getLocation(){return mLocation;}
    public long getmTimeInMilliseconds(){return mTimeInMilliseconds;}
    public String getUrl(){return mUrl;}

}
