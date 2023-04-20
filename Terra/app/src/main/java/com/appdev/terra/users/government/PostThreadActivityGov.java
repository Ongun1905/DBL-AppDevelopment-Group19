package com.appdev.terra.users.government;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.shared.utils.PostVHAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PostThreadActivityGov extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    private List<PostModel> items = new ArrayList<>();
    private List<PostModel> filteredItems = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;
    private PostVHAdapter adapter;
    private ScrollView scrollView;

    private PostCollection posts;

    private PostService postService = new PostService("__GOV__");


    public static final int REQUEST_LOCATION = 1;
    // Should keep track of location data
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_government_home_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewAuthority);
        bottomNavigationView.setSelectedItemId(R.id.home);

        Bundle extras = getIntent().getExtras();


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

        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        postService.getPostCollection(
                PostModel.makeGeoId(
                        extras.getDouble("latitude"),
                        extras.getDouble("longitude")
                ), new IFirestoreCallback<PostCollection>() {
                    @Override
                    public void onCallback(PostCollection collection) {
                        IFirestoreCallback.super.onCallback(collection);

                        items.addAll(collection.getPosts());

                        adapter.notifyDataSetChanged();
                    }
                }
        );

        scrollView = findViewById(R.id.scrollView2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostVHAdapter(items, new PostVHAdapter.PostVHUpdate() {
            @Override
            public void onClick(PostModel post) {
                // Start the UpdatePostScreen activity with the post data
                Intent intent = new Intent(getApplicationContext(), UpdatePostScreen.class);
                intent.putExtra("geoPointId", post.geoId);
                intent.putExtra("userId", post.userId);
                startActivity(intent);
            }

            @Override
            public boolean shouldHaveRedCross(PostModel model) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);

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
        for (PostModel post : items) {
            if (post.description.toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(post);
            }
        }

        // Update the RecyclerView with the filtered items list
        adapter.setItems(filteredItems);
    }
}