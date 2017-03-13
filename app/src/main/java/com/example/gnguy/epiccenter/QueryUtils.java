package com.example.gnguy.epiccenter;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {


    /** Tag for log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }


    /**
     *  Query the USGS data set and create an List<Earthquake> to represent
     *  a list of earthquakeList.
     *  @return List<Earthquake>
     */
    public static List<Earthquake> fetchEarthquakeData(String requestUrl){
        // create a URL object from given url
        Log.e(LOG_TAG, "TEST: fetching request url into AsyncTaskLoader.");
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create List<Earthquake>
        List<Earthquake> earthquakeList = extractEarthquakeFeatures(jsonResponse);

        return earthquakeList;
    }



    /**
     * @return new URL object from the given url string
     */
    private static URL createUrl(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue){
            Log.e(LOG_TAG, "Error with building URL from given url String.", mue);
        }
        return url;
    }



    /**
     * Make an HTTP request to the given URL and return a String as the response.
     *  Attempt to connect to the given URL and establish the InputStream if
     *  successful, or log an error.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if the URL is null, return early from method
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // if the request was successful (response code: 200), read the input and parse response
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        // Regardless of whether the request was successful, disconnect the connection
        // and close the inputStream when done
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }



    /**
     *  Convert the InputStream into a String which contains the entire JSON response
     *  from the server
     */
    private static String readFromStream(InputStream inputStream)throws IOException {
        /** Create a StringBuilder object that creates a string by appending smaller strings
         *  piece by piece
         */
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while(line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }



    /**
     * extractEarthquakeFeatures
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakeFeatures(String earthqaukeJsonResponse) {
        // if the requestUrl is empty or null, return null and exit early
        if (TextUtils.isEmpty(earthqaukeJsonResponse)){
            return null;
        }
        // Create an empty List that we can start adding Earthquake objects to
        List<Earthquake> earthquakeList = new ArrayList<>();

        // Parse the JSONrequestUrl to extract all necessary features
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
             // Convert JSONrequestUrl String into a JSONObject
            // Extract “features” JSONArray
            JSONObject root = new JSONObject(earthqaukeJsonResponse);
            JSONArray featuresArray = root.getJSONArray("features");

            /** Loop through each feature in the array
             * Get earthquake JSONObject at position i
             * Get “properties” JSONObject
             * Extract “mag” for magnitude
             * Extract "place" for location
             * Extract "time" for date
             * Extract "url" for clickable url
             */
            for (int i=0; i<featuresArray.length(); i++){

                JSONObject currentEarthquake = featuresArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                // Create a new Earthquake object from magnitude, location, time, and url
                // Add Earthquake object to List<earthquakeList>
                earthquakeList.add(new Earthquake(mag, place, time, url));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakeList
        return earthquakeList;
    }





}