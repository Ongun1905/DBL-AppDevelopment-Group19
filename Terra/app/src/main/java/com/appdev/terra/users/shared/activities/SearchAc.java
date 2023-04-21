package com.appdev.terra.users.shared.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.appdev.terra.R;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.location.LocationManager;
import android.widget.TextView;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class SearchAc extends AppCompatActivity {
    // Services
    private LocationService locationService;
    protected PostService postService = new PostService("__GOV__");


    // Helper objects
    private final List<PostCollection> nearbyAccidents = new ArrayList<>();
    private final PostCollectionVHAdapter nearbyAccidentsAdapter = new PostCollectionVHAdapter(
            nearbyAccidents,
            new PostCollectionVHAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PostCollection item) {
                    // Open an activity based on this collection
                    System.out.println("Clicked: " + item.getLocation().toString());
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link all GUI elements
        RecyclerView nearbyAccidentsRecyclerView = findViewById(R.id.nearbyAccidentsList);
        TextView sheltersList = findViewById(R.id.shelters_list);
        TextView resourcesList = findViewById(R.id.resources_list);

        // Instantiate the location services
        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        // Set up placeholder information for nearby shelter and resource distribution centers
        sheltersList.setText("1. Shelter A\n2. Shelter B\n3. Shelter C");
        resourcesList.setText("1. Hospital A\n2. Police Station B\n3. Fire Station C");

        // Set up the list of nearby emergency locations
        nearbyAccidentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nearbyAccidentsRecyclerView.setAdapter(nearbyAccidentsAdapter);

        // Fill the list of nearby emergency locations
        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();
        if (userLocationOption == null) {
            System.out.println("Location permissions were not granted");
        } else {
            userLocationOption.ifPresent(geoPoint -> postService.getNearbyPostCollections(
                    geoPoint,
                    new IFirestoreCallback<PostCollection>() {
                        @Override
                        public void onCallback(ArrayList<PostCollection> models) {
                            IFirestoreCallback.super.onCallback(models);
                            nearbyAccidents.addAll(models);
                            nearbyAccidentsAdapter.setItems(nearbyAccidents);
                        }
                    }
            ));
        }
    }
}
