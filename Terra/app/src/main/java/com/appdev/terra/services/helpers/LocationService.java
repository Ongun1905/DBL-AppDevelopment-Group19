package com.appdev.terra.services.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.GeoPoint;

import java.util.Optional;

public class LocationService implements LocationListener {
    public static final int REQUEST_LOCATION = 1;
    private final LocationManager locationManager;
    private final Context context;
    private final Activity activity;

    public LocationService(LocationManager locationManager, Context context, Activity activity) {
        this.locationManager = locationManager;
        this.context = context;
        this.activity = activity;

        ActivityCompat.requestPermissions( activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            locationManager.requestLocationUpdates(android.location.LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    /**
     * A method that returns an optional location. If the returned value is `null`, location
     * permissions are not granted. If the enclosed value in the `Optional` wrapper is null, the
     * location could not be accessed. If the the enclosed value is a location, that is the most
     * recent user location.
     *
     * @return Possibly the user's location. Could fail in two ways.
     */
    public Optional<Location> getLocation() {
        if (
           ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
           ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return null;
        } else {
            return Optional.ofNullable(locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER));
        }
    }

    /**
     * A method that returns an optional GeoPoint. If the returned value is `null`, location
     * permissions are not granted. If the enclosed value in the `Optional` wrapper is null, the
     * location could not be accessed. If the the enclosed value is a GeoPoint representing the most
     * recent user location.
     *
     * @return Possibly the user's location as a GeoPoint. Could fail in two ways.
     */
    public Optional<GeoPoint> getGeoPoint() {
        if (
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return null;
        } else {
            Location currentLocation = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);

            if (currentLocation == null) {
                return Optional.empty();
            } else {
                return Optional.of(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Wow! Such empty!
    }
}
