package com.appdev.terra.models;

import com.google.firebase.firestore.GeoPoint;

public class EarthquakeModel {
    public String id;
    public GeoPoint location;
    public Double magnitude;
}
