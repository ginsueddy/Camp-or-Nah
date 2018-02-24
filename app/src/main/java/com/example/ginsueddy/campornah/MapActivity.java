package com.example.ginsueddy.campornah;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.LinkedList;


/**
 * Created by ginsueddy on 1/15/18.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private boolean mLocationPermissionGranted = false;
    private boolean gotDeviceLocation = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mCurrentLocation;
    private LatLng mLatLng;

    private LinkedList<CampSpot> campSpots = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if(loadCampSpotJsonFromInternalStorage() != null){
            deserializeCampSpots(loadCampSpotJsonFromInternalStorage());
        }

        getLocationPermission();
    }

    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                initMap();
            }
            else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(mLocationPermissionGranted){

                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            mCurrentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                            gotDeviceLocation = true;

                            addLocationInfo();

                        }
                        else{
                            Toast.makeText(MapActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void addLocationInfo(){
        ImageButton mAddLocation = (ImageButton) findViewById(R.id.ic_add_location_button);
        mAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                Toast.makeText(MapActivity.this, "test success", Toast.LENGTH_SHORT).show();
                Intent intentToMarker = new Intent(MapActivity.this, MarkerActivity.class);
                Bundle extras = new Bundle();
                extras.putDouble("EXTRA_LATITUDE", mLatLng.latitude);
                extras.putDouble("EXTRA_LONGITUDE", mLatLng.longitude);
                intentToMarker.putExtras(extras);

                startActivityForResult(intentToMarker,101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101 && resultCode == RESULT_OK){
            if(data.hasExtra("EXTRA_NAME") && data.hasExtra("EXTRA_DESCRIPTION") &&
                    data.hasExtra("EXTRA_LATITUDE") && data.hasExtra("EXTRA_LONGITUDE")){
                Toast.makeText(this, data.getExtras().getString("EXTRA_NAME"), Toast.LENGTH_SHORT).show();
                mMap.addMarker(new MarkerOptions().position(mLatLng).title(data.getExtras().getString("EXTRA_NAME")));

                CampSpot campSpot = new CampSpot(data.getExtras().getString("EXTRA_NAME"),
                        data.getExtras().getString("EXTRA_DESCRIPTION"),
                        data.getExtras().getDouble("EXTRA_LATITUDE"),
                        data.getExtras().getDouble("EXTRA_LONGITUDE"));

                serializeCampSpots(campSpot);
            }
        }
    }

    private void serializeCampSpots(CampSpot campSpot){
        campSpots.add(campSpot);

        Gson gson = new Gson();
        String campSpotJson = gson.toJson(campSpots);
        saveCampSpotJsonToInternalStorage(campSpotJson);
    }

    private void saveCampSpotJsonToInternalStorage(String campSpotJson){
        String fileName = getResources().getString(R.string.saved_camp_sites_file);
        try {
            FileOutputStream fileOutputStream = openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(campSpotJson.getBytes());
            Log.d(TAG, "saveCampSpotJsonToInternalStorage: Camp spots saved to " + getFilesDir() + "/" + fileName);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String loadCampSpotJsonFromInternalStorage() {
        try {
            FileInputStream fileInputStream = openFileInput(getResources().getString(R.string.saved_camp_sites_file));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String jsonString;

            while((jsonString = bufferedReader.readLine())!= null){
                stringBuilder.append(jsonString);
            }

            fileInputStream.close();
            return stringBuilder.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void deserializeCampSpots(String jsonString){
        Log.d(TAG, "deserializeCampSpots: " + jsonString);

        Type campSpotListType = new TypeToken<LinkedList<CampSpot>>(){}.getType();

        campSpots = new Gson().fromJson(jsonString, campSpotListType);
    }
}
