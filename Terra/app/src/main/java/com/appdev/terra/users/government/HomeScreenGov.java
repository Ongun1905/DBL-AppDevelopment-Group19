package com.appdev.terra.users.government;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.appdev.terra.R;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeScreenGov extends AppCompatActivity {
    private List<PostCollection> items = new ArrayList<>();
    private List<PostCollection> filteredItems = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;
    private PostCollectionVHAdapter adapter = new PostCollectionVHAdapter(items, new PostCollectionVHAdapter.OnItemClickListener() {
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
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.home);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        scrollView = findViewById(R.id.scrollView2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();
//      get the posts of a certain thread by using their common location
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

//      deal with the search functionality of the feed
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