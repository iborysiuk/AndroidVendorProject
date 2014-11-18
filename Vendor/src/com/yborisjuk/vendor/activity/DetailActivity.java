package com.yborisjuk.vendor.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.libs.DBVendorHelper;
import com.yborisjuk.vendor.libs.mySharedPreferences;
import com.yborisjuk.vendor.libs.json.DirectionsJSONParser;
import com.yborisjuk.vendor.libs.json.LoadUrlForImage;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask.Status;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity implements LocationListener {

	private TextView inputName, inputEmail, inputPhone, inputAdress,
			inputWorkTime;
	private Button btn_save;
	public ImageView imgLogo;
	private mySharedPreferences sharedPreferences;
	private String dvUID, dvName, dvEmail, dvPhone, dvCountry, dvCity,
			dvAddress, dvPostalCode, dvImgLink, dvWorkTime;
	private DBVendorHelper db;
	private GoogleMap googleMap;
	private ArrayList<LatLng> mMarkerPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LoadUrlForImage image = new LoadUrlForImage(getApplicationContext());

		do {
			new LoadUrlForImage(DetailActivity.this).execute();
			Log.i("Information", "Info: Image downloaded");
		} while (image.getStatus() == Status.FINISHED);

		setContentView(R.layout.detail_screen);
		inputName = (TextView) findViewById(R.id.detail_src_name);
		inputEmail = (TextView) findViewById(R.id.detail_scr_email);
		inputPhone = (TextView) findViewById(R.id.detail_src_tel);
		inputAdress = (TextView) findViewById(R.id.detail_scr_address);
		inputWorkTime = (TextView) findViewById(R.id.detail_scr_workTime);
		imgLogo = (ImageView) findViewById(R.id.detail_scr_img);

		btn_save = (Button) findViewById(R.id.detail_src_btn_save);

		dvUID = getIntent().getStringExtra("uid");
		dvName = getIntent().getStringExtra("name");
		dvEmail = getIntent().getStringExtra("email");
		dvPhone = getIntent().getStringExtra("phone");
		dvCountry = getIntent().getStringExtra("country");
		dvCity = getIntent().getStringExtra("city");
		dvAddress = getIntent().getStringExtra("address");
		dvPostalCode = getIntent().getStringExtra("postalcode");
		dvImgLink = getIntent().getStringExtra("imgLink");
		dvWorkTime = getIntent().getStringExtra("workTime");

		// set text from Intent
		inputName.setText(dvName);
		inputEmail.setText(dvEmail);
		inputPhone.setText(dvPhone);
		inputAdress.setText(dvAddress + ", " + dvCity + ", " + dvCountry + ", "
				+ dvPostalCode);
		inputWorkTime.setText(dvWorkTime);

		// set clickListener
		btn_save.setOnClickListener(clickListener);
		inputEmail.setOnClickListener(clickListener);
		inputPhone.setOnClickListener(clickListener);

		db = new DBVendorHelper(getApplicationContext());
		/**
		 * Google Maps
		 **/

		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();

		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getBaseContext());

		if (status != ConnectionResult.SUCCESS) { // Google Play Services are
			// not available

			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
					requestCode);
			dialog.show();
		} else {
			// Initializing
			mMarkerPoints = new ArrayList<LatLng>();
			// Getting reference to SupportMapFragment of the activity_main
			Fragment fm = (Fragment) getFragmentManager().findFragmentById(
					R.id.map);

			// Getting Map for the MapFragment
			googleMap = ((MapFragment) fm).getMap();

			// Enable MyLocation Button in the Map
			googleMap.setMyLocationEnabled(true);

			// Getting LocationManager object from System
			// ServiceLOCATION_SERVICE
			LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

			// Creating a criteria object to retrieve provider
			Criteria criteria = new Criteria();

			// Getting the name of the best provider
			String provider = locationManager.getBestProvider(criteria, true);

			// Getting Current Location From GPS
			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				onLocationChanged(location);
			}
			locationManager.requestLocationUpdates(provider, 20000, 0, this);

			// Checks, whether start and end locations are captured
			if (mMarkerPoints.size() >= 2) {
				LatLng origin = mMarkerPoints.get(0);
				LatLng dest = mMarkerPoints.get(1);

				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(origin, dest);

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions API
				downloadTask.execute(url);
			}
		}
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/**
	 * A method to download json data from url
	 */

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception while downloading url", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	/** A class to download data from Google Directions URL */

	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread

		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);

		}
	}

	/** A class to parse the Google Directions in JSON format */

	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread

		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process

		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);

			}

			// Drawing polyline in the Google Map for the i-th route
			googleMap.addPolyline(lineOptions);
		}
	}

	public void onLocationChanged(Location location) { 
		// Draw the marker if destination location is not set
		if (mMarkerPoints.size() < 2) {

			double mLatitude = location.getLatitude();
			double mLongitude = location.getLongitude();

			LatLng currentPoint = new LatLng(mLatitude, mLongitude);
			LatLng destinationPoint = getLocationFromAdress(inputAdress
					.getText().toString());

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					currentPoint, 16));
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
					destinationPoint, 16));
			googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
			// googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
			// 2000, null);
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));

			drawMarker(currentPoint);
			drawMarker(destinationPoint);
		}
	}

	private void drawMarker(LatLng point) {
		mMarkerPoints.add(point);

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		// Setting the position of the marker 
		options.position(point);
		// For the start location, the color of marker is GREEN and for the end
		// location, the color of marker is RED.

		if (mMarkerPoints.size() == 1) {
			options.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
			options.title("My Location");
		} else if (mMarkerPoints.size() == 2) {
			options.icon(BitmapDescriptorFactory
					.defaultMarker(BitmapDescriptorFactory.HUE_RED));
			options.title(inputName.getText().toString());
		}

		// Add new marker to the Google Map Android API V2
		googleMap.addMarker(options);
	}

	public LatLng getLocationFromAdress(String strAddress) {
		Geocoder geocoder = new Geocoder(this);
		List<Address> address;
		LatLng latLng = null;
		try {
			address = geocoder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			double lat = location.getLatitude();
			double lon = location.getLongitude();

			latLng = new LatLng(lat, lon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return latLng;

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = v.getId();
			if (i == R.id.detail_src_btn_save) {
				String uid;
				db.open();
				Cursor cursor = db.checkFavoriteVendor(dvUID);
				if (cursor.moveToFirst()) {
					do {
						uid = cursor.getString(DBVendorHelper.COL_UID);
					} while (cursor.moveToNext());
					if (!uid.isEmpty()) {
						db.deleteFavorite(dvUID);
						Log.d("check", "deleted");
					}
				} else {
					Log.d("check", "unchecked");
					db.addFavoriteVendor(dvUID, dvName, dvEmail, dvPhone,
							dvCountry, dvCity, dvAddress, dvPostalCode,
							dvImgLink, dvWorkTime);
				}
				db.close();
			} else if (i == R.id.detail_scr_email) {

				Intent sendEmail = new Intent(Intent.ACTION_SEND);
				sendEmail.setData(Uri.parse("mailto:"));
				sendEmail.setType("text/plain");

				sendEmail.putExtra(Intent.EXTRA_EMAIL,
						new String[] { inputEmail.getText().toString() });
				try {
					startActivity(Intent.createChooser(sendEmail,
							"Send email..."));
					finish();
					Log.i("Finished sending email...", "");
				} catch (android.content.ActivityNotFoundException e) {
					Toast.makeText(getApplicationContext(),
							"There is no email client installed.",
							Toast.LENGTH_SHORT).show();
				}
			} else if (i == R.id.detail_src_tel) {

				Intent phoneCall = new Intent(Intent.ACTION_CALL);
				phoneCall.setData(Uri.parse("tel:"
						+ inputPhone.getText().toString()));

				try {
					startActivity(phoneCall);
					finish();
					Log.i("Finished making a call...", "");
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(),
							"Call faild, please try again later.",
							Toast.LENGTH_SHORT).show();
				}

			}

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.additional_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.logout) {
			sharedPreferences = new mySharedPreferences(getApplicationContext());
			sharedPreferences.clearPrefence();

			Intent loginIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(loginIntent);
			finish();
		} else if (id == R.id.back) {
			Intent homeIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}
