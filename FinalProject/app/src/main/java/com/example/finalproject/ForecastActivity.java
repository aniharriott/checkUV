package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class ForecastActivity extends AbstractActivity {

    protected double latitude;
    protected double longitude;
    protected double currUV;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    /** A bundle of key/value pairs used to communicate information between activities */
    private Bundle mUVInfo;

    /**
     * Callback that is called when the activity is first created.
     * @param savedInstanceState contains the activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_page);

        mContext = this;
        getAllPermissions();
        setNavigationListeners();
        setUV();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);
    }

    private void setUV() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setUVText(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                // TODO make this do something
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
        }
        locationManager.requestLocationUpdates("gps",5000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        setUVText(location);
    }

    private void setUVText(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        makeUVRequest();
        TextView currentUV = findViewById(R.id.currentUV);
        String newUV = "current UV: " + currUV;
        currentUV.setText(newUV);
        //mUVInfo.putDouble("uvi", currUV);
        if (currUV <= 2) {
            currentUV.setTextColor(getColor(R.color.green));
        } else if (currUV <= 5) {
            currentUV.setTextColor(getColor(R.color.yellow));
        } else if (currUV <= 7) {
            currentUV.setTextColor(getColor(R.color.orange));
        } else if (currUV <= 10) {
            currentUV.setTextColor(getColor(R.color.red));
        } else {
            currentUV.setTextColor(getColor(R.color.pink));
        }
    }

    private void makeUVRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String URL = "https://api.openweathermap.org/data/2.5/onecall?lat="+latitude+"&lon="+longitude+"&exclude=minutely,daily,alerts&appid=0d25c352f160532abf17bd4f8206749e";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("rest response", response.toString());
                        double uvi = 0;
                        try {
                            uvi = response.getJSONObject("current").getDouble("uvi");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        currUV = uvi;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("rest response", error.toString());
                    }
                }
        );
        requestQueue.add(request);
    }

    /**
     *  Ensure that we have all of the permissions set up for this application. We need
     *  to be able to read contacts, access fine location, and the internet. This will cause
     *  the app to request these permissions the first time through the app.
     */
    private void getAllPermissions() {
        if (!isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            String[] permissions = new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET};
            ActivityCompat.requestPermissions(ForecastActivity.this, permissions, 353);
        }  // Permission granted
    }

}
