package com.example.loginregister;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HP on 2/25/2017.
 */
public class LocationBroadcastService extends IntentService {
    public LocationBroadcastService() {
        super("LocationBroadcastService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        final String token = FirebaseInstanceId.getInstance().getToken();
        while (true) {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            System.out.println("nyobanyoba!");

            if (location != null) {
                System.out.println("LOCATION BROADCAST DONE!");
                double lng = location.getLongitude();
                double lat = location.getLatitude();
                //getLoc
                try {
                    URL url = new URL(dataString + "&lat=" + Double.toString(lat) + "&lng=" + Double.toString(lng));
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    } finally {
                        urlConnection.disconnect();
                    }
                } catch (Exception ex) {
                }
                try {
                    Thread.sleep(15000);
                } catch (Exception ex) {
                }
            }
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
        }
    }

}
