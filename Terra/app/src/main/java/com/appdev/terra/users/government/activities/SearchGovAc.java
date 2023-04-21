package com.appdev.terra.users.government.activities;

import android.os.Bundle;

import com.appdev.terra.users.shared.activities.SearchAc;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.R;

/**
 * An activity for the available nearby accidents and resources page of government users. This page
 * contains a list of nearby emergency locations, a list of nearby shelters, a list of nearby
 * resource distribution facilities and a navigation bar to move to different screens.
 */
public class SearchGovAc extends SearchAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set up the super-activity in a government-specific way
        setContentView(R.layout.activity_authority_search_screen);
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.search);
    }
}