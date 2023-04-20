package com.appdev.terra.users.government.activities;

import android.os.Bundle;

import com.appdev.terra.users.shared.activities.SearchScreen;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.R;

public class GovernmentSearchScreen extends SearchScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        set out the layout and the navigation bar needed  for this page
        setContentView(R.layout.activity_authority_search_screen);
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.search);
    }
}