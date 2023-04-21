package com.appdev.terra.users.citizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appdev.terra.R;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.users.citizen.NewPostActivity;
import com.appdev.terra.users.shared.activities.PostFeedAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.users.shared.utils.PostVHAdapter;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.PostService;

import java.util.Arrays;

/**
 * An activity for the post list page of citizen users, associated with a post collection.
 * This page contains a list of posts from this post collection, a search bar to search among these
 * posts, a button for making a new post and a navigation bar to move to different screens. Posts on
 * this page display a red cross if one of their requested qualifications overlaps with the user's
 * qualifications. When the new post button is clicked, the user moves to the `NewPostCitAc`
 * activity where they can create a new post.
 */
public class PostFeedCitAc extends PostFeedAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a citizen-specific way
        setContentView(R.layout.activity_home_screen);
        postService = new PostService();
        accidentPostsAdapter = new PostVHAdapter(
                accidents,
                new PostVHAdapter.PostVHUpdate() {
                    @Override
                    public void onClick(PostModel item) {
                        // Wow, such empty
                    }

                    @Override
                    public boolean shouldHaveRedCross(PostModel post) {
                        return Arrays.stream(QualificationsEnum.values()).anyMatch(q ->
                                post.qualifications.get(q.toString())
                                && AccountService.logedInUserModel.qualifications.get(q)
                        );
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
                        PostFeedCitAc.this,
                        NewPostActivity.class
                );
                startActivity(intent);
            }
        });
    }
}