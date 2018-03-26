package com.example.mainscreenactivity;
import android.content.Context;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.json.JSONException;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
//import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Robert on 01/06/2017.
 */

public class GpsService extends Service{
    //@Nullable
    String lat, lon, location, heure, s;
    Double latitude, longitude;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://10.130.1.203:8888/lora_register/script.php";
    private static final String TAG_SUCCESS = "success";
    private ProgressDialog pDialog;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        int i=0;
        findgps();
        GpsService.CreateNewProduct task = new GpsService.CreateNewProduct();
        task.execute();
        //Toast.makeText(this, "Service demareee", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service arreteee", Toast.LENGTH_LONG).show();
    }
    public void findgps(){

        LocationManager locationManager =
                (LocationManager) GpsService.this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                lat = Double.toString(location.getLatitude());
                lon = Double.toString(location.getLongitude());

                //Toast.makeText(getApplicationContext(), "latitude " + lat +"longtitude " + lon ,Toast.LENGTH_SHORT).show();

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);

    }

    class CreateNewProduct extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //pDialog = new ProgressDialog(GpsService.this);
            //pDialog.setMessage("Envoi gps..");
            /*pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub

            LocationManager locationManager =
                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Location lastKnownLocation_byGps =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation_byGps != null) {
                //Location location = null;
                Date d = new Date();
                SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                s = f.format(d);
                heure = s.substring(9);

                //lat = "10";
                //lon = "20";
                lat = Double.toString(lastKnownLocation_byGps.getLatitude());
                lon = Double.toString(lastKnownLocation_byGps.getLongitude());

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("lat", lat));
                params.add(new BasicNameValuePair("long", lon));
                params.add(new BasicNameValuePair("rssi", heure));


                JSONObject json = jsonParser.makeHttpRequest(url_create_product, "POST", params);


                /*Log.d("Create Response", json.toString());
                Toast.makeText(getApplicationContext(), "heure" + heure + "latitude " + lat + "longtitude " + lon, Toast.LENGTH_SHORT).show();
*/
            }
            else{
                Toast.makeText(getApplicationContext(), "there is no last known location.", Toast.LENGTH_SHORT).show();

            }

            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            // TODO Auto-generated method stub
           // pDialog.dismiss();
            Toast.makeText(getApplicationContext(), "heure" + heure + "latitude " + lat + "longtitude " + lon, Toast.LENGTH_SHORT).show();
        }

    }
}
