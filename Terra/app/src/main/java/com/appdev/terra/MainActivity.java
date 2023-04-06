package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    // Services
    PostService postService = new PostService();

    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

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

        Button sosButton = (Button) findViewById(R.id.sos_button);


        Spinner locationSpinner = findViewById(R.id.location_spinner);
        SpinnerUtils.populateLocationSpinner(this, locationSpinner,locationService.getGeoPoint().get().toString());
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = parent.getItemAtPosition(position).toString();
                if (selectedLocation.equals("Another location")) {
                    Toast.makeText(getApplicationContext(), "If you want to create a post with another location pLease use the + button in the feed page.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });


        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

                if (userLocationOption == null) {
                    Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userLocationOption.isPresent()) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                    return;
                }


                PostModel post = PostModel.sosPost(userLocationOption.get());
                postService.add(post, new IFirestoreCallback<PostModel>() {});
            }
        });


    }
}