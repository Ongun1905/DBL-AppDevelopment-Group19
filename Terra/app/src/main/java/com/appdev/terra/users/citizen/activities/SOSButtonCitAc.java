package com.appdev.terra.users.citizen.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.utils.SpinnerUtils;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.google.firebase.firestore.GeoPoint;

import java.util.Optional;

public class SOSButtonCitAc extends AppCompatActivity {
    // Services
    private final PostService postService = new PostService();
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate the location services
        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        // Link all GUI elements
        Button sosButton = (Button) findViewById(R.id.sos_button);
        Spinner locationSpinner = findViewById(R.id.location_spinner);

        // Set up the navigation bar menu at the bottom of the page
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.sos);

        // Set up the dropdown for the accident location and choose the user's location as initial
        SpinnerUtils.populateLocationSpinner(
                this, locationSpinner, locationService.getGeoPoint().get().toString()
        );
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLocation = parent.getItemAtPosition(position).toString();
                if (selectedLocation.equals("Another location")) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Make custom location posts through the feed's + button",
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });

        // Set the SOS button to make a post based on location if location permissions are given
        sosButton.setOnClickListener(view -> {
            Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

            if (userLocationOption == null) {
                Toast.makeText(
                        getApplicationContext(), "Location permissions not granted!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (!userLocationOption.isPresent()) {
                Toast.makeText(
                        getApplicationContext(), "Unable to retrieve location!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }


            PostModel post = PostModel.sosPost(userLocationOption.get());
            postService.add(post, new IFirestoreCallback<PostModel>() {
            });
        });

    }
}