package com.example.ginsueddy.campornah;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ginsueddy on 1/30/18.
 */

public class MarkerActivity extends AppCompatActivity {

    private static final String TAG = "MarkerActivity";

    private EditText mNameEditText;
    private EditText mDescriptionEditText;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private ImageButton mFinishImageButton;

    private boolean doneButtonPressed = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
        Toast.makeText(this, "Intent success", Toast.LENGTH_SHORT).show();

        mNameEditText = (EditText) findViewById(R.id.name_edit_text);
        mDescriptionEditText = (EditText) findViewById(R.id.description_edit_text);
        mLatitudeTextView = (TextView) findViewById(R.id.latitude_text_view);
        mLongitudeTextView = (TextView) findViewById(R.id.longitude_text_view);
        mFinishImageButton = (ImageButton) findViewById(R.id.ic_finish_task_image_button);

        Intent intentPackage = getIntent();
        Bundle intentPackageExtras = intentPackage.getExtras();
        double latitude = 0;
        double longitude = 0;
        if(intentPackage != null){
            latitude = intentPackageExtras.getDouble("EXTRA_LATITUDE");
            longitude = intentPackageExtras.getDouble("EXTRA_LONGITUDE");
        }

        mLatitudeTextView.setText("Latitude: " + latitude);
        mLongitudeTextView.setText("Longitude: " + longitude);
        completeMarker();
    }

    private void completeMarker(){
        mFinishImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mNameEditText.getText().toString().isEmpty() && !mDescriptionEditText.getText().toString().isEmpty()){
                    doneButtonPressed = true;
                    finish();
                }
                else if(mNameEditText.getText().toString().isEmpty() && mDescriptionEditText.getText().toString().isEmpty()){
                    Toast.makeText(MarkerActivity.this,"Both fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(mNameEditText.getText().toString().isEmpty()){
                    Toast.makeText(MarkerActivity.this, "Name field is empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MarkerActivity.this, "Description field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void finish() {
        if(doneButtonPressed){
            Intent intentToMapActivity = new Intent();
            intentToMapActivity.putExtra("EXTRA_NAME", mNameEditText.getText().toString());
            intentToMapActivity.putExtra("EXTRA_DESCRIPTION", mNameEditText.getText().toString());
            setResult(RESULT_OK, intentToMapActivity);
            super.finish();
        }
        else{
            super.finish();
        }
    }
}
