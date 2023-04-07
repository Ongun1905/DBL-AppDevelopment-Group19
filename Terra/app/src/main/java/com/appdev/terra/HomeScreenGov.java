package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeScreenGov extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    private List<PostCollection> items = new ArrayList<>();
    private List<PostCollection> filteredItems = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;
    private MyAdapterGov adapter = new MyAdapterGov(items, new MyAdapterGov.OnItemClickListener() {
        @Override
        public void onItemClick(PostCollection item) {
            // Open an activity based on this collection
            System.out.println("Clicked: " + item.getLocation().toString());
            Intent intent = new Intent(getApplicationContext(), PostThreadActivityGov.class);
            intent.putExtra("latitude", item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());
            startActivity(intent);
        }
    });
    private ScrollView scrollView;

    private PostService postService = new PostService("__GOV__");

    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_government_home_screen);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        bottomNavigationView = findViewById(R.id.bottomNavigationViewAuthority);
        bottomNavigationView.setSelectedItemId(R.id.home);

        scrollView = findViewById(R.id.scrollView2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {


                    case R.id.home:
                        return true;


                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), AuthoritySearchScreen.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

        if (userLocationOption == null) {
            System.out.println("Failed to get location for post feed!");
        } else if (userLocationOption.isPresent()) {
            postService.getNearbyPostCollections(userLocationOption.get(), new IFirestoreCallback<PostCollection>() {
                @Override
                public void onCallback(ArrayList<PostCollection> models) {
                    IFirestoreCallback.super.onCallback(models);
                    for (PostCollection collection : models) {
                        items.add(collection);
                    }
                    adapter.setItems(items);
                }
            });
        }

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform the search
                search(query);
                // Return true to indicate that the search has been handled
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform the search on every text change
                search(newText);
                // Return true to indicate that the search has been handled
                return true;
            }
        });

        // Initialize filteredItems with all the items
        filteredItems.addAll(items);
    }

    private void search(String query) {
        // Clear the current filteredItems list
        filteredItems.clear();

        // Loop through the original items list and add the items that match the query
        for (PostCollection collection : items) {
            if (collection.getLocationName(this).toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(collection);
            }
        }

        // Update the RecyclerView with the filtered items list
        adapter.setItems(filteredItems);
    }
}