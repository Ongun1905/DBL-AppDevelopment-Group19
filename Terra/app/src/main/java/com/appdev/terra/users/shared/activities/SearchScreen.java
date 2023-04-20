package com.appdev.terra.users.shared.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.appdev.terra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

public abstract class SearchScreen extends AppCompatActivity {
    private List<PostCollection> nearbyAccidents;
    private RecyclerView nearbyAccidentsRecyclerView;
    private PostCollectionVHAdapter nearbyAccidentsAdapter;
    private TextView sheltersList, resourcesList;
    private LocationService locationService;
    protected PostService postService = new PostService("__GOV__");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        sheltersList = findViewById(R.id.shelters_list);
        resourcesList = findViewById(R.id.resources_list);

        // Initialize the data for the nearby accidents section
        nearbyAccidents = new ArrayList<>();

        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

        if (userLocationOption == null) {
            System.out.println("Failed to get location for post feed!");
        } else if (userLocationOption.isPresent()) {
            postService.getNearbyPostCollections(userLocationOption.get(), new IFirestoreCallback<PostCollection>() {
                @Override
                public void onCallback(ArrayList<PostCollection> models) {
                    IFirestoreCallback.super.onCallback(models);
                    for (PostCollection collection : models) {
                        nearbyAccidents.add(collection);
                    }
                    nearbyAccidentsAdapter.setItems(nearbyAccidents);
                }
            });
        }

        String shelters = "1. Shelter A\n2. Shelter B\n3. Shelter C";
        sheltersList.setText(shelters);

        // Dummy text for Resources
        String resources = "1. Hospital A\n2. Police Station B\n3. Fire Station C";
        resourcesList.setText(resources);

        // Initialize the RecyclerViews for each section
        nearbyAccidentsRecyclerView = findViewById(R.id.nearbyAccidentsList);
        nearbyAccidentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nearbyAccidentsAdapter = new PostCollectionVHAdapter(nearbyAccidents, new PostCollectionVHAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostCollection item) {
                // Open an activity based on this collection
                System.out.println("Clicked: " + item.getLocation().toString());
            }
        });
        nearbyAccidentsRecyclerView.setAdapter(nearbyAccidentsAdapter);
    }
}
