package com.example.ginsueddy.campornah;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by ginsueddy on 1/30/18.
 */

public class MarkerActivity extends AppCompatActivity {

    private static final String TAG = "MarkerActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Toast.makeText(this, "Intent success", Toast.LENGTH_SHORT).show();
    }
}
