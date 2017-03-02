package com.example.gnguy.epiccenter;

/**
 * Created by gnguy on 3/1/2017.
 */

public class Earthquake {

    private String mMagnitude;
    private String mLocation;
    private String mDate;

    // Constructor
    public Earthquake(String mag, String location, String date){
        mMagnitude = mag;
        mLocation = location;
        mDate = date;
    }

    // Accessor Methods
    public String getMagnitutude(){return mMagnitude; }
    public String getLocation(){return mLocation;}
    public String getDate(){return mDate;}



}
