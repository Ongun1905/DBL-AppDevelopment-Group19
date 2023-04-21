package com.appdev.terra.users.citizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appdev.terra.R;
import com.appdev.terra.users.citizen.NewPostActivity;
import com.appdev.terra.users.shared.activities.PostCollectionFeedAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.users.shared.utils.PostCollectionVHAdapter;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.helpers.PostCollection;

/**
 * An activity for the post collections list page of citizen users.
 * This page contains a list of clickable nearby emergency locations, a search bar to search among
 * these locations, a button for making a new post and a navigation bar to move to different
 * screens. When one of the post collections is clicked, the user moves to the `PostFeedCitAc`
 * activity where they can examine specific posts. When the new post button is clicked, the user
 * moves to the `NewPostCitAc` activity where they can create a new post.
 */
public class PostCollectionFeedCitAc extends PostCollectionFeedAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a citizen-specific way
        setContentView(R.layout.activity_home_screen);
        postService = new PostService();
        nearbyAccidentsAdapter = new PostCollectionVHAdapter(
                nearbyAccidents,
                new PostCollectionVHAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(PostCollection item) {
                        // Open an activity based on this collection
                        System.out.println("Clicked: " + item.getLocation().toString());
                        Intent intent = new Intent(getApplicationContext(), PostFeedCitAc.class);
                        intent.putExtra("latitude", item.getLatitude());
                        intent.putExtra("longitude", item.getLongitude());
                        intent.putExtra("geoId", PostModel.makeGeoId(
                                item.getLatitude(),
                                item.getLongitude()
                        ));
                        startActivity(intent);

                    }
                }
        );
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.home);

        // Link and set a "new post" button that leads to the `NewPostCitAc` activity
        Button addButton = findViewById(R.id.user_new_post_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        PostCollectionFeedCitAc.this,
                        NewPostActivity.class
                );
                startActivity(intent);
            }
        });
    }
}