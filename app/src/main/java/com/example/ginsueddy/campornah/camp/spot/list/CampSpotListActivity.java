package com.example.ginsueddy.campornah.camp.spot.list;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ginsueddy.campornah.CampSpot;
import com.example.ginsueddy.campornah.InternalStorageIO;
import com.example.ginsueddy.campornah.R;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ginsueddy on 2/18/18.
 */

public class CampSpotListActivity extends AppCompatActivity implements CampSpotAdapter.CampSpotAdapterOnClickHandler{

    private static final String TAG = "CampSpotListActivity";

    private RecyclerView campSpotListRecyclerView;
    private CampSpotAdapter campSpotAdapter;
    private TextView errorMessage;
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_start);

        campSpotListRecyclerView = (RecyclerView) findViewById(R.id.camp_spot_recycler_view);
        errorMessage = (TextView) findViewById(R.id.error_message_text);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        campSpotListRecyclerView.setLayoutManager(layoutManager);
        campSpotListRecyclerView.setHasFixedSize(true);

        campSpotAdapter = new CampSpotAdapter(this);
        campSpotListRecyclerView.setAdapter(campSpotAdapter);

        loadingIndicator = findViewById(R.id.camp_spot_list_loading_progress);
        loadingIndicator.setVisibility(View.VISIBLE);

        Log.d(TAG, "onCreate: made it");
        loadCampSpots();
    }

    private void loadCampSpots() {
        showCampSpotList();
        new FetchCampSpots().execute(InternalStorageIO.loadCampSpotJsonFromInternalStorage(this.getApplicationContext()));
    }

    private void showCampSpotList() {
        errorMessage.setVisibility(View.INVISIBLE);
        campSpotListRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(){
        campSpotListRecyclerView.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(String singleCampSpotDes, int campSpotIndex) {
        Intent intentToDetailActivity = new Intent(this, CampSpotDetailActivity.class);
        intentToDetailActivity.putExtra("EXTRA_CAMP_SPOT_DES", singleCampSpotDes);
        intentToDetailActivity.putExtra("EXTRA_CAMP_SPOT_INDEX", campSpotIndex);
        startActivity(intentToDetailActivity);
    }

    public class FetchCampSpots extends AsyncTask<String, Void, ArrayList<CampSpot>> {

        @Override
        protected ArrayList<CampSpot> doInBackground(String... strings) {
            if(strings.length == 0){
                return null;
            }
            String campSpotJson = strings[0];
            Log.d(TAG, "doInBackground: " + campSpotJson);
            return InternalStorageIO.deserializeCampSpotsArrayList(campSpotJson);
        }

        @Override
        protected void onPostExecute(ArrayList<CampSpot> campSpots) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if(campSpots != null){
                showCampSpotList();
                campSpotAdapter.setCampSpots(campSpots);
            }
            else {
                showErrorMessage();
            }
        }
    }
}
