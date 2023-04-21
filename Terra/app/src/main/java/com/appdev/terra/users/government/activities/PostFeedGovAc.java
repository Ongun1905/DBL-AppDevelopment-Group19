package com.appdev.terra.users.government.activities;

import android.os.Bundle;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.activities.PostFeedAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;

/**
 * An activity for the post list page of government users, associated with a post collection.
 * This page contains a list of clickable posts from this post collection, a search bar to search
 * among these posts and a navigation bar to move to different screens. When one of the posts is
 * clicked, the user moves to the `UpdatePostGovAc` activity where they can change post data.
 */
public class PostFeedGovAc extends PostFeedAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a government-specific way
        setContentView(R.layout.acitivity_government_home_screen);
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.home);
    }
}