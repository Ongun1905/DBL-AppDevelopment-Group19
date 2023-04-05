package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class PostThreadActivityGov extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    private List<Post> items = new ArrayList<>();
    private List<Post> filteredItems = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;
    private MyAdapterThreadGov adapter;
    private ScrollView scrollView;


    public static final int REQUEST_LOCATION = 1;
    // Should keep track of location data
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_government_home_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationViewAuthority);
        bottomNavigationView.setSelectedItemId(R.id.home);


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
        //a/ Add request for location permissions
        // Request location
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        //a/

        for (int i = 5; i <= 9; i++) {
            boolean reqmet = true;
            if ((i==6) || (i==8)){
                reqmet = false;
            }

            Post post = new Post("Post " + i, "Username " + i, "Location " + i, "Level " + i, "Requirements:"+i,true, reqmet );
            items.add(post);
        }



        scrollView = findViewById(R.id.scrollView2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapterThreadGov(this, items);
        recyclerView.setAdapter(adapter);

        Button addButton = findViewById(R.id.authority_new_post_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostThreadActivityGov.this, NewPostActivity.class);
                startActivity(intent);
            }
        });

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
        for (Post post : items) {
            if (post.getPostText().toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(post);
            }
        }

        // Update the RecyclerView with the filtered items list
        adapter.setItems(filteredItems);
    }
}