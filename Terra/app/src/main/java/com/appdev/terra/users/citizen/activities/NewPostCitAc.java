package com.appdev.terra.users.citizen.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.utils.SpinnerUtils;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.users.shared.utils.CheckBoxVHAdapter;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.Optional;

public class NewPostCitAc extends AppCompatActivity {
    // Services
    private final PostService postService = new PostService();
    private LocationService locationService;


    // Helper objects
    private CheckBoxVHAdapter checkBoxAdapter;
    private StatusEnum status;


    private static final String TAG = NewPostCitAc.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        // Instantiate the location services
        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        // Link all GUI elements
        RecyclerView checkBoxList = findViewById(R.id.checkboxes);
        EditText description = findViewById(R.id.description);
        Button submitBtn = findViewById(R.id.submitButton);
        Spinner statusSpinner = findViewById(R.id.statusSpinner);

        // Set up the navigation bar menu at the bottom of the page
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.home);

        // Instantiate the adapter for the qualifications checkbox list
        checkBoxAdapter = new CheckBoxVHAdapter(getApplicationContext(), CheckBox::toggle);

        // Set up the list of qualification checkboxes
        checkBoxList.setLayoutManager(new LinearLayoutManager(this));
        checkBoxList.setAdapter(checkBoxAdapter);

        // Set up location determining through the `Places` API
        String apiKey = "AIzaSyBbjGgg9-D0FK4rhhGaf6jm-CvEhqjVfKc";
        Places.initialize(getApplicationContext(), apiKey);
        final LatLng[] selectedLocation = {null};

        // Initialize an AutocompleteSupportFragment for the location search bar
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the storage of location information whenever a location has been selected
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG
        ));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                selectedLocation[0] = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        // Set up the dropdown for the accident status and choose the first enum value as initial
        SpinnerUtils.populateStatusSpinner(this, statusSpinner);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = StatusEnum.valueOf((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });

        // Set the submit button to combine all information in a post and send it to the database
        submitBtn.setOnClickListener(view -> {
            Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

            if (userLocationOption == null) {
                System.out.println("Location permissions were not granted");
            } else {
                userLocationOption.ifPresent(geoPoint -> postService.add(new PostModel(
                        description.getText().toString(),
                        Timestamp.now(),
                        new GeoPoint(
                                selectedLocation[0].latitude,
                                selectedLocation[0].longitude
                        ),
                        status,
                        checkBoxAdapter.getQualifications()
                ), new IFirestoreCallback<PostModel>() {
                    @Override
                    public void onCallback(PostModel model1, String message) {
                        Toast.makeText(
                                getApplicationContext(), message, Toast.LENGTH_LONG
                        ).show();
                    }
                }));
            }

            Intent intent = new Intent(getApplicationContext(), PostCollectionFeedCitAc.class);
            startActivity(intent);
        });
    }
}

