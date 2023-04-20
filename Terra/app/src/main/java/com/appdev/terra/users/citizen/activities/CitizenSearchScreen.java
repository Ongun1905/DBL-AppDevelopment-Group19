package com.appdev.terra.users.citizen.activities;

import android.os.Bundle;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.activities.SearchScreen;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.appdev.terra.services.PostService;

public class CitizenSearchScreen extends SearchScreen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        set up the navigation bar and the layout needed
        setContentView(R.layout.activity_search_screen);
        postService = new PostService();
        super.onCreate(savedInstanceState);
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.search);
    }
}