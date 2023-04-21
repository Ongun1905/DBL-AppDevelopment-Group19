package com.appdev.terra.users.citizen.activities;

import android.os.Bundle;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.activities.SearchAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.services.PostService;

/**
 * An activity for the available nearby accidents and resources page of citizen users. This page
 * contains a list of nearby emergency locations, a list of nearby shelters, a list of nearby
 * resource distribution facilities and a navigation bar to move to different screens.
 */
public class SearchCitAc extends SearchAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a citizen-specific way
        setContentView(R.layout.activity_search_screen);
        postService = new PostService();
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.search);
    }
}