package com.example.ginsueddy.campornah.camp.spot.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.ginsueddy.campornah.CampSpot;
import com.example.ginsueddy.campornah.InternalStorageIO;
import com.example.ginsueddy.campornah.R;

import java.util.ArrayList;

/**
 * Created by ginsueddy on 2/25/18.
 */

public class CampSpotDetailActivity extends AppCompatActivity{

    private TextView campSpotDescriptionDisplay;

    private  int selectedCampSpotIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_spot_detail);

        campSpotDescriptionDisplay = (TextView) findViewById(R.id.camp_spot_description_display);

        Intent intent = getIntent();
        if(intent != null){
            campSpotDescriptionDisplay.setText(intent.getExtras().getString("EXTRA_CAMP_SPOT_DES"));

            selectedCampSpotIndex = intent.getExtras().getInt("EXTRA_CAMP_SPOT_INDEX");
        }
    }

    private void deleteCampSpot(){
        ArrayList<CampSpot> campSpots = InternalStorageIO.deserializeCampSpotsArrayList(InternalStorageIO.loadCampSpotJsonFromInternalStorage(this.getApplicationContext()));
        campSpots.remove(selectedCampSpotIndex);
        InternalStorageIO.serializeCampSpotsArrayList(campSpots, this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camp_spot_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.delete_camp_spot){
            deleteCampSpot();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
