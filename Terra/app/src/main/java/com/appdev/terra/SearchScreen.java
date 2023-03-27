package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private List<Post> nearbyAccidents;
    private RecyclerView nearbyAccidentsRecyclerView;
    private MyAdapter nearbyAccidentsAdapter;

    private TextView sheltersLabel, resourcesLabel, sheltersList, resourcesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.search);

        sheltersLabel = findViewById(R.id.shelters_label);
        resourcesLabel = findViewById(R.id.resources_label);
        sheltersList = findViewById(R.id.shelters_list);
        resourcesList = findViewById(R.id.resources_list);

        // Initialize the data for the nearby accidents section
        nearbyAccidents = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Post post = new Post("Accident " + i, "Username " + i, "Location " + i, "Level " + i);
            nearbyAccidents.add(post);
        }

        String shelters = "1. Shelter A\n2. Shelter B\n3. Shelter C";
        sheltersList.setText(shelters);

        // Dummy text for Resources
        String resources = "1. Hospital A\n2. Police Station B\n3. Fire Station C";
        resourcesList.setText(resources);

        // Initialize the RecyclerViews for each section
        nearbyAccidentsRecyclerView = findViewById(R.id.nearbyAccidentsList);
        nearbyAccidentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nearbyAccidentsAdapter = new MyAdapter(nearbyAccidents);
        nearbyAccidentsRecyclerView.setAdapter(nearbyAccidentsAdapter);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(), ContactScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sos:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
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
                        return true;

                }
                return false;
            }
        });
    }
}