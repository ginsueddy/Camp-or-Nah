package com.example.ginsueddy.campornah;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ginsueddy on 1/30/18.
 */

public class MarkerActivity extends AppCompatActivity {

    private static final String TAG = "MarkerActivity";

    private TextView mLatitudeDisplay;
    private TextView mLongitudeDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Toast.makeText(this, "Intent success", Toast.LENGTH_SHORT).show();

        mLatitudeDisplay = (TextView) findViewById(R.id.latitude_text_view);
        mLongitudeDisplay = (TextView) findViewById(R.id.longitude_text_view);

        Intent intentPackage = getIntent();
        Bundle extras = intentPackage.getExtras();
        double latitude = 0;
        double longitude;
        if(intentPackage != null){
            latitude = extras.getDouble("EXTRA_LATITUDE");
        }

        Toast.makeText(this, "latitude: " + latitude, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onCreate: mylatitude " + latitude);
    }
}
