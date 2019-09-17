package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class LocationActivity extends AppCompatActivity {
    private static final String DARK_SKY_API_KEY = "abe7f3a9a6f05474acd945b2577bd8c3";
    private static final String LOCATION_LATITUDE = "location_latitude";
    private static final String LOCATION_LONGTITUDE = "location_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        Double latitude = getIntent().getDoubleExtra(LOCATION_LATITUDE, 0);
        Double longitude = getIntent().getDoubleExtra(LOCATION_LONGTITUDE, 0);

        String weatherUrl = "https://api.darksky.net/forecast/" + DARK_SKY_API_KEY + "/" +
                latitude + "," + longitude;

        System.out.println(weatherUrl);

        displayMap(latitude, longitude);
        getWeatherInformation(weatherUrl);
    }

    public void displayMap(Double latitude, Double longitude) {
        // TODO:
    }

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
