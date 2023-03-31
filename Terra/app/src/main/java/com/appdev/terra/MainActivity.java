package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity implements LocationListener {

    BottomNavigationView bottomNavigationView;

    public static final int REQUEST_LOCATION = 1;

    // Should keep track of location data
    LocationManager locationManager;

    // Services
    PostService postService = new PostService();
    private List<Post> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.sos);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(), ContactScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sos:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });




        // Should request location
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        Button sendButton = (Button) findViewById(R.id.citizen_button);
        Button sosButton = (Button) findViewById(R.id.sos_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Optional<Location> userLocationOption = getLocation();

                if (userLocationOption == null) {
                    Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userLocationOption.isPresent()) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Location userLocation = userLocationOption.get();

                postService.getNearbyPostCollections(new GeoPoint(userLocation.getLatitude(), userLocation.getLongitude()), new IFirestoreCallback<PostCollection>() {
                    @Override
                    public void onCallback(ArrayList<PostCollection> collections) {
                        IFirestoreCallback.super.onCallback(collections);

                        for (PostCollection collection : collections) {
                            System.out.println(collection.getLocation());
                        }
                    }
                });

//                postService.get(PostModel.makeGeoId(userLocation.getLatitude(), userLocation.getLongitude()), new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(PostModel model) {
//                        IFirestoreCallback.super.onCallback(model);
//
//                        System.out.println("Found a post! It has as description:\n" + model.description);
//                    }
//                });

//                postService.remove(PostModel.makeGeoId(userLocation.getLatitude(), userLocation.getLongitude()), new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(PostModel model) {
//                        IFirestoreCallback.super.onCallback(model);
//
//                        System.out.println("Removed post with description:\n" + model.description);
//                    }
//                });

//                postService.getAllPosts(new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(ArrayList<PostModel> models) {
//                        IFirestoreCallback.super.onCallback(models);
//
//                        for (PostModel model : models) {
//                            System.out.println(model.description);
//                        }
//                    }
//                });
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                Optional<Location> userLocationOption = getLocation();

                if (userLocationOption == null) {
                    Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userLocationOption.isPresent()) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Location userLocation = userLocationOption.get();
                PostModel post = PostModel.sosPost(userLocation.getLatitude(), userLocation.getLongitude());
                postService.add(post, new IFirestoreCallback<PostModel>() {});
            }
        });
    }

    /**
     * A method that returns an optional location. If the returned value is `null`, location
     * permissions are not granted. If the enclosed value in the `Optional` wrapper is null, the
     * location could not be accessed. If the the enclosed value is a location, that is the most
     * recent user location.
     *
     * @return Possibly the user's location. Could fail in two ways.
     */
    private Optional<Location> getLocation() {
        if (
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)   != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return null;
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            return Optional.ofNullable(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Wow! Such empty!
    }
}