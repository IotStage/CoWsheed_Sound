package com.example.mainscreenactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.app.PendingIntent;
import java.util.ArrayList;
import java.util.List;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.example.maps.api.*;

//import com.androidcss.asyncexample.MainActivity;
public class MainScreenActivity extends Activity {

	Button btnViewProducts;
	Button btnNewProduct;
	Button btnGeoloc;
	Button Sound;
	String lat, lon, location, heure, s;
	Double latitude, longitude,lati,longi;
	MWMPoint[] points;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		btnViewProducts = (Button) findViewById(R.id.btnViewProducts);
		btnNewProduct = (Button) findViewById(R.id.btnCreateProduct);
		btnGeoloc = (Button) findViewById(R.id.btnGeoloc);
		Sound =(Button) findViewById(R.id.btnSound);
		//findgps();
		//MainScreenActivity.CreateNewProduct task = new MainScreenActivity.CreateNewProduct();
		//task.execute();

		//startService(new Intent(getBaseContext(), GpsService.class));
		btnGeoloc.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				findgps();
				lastknowngps();	
				
			}
		});
		
		btnViewProducts.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(i);
			}
		});
		
		Sound.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),Main.class);
				startActivity(i);
			}
		});
		btnNewProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getApplicationContext(),NewProductActivity.class);
				startActivity(i);
			}
		});
	}
	public void startService(View view) {
		startService(new Intent(getBaseContext(), GpsService.class));
	}

	// Method to stop the service
	public void stopService(View view) {
		stopService(new Intent(getBaseContext(), GpsService.class));
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//stopService(new Intent(getBaseContext(), GpsService.class));
	}
	@Override
	protected void onResume(){
		super.onResume();
		//findgps();
		//MainScreenActivity.CreateNewProduct task = new MainScreenActivity.CreateNewProduct();
		//task.execute();
		//TimeUnit.SECONDS.sleep(1);

	}
		public void findgps(){
	
		LocationManager locationManager = 
                (LocationManager) MainScreenActivity.this.getSystemService(Context.LOCATION_SERVICE);
		
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
	public static String EXTRA_FROM_MWM = "from-maps-with-me";
	public static PendingIntent getPendingIntent(Context context)
	{
		final Intent i = new Intent(context, MainScreenActivity.class);
		i.putExtra(EXTRA_FROM_MWM, true);
		return PendingIntent.getActivity(context, 0, i, 0);
	}
	public void lastknowngps(){
		
		LocationManager locationManager = 
                (LocationManager) MainScreenActivity.this.getSystemService(Context.LOCATION_SERVICE);
		
		 Location lastKnownLocation_byGps = 
		          locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		 if(lastKnownLocation_byGps!= null){
		 
		 lat = Double.toString(lastKnownLocation_byGps.getLatitude());
			 latitude = Double.parseDouble(lat);
		 lon = Double.toString(lastKnownLocation_byGps.getLongitude());
			 longitude = Double.parseDouble(lon);
			 location = "Boitier 1";
			/* lati = 14.749471;
			 longi = -17.501335;
			 MWMPoint point1 = new MWMPoint(latitude,longitude,location);
			 MWMPoint point2 = new MWMPoint(lati,longi,"2e Lieu");
			 List<MWMPoint> Points = new ArrayList<MWMPoint>();
			 Points.add(point1);

			 Points.add(point2);
			 // Convert objects to MMWPoints
			 points = new MWMPoint[Points.size()];*/
			 //for (int i = 0; i < Points.size(); i++)
			 //{
				 // Get lat, lon, and name from object and assign it to new MMWPoint

			 //

		 
		 Toast.makeText(getApplicationContext(), "latitude " + lat +"longtitude " + lon ,Toast.LENGTH_SHORT).show();
			// MapsWithMeApi.showPointsOnMap(this, "Look at my points, my points are amazing!", points);
			 //latitude = 14.7321416;
			 //longitude = -17.4332752;
			MapsWithMeApi.showPointOnMap(this, latitude, longitude, location);
		 }
		 else{
		 //Toast.makeText(getApplicationContext(), "there is no last known location.",Toast.LENGTH_SHORT).show();
			 /*location = "Boitier 1";
			 latitude = 14.7321416;
			 longitude = -17.4332752;
			 lati = 14.754001;
			 longi = -17.498147;
			 MWMPoint point1 = new MWMPoint(latitude,longitude,location);
			 MWMPoint point2 = new MWMPoint(lati,longi,"Boitier 1");
			 List<MWMPoint> Points = new ArrayList<MWMPoint>();
			 Points.add(point1);
			 Points.add(point2);
			 // Convert objects to MMWPoints
			 points = new MWMPoint[Points.size()];
			 //for (int i = 0; i < Points.size(); i++)
			 //{
			 // Get lat, lon, and name from object and assign it to new MMWPoint

			 //

*/
			 Toast.makeText(getApplicationContext(), "No points " ,Toast.LENGTH_SHORT).show();
			// MapsWithMeApi.showPointOnMap(this, latitude, longitude, location);
			 //MapsWithMeApi.showPointsOnMap(this, "Look at my points, my points are amazing!",MainScreenActivity.getPendingIntent(this), points);

		 }
		 
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
}
