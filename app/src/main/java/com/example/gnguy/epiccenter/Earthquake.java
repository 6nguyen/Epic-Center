package com.example.gnguy.epiccenter;

/**
 * Created by gnguy on 3/1/2017.
 */

public class Earthquake {

    private String mMagnitude;
    private String mLocation;
    private long mTimeInMilliseconds;

    // Constructor
    public Earthquake(String mag, String location, long time){
        mMagnitude = mag;
        mLocation = location;
        mTimeInMilliseconds = time;
    }

    // Accessor Methods
    public String getMagnitutude(){return mMagnitude; }
    public String getLocation(){return mLocation;}
    public long getmTimeInMilliseconds(){return mTimeInMilliseconds;}

}
