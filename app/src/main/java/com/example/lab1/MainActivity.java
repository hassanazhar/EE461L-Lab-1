package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String GOOGLE_MAPS_API_KEY = " AIzaSyCVrdaCwAUVATrLXC9xUZaOqIir1v1B39o";
    private static final String LOCATION_LATITUDE = "location_latitude";
    private static final String LOCATION_LONGTITUDE = "location_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Led by Hassan
    public void locationOnClick (View view) {
        // Get the text view.
        EditText showLocationEditText = (EditText) findViewById(R.id.locationInputText);

        // Get the value of the text view.
        String locationString = showLocationEditText.getText().toString();
        locationString = locationString.replace(' ','+');
        String locationUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                locationString + "&key=" + GOOGLE_MAPS_API_KEY;

        System.out.println(locationUrl);
        geocodeLocation(locationUrl);
    }
    // Led by Ryan
    public void startLocationActivity(Double latitude, Double longtitude) {
        Intent locationIntent = new Intent(this, LocationActivity.class);

        locationIntent.putExtra(LOCATION_LATITUDE, latitude);
        locationIntent.putExtra(LOCATION_LONGTITUDE, longtitude);

        startActivity(locationIntent);
    }

    /*
     The structure for this method is outlined in https://developer.android.com/training/volley.
     */
    // Led by Hassan
    public void geocodeLocation(String url) {
        RequestQueue requestQueue;

        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

        // Start the queue
        requestQueue.start();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response: " + response);

                try {
                    JSONArray results = response.getJSONArray("results");
                    System.out.println("Results: " + results);

                    JSONObject geometry = results.getJSONObject(0).getJSONObject("geometry");
                    System.out.println("Geometry: " + geometry);

                    JSONObject location = geometry.getJSONObject("location");
                    System.out.println("Location: " + location);

                    Double lat = location.getDouble("lat");
                    Double lng = location.getDouble("lng");

                    System.out.println("Lat: " + lat);
                    System.out.println("Lng: " + lng);
                    startLocationActivity(lat, lng);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println(error);
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
