package com.appdev.terra.users.government.activities;

import android.os.Bundle;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.activities.PostCollectionFeedAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;

/**
 * An activity for the post collections list page of government users.
 * This page contains a list of clickable nearby emergency locations, a search bar to search among
 * these locations and a navigation bar to move to different screens. When one of the post
 * collections is clicked, the user moves to the `PostFeedGovAc` activity where they can examine
 * specific posts.
 */
public class PostCollectionFeedGovAc extends PostCollectionFeedAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a government-specific way
        setContentView(R.layout.acitivity_government_home_screen);
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.home);
    }
}