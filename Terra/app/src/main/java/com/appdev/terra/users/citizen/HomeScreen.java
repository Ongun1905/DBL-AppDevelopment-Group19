package com.appdev.terra.users.citizen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SearchView;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeScreen extends AppCompatActivity {
    private List<PostCollection> items = new ArrayList<>();
    private List<PostCollection> filteredItems = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;
//    get the related posts feed when a thread is clicked
    private PostCollectionVHAdapter adapter = new PostCollectionVHAdapter(items, new PostCollectionVHAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(PostCollection item) {
            // Open an activity based on this collection
            System.out.println("Clicked: " + item.getLocation().toString());
            Intent intent = new Intent(getApplicationContext(), PostThreadActivity.class);
            intent.putExtra("latitude", item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());
            intent.putExtra("geoId", PostModel.makeGeoId(item.getLatitude(), item.getLongitude()));
            startActivity(intent);

        }
    });
    private ScrollView scrollView;

    private PostService postService = new PostService();

    private LocationService locationService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.home);

        Button addButton = findViewById(R.id.user_new_post_button);

        scrollView = findViewById(R.id.scrollView2);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();
//      gets the posts of a certain thread
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

        sendNearbyNotification();

//      opens new post page if the + button is clicked
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, NewPostActivity.class);
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
        for (PostCollection collection : items) {
            if (collection.getLocationName(this).toLowerCase().contains(query.toLowerCase())) {
                filteredItems.add(collection);
            }
        }

        // Update the RecyclerView with the filtered items list
        adapter.setItems(filteredItems);
    }

//    creates a notification channel to be able to send notifications to the phone
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
//     checks if there exist nearby posts to put in the feed and notifies you
    private void sendNearbyNotification() {
        createNotificationChannel();
        Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
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

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(HomeScreen.this);
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