package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String DARK_SKY_API_KEY = "abe7f3a9a6f05474acd945b2577bd8c3";
    private static final String LOCATION_LATITUDE = "location_latitude";
    private static final String LOCATION_LONGTITUDE = "location_longitude";

    private GoogleMap mMap;
    private static Double latitude, longitude;

    @Override
    // Led by Hassan
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        latitude = getIntent().getDoubleExtra(LOCATION_LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(LOCATION_LONGTITUDE, 0);

        String weatherUrl = "https://api.darksky.net/forecast/" + DARK_SKY_API_KEY + "/" +
                latitude + "," + longitude;

        System.out.println(weatherUrl);

        displayMap(latitude, longitude);
        getWeatherInformation(weatherUrl);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    // Led by Ryan
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    // Led by Ryan
    public void displayMap(Double latitude, Double longitude) {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Led by Hassan
    public void displayWeatherInformation(Double temperature, Double humidity, Double windSpeed, Double precipitation) {
        TextView temperatureView = (TextView) findViewById(R.id.temperature_text);
        String temperatureText = String.format(Locale.US, "Temperature: %.2f\u2109", temperature);
        temperatureView.setText(temperatureText);

        TextView humidityView = (TextView) findViewById(R.id.humidity_text);
        String humidityText = String.format(Locale.US, "Humidity: %.0f%%", humidity * 100);
        humidityView.setText(humidityText);

        TextView windSpeedView = (TextView) findViewById(R.id.wind_speed_text);
        String windSpeedText = String.format(Locale.US, "Wind Speed: %.2f mph", windSpeed);
        windSpeedView.setText(windSpeedText);

        TextView precipitationView = (TextView) findViewById(R.id.precipitation_text);
        String precipitationText = String.format(Locale.US, "Precipitation: %.0f%%", precipitation * 100);
        precipitationView.setText(precipitationText);
    }

    /*
     The structure for this method is outlined in https://developer.android.com/training/volley.
     */
    // Led by Hassan
    public void getWeatherInformation(String url) {
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
                    JSONObject currently = response.getJSONObject("currently");
                    System.out.println("Currently:" + currently);

                    Double temperature = currently.getDouble("temperature");
                    Double humidity = currently.getDouble("humidity");
                    Double windSpeed = currently.getDouble("windSpeed");
                    Double precipitation = currently.getDouble("precipProbability");

                    System.out.println("Temperature: " + temperature);
                    System.out.println("Humidity: " + humidity);
                    System.out.println("Wind speed: " + windSpeed);
                    System.out.println("Precipitation: " + precipitation);

                    displayWeatherInformation(temperature, humidity, windSpeed, precipitation);

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
