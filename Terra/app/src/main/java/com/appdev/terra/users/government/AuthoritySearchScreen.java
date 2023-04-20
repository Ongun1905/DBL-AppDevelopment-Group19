package com.appdev.terra.users.government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

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

public class AuthoritySearchScreen extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private List<PostCollection> nearbyAccidents;
    private RecyclerView nearbyAccidentsRecyclerView;
    private PostCollectionVHAdapter nearbyAccidentsAdapter;

    private TextView sheltersList, resourcesList;

    private PostService postService = new PostService("__GOV__");

    private LocationService locationService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_search_screen);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        bottomNavigationView = findViewById(R.id.bottomNavigationViewAuthority);
        bottomNavigationView.setSelectedItemId(R.id.search);

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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreenGov.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        return true;

                }
                return false;
            }
        });

    }
}