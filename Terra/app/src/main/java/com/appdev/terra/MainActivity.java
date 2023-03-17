package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;
import com.google.firebase.firestore.GeoPoint;

import android.Manifest;

import java.util.Optional;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int REQUEST_LOCATION = 1;

    // Should keep track of location data
    LocationManager locationManager;
    String latitude, longitude;

    // Services
    PostService postService = new PostService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Should request location
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        Button sendButton = (Button) findViewById(R.id.button);
        Button sosButton = (Button) findViewById(R.id.sos_button);
        Button homebutton = (Button) findViewById(R.id.home);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Feed.class);
                startActivity(intent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = new UserService();
                userService.get("Re86sT1MJa3Pm30whdqr", new IFirestoreCallback<UserModel>() {

                    @Override
                    public void onCallback(UserModel model) {
                        System.out.println("data: " + model.id);
                    }
                });
                //UserModel model = new UserModel();
                /*model.phoneNumber = Long.valueOf(234234234);
                model.password = "asdasdsa";
                model.name = "asdasdasdasd";
                model.surname = "asdasdasdasdasd";
                model.address = new GeoPoint(23.34, 4.213);
                model.contactIds = new ArrayList<>();
                userService.add(model); */


            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    Optional<Location> userLocationOption = getLocation();

                    if (userLocationOption != null) {
                        if (userLocationOption.isPresent()) {
                            Location userLocation = userLocationOption.get();
                            Toast.makeText(getApplicationContext(), "Longitude: " + String.valueOf(userLocation.getLongitude()), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Latitude: " + String.valueOf(userLocation.getLatitude()), Toast.LENGTH_SHORT).show();

                            PostModel post = PostModel.sosPost("415", userLocation.getLatitude(), userLocation.getLongitude());
                            postService.add(post, new IFirestoreCallback<PostModel>() {});
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
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