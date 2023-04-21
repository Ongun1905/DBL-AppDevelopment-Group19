package com.appdev.terra.users.shared.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

import com.appdev.terra.R;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.government.activities.UpdatePostGovAc;
import com.appdev.terra.users.shared.utils.PostVHAdapter;

import java.util.ArrayList;
import java.util.List;

public class PostFeedAc extends AppCompatActivity {
    // Services
    protected PostService postService = new PostService("__GOV__");


    // Helper Objects
    protected List<PostModel> accidents = new ArrayList<>();
    protected final List<PostModel> filteredAccidents = new ArrayList<>();
    protected PostVHAdapter accidentPostsAdapter = new PostVHAdapter(
            accidents,
            new PostVHAdapter.PostVHUpdate() {
                @Override
                public void onClick(PostModel post) {
                    // Start the UpdatePostGovAc activity with the post data
                    Intent intent = new Intent(getApplicationContext(), UpdatePostGovAc.class);
                    intent.putExtra("geoPointId", post.geoId);
                    intent.putExtra("userId", post.userId);
                    startActivity(intent);
                }

                @Override
                public boolean shouldHaveRedCross(PostModel model) {
                    // Government users have no qualifications and hence no use for red crosses
                    return false;
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link all GUI elements
        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView accidentsRecyclerView = findViewById(R.id.recyclerView);

        // Set up the list of accident posts
        accidentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        accidentsRecyclerView.setAdapter(accidentPostsAdapter);

        // Retrieve and set the list of posts associated with the selected emergency location
        Bundle extras = getIntent().getExtras();
        postService.getPostCollection(
                PostModel.makeGeoId(
                        extras.getDouble("latitude"),
                        extras.getDouble("longitude")
                ), new IFirestoreCallback<PostCollection>() {
                    @Override
                    public void onCallback(PostCollection collection) {
                        IFirestoreCallback.super.onCallback(collection);
                        accidents.addAll(collection.getPosts());
                        accidentPostsAdapter.setItems(accidents);
                    }
                }
        );

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

        // Initialize filteredItems with all the items
        filteredAccidents.addAll(accidents);
    }

    // TODO: Document that crap
    private void search(String query) {
        // Clear the current list of filtered accidents
        filteredAccidents.clear();

        // Add the accidents that match the query to the list of filtered accidents
        for (PostModel post : accidents) {
            if (post.description.toLowerCase().contains(query.toLowerCase())) {
                filteredAccidents.add(post);
            }
        }

        // Update the RecyclerView with the filtered accidents list
        accidentPostsAdapter.setItems(filteredAccidents);
    }
}
