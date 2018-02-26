package com.example.ginsueddy.campornah;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by ginsueddy on 2/23/18.
 */

public final class InternalStorageIO {

    private static final String TAG = "InternalStorageIOClass";

    public static void serializeCampSpotsLinkedList(LinkedList<CampSpot> campSpots, Context ctx){
        Gson gson = new Gson();
        String campSpotJson = gson.toJson(campSpots);
        saveCampSpotJsonToInternalStorage(campSpotJson, ctx);
    }
    public static void serializeCampSpotsArrayList(ArrayList<CampSpot> campSpots, Context ctx){
        Gson gson = new Gson();
        String campSpotJson = gson.toJson(campSpots);
        saveCampSpotJsonToInternalStorage(campSpotJson, ctx);
    }

    private static void saveCampSpotJsonToInternalStorage(String campSpotJson, Context ctx){
        String fileName = ctx.getResources().getString(R.string.saved_camp_sites_file);
        try {
            FileOutputStream fileOutputStream = ctx.openFileOutput(fileName, ctx.MODE_PRIVATE);
            fileOutputStream.write(campSpotJson.getBytes());
            Log.d(TAG, "saveCampSpotJsonToInternalStorage: Camp spots saved to " + ctx.getFilesDir() + "/" + fileName);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadCampSpotJsonFromInternalStorage(Context ctx) {
        try {
            FileInputStream fileInputStream = ctx.openFileInput(ctx.getResources().getString(R.string.saved_camp_sites_file));
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

    public static LinkedList<CampSpot> deserializeCampSpotsLinkedList(String jsonString){
        Log.d(TAG, "deserializeCampSpots: " + jsonString);

        Type campSpotListType = new TypeToken<LinkedList<CampSpot>>(){}.getType();
        return new Gson().fromJson(jsonString, campSpotListType);
    }

    public static ArrayList<CampSpot> deserializeCampSpotsArrayList(String jsonString){
        Log.d(TAG, "deserializeCampSpots: " + jsonString);

        Type campSpotListType = new TypeToken<ArrayList<CampSpot>>(){}.getType();
        return new Gson().fromJson(jsonString, campSpotListType);
    }
}
