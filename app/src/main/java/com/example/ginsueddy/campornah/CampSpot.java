package com.example.ginsueddy.campornah;

/**
 * Created by ginsueddy on 2/22/18.
 */

public class CampSpot {
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    public CampSpot (String name, String description, double latitude, double longitude){
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}

