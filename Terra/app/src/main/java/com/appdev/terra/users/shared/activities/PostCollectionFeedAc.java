package com.appdev.terra.users.shared.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;

import com.appdev.terra.R;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.government.activities.PostFeedGovAc;
import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class PostCollectionFeedAc extends AppCompatActivity {
    // Services
    protected PostService postService = new PostService("__GOV__");
    protected LocationService locationService;


    // Helper objects
    protected List<PostCollection> nearbyAccidents = new ArrayList<>();
    protected List<PostCollection> filteredNearbyAccidents = new ArrayList<>();
    protected PostCollectionVHAdapter nearbyAccidentsAdapter = new PostCollectionVHAdapter(
            nearbyAccidents,
            new PostCollectionVHAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(PostCollection item) {
                    // Open an activity based on this collection
                    System.out.println("Clicked: " + item.getLocation().toString());
                    Intent intent = new Intent(getApplicationContext(), PostFeedGovAc.class);
                    intent.putExtra("latitude", item.getLatitude());
                    intent.putExtra("longitude", item.getLongitude());
                    startActivity(intent);
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link all GUI elements
        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView postCollectionsRecyclerView = findViewById(R.id.recyclerView);

        // Instantiate the location services
        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        // Set up the list of nearby emergency locations
        postCollectionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postCollectionsRecyclerView.setAdapter(nearbyAccidentsAdapter);

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

        // Define search bar behavior
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

        // Initialize the list of filtered nearby accidents to a list of all nearby accidents
        filteredNearbyAccidents.addAll(nearbyAccidents);

        // Send a notification if nearby accidents exist
        if (nearbyAccidents.size() > 0) {
            sendNearbyNotification();
        }
    }

    // TODO: Document that crap
    private void search(String query) {
        // Clear the current filteredItems list
        filteredNearbyAccidents.clear();

        // Loop through the original items list and add the items that match the query
        for (PostCollection collection : nearbyAccidents) {
            if (collection.getLocationName(this).toLowerCase().contains(query.toLowerCase())) {
                filteredNearbyAccidents.add(collection);
            }
        }

        // Update the RecyclerView with the filtered items list
        nearbyAccidentsAdapter.setItems(filteredNearbyAccidents);
    }

    // TODO: Document that crap
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel post";
            String description = "Channel post Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    // TODO: Document that crap
    private void sendNearbyNotification() {
        createNotificationChannel();
        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");

        if (userLocationOption == null) {
            System.out.println("Failed to get location for post feed!");
        } else if (userLocationOption.isPresent()) {
            if (!AccountService.nearbyNotificationSent){
                postService.nearbyPostsExists(userLocationOption.get(), new IFirestoreCallback() {
                    @Override
                    public void onCallback(boolean result, String message) {
                        if (result) {
                            builder.setSmallIcon(R.drawable.ic_launcher_background)
                                    .setContentTitle("Nearby Posts Exist!")
                                    .setContentText("There are nearby posts available! Please check nearby posts...")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                            int notificationId = 1;
                            try {
                                notificationManager.notify(notificationId, builder.build());
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                AccountService.nearbyNotificationSent = true;
            }
        }
    }
}
