package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.widget.TextView;

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
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.net.URL;
//import com.android.volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void locationOnClick (View view) {


        // Get the text view.
        EditText showLocationEditText = (EditText) findViewById(R.id.locationInputText);

        // Get the value of the text view.
        String locationString = showLocationEditText.getText().toString();
        locationString = locationString.replace(' ','+');
        String url= "https://maps.googleapis.com/maps/api/geocode/json?address="+locationString+"&key=AIzaSyCVrdaCwAUVATrLXC9xUZaOqIir1v1B39o";

        RequestQueue requestQueue;

// Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap

// Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        requestQueue = new RequestQueue(cache, network);

// Start the queue
        requestQueue.start();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null, new Response.Listener<JSONObject>(){
            @Override

            public void onResponse(JSONObject response){
                //try {
                    System.out.println(response);
                    //JSONObject results = response.getJSONObject("results");
                    //System.out.println(results);
                    //JSONObject results = (JSONObject) response.get("results");
                    //JSONObject geometry = (JSONObject) results.get("geometry");
                    //JSONArray location = (JSONArray) geometry.get("location");

                    //JSONObject lat = location.getJSONObject(0);
                    //JSONObject lng = location.getJSONObject(1);
                    //JSONObject lat = (JSONObject) location.get("lat");
                    //JSONObject lng = (JSONObject) location.get("lng");

                    //System.out.println(lat + " - " + lng);
                //} catch (JSONException e) {
                //    System.out.println("asdf");
                //}
            }
        },
                new Response.ErrorListener(){
            @Override
                    public void onErrorResponse(VolleyError error){
                        System.out.println(error);
            }
                });
        requestQueue.add(jsonObjectRequest);



    }
}
