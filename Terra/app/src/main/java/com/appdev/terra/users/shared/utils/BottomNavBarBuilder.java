package com.appdev.terra.users.shared.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.appdev.terra.R;
import com.appdev.terra.users.citizen.ContactScreen;
import com.appdev.terra.users.citizen.HomeScreen;
import com.appdev.terra.users.citizen.MainActivity;
import com.appdev.terra.users.citizen.ProfileScreen;
import com.appdev.terra.users.government.activities.GovernmentSearchScreen;
import com.appdev.terra.users.government.HomeScreenGov;
import com.appdev.terra.users.citizen.activities.CitizenSearchScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public abstract class BottomNavBarBuilder {
    /**
     * PRECONDITION: the content view of `activity` must have a reference to the six XML components
     *               used in this method.
     * PRECONDITION: `selectedItemId` must refer to one of the XML components inside a citizen
     *               bottom navigation bar.
     *
     * @param activity
     * @param selectedItemId
     * @return
     */
    public static void setUpCitizenNavBar(Activity activity, @IdRes int selectedItemId) {
        BottomNavigationView navBar;
        navBar = activity.findViewById(R.id.bottomNavigationView);
        navBar.setSelectedItemId(selectedItemId);

        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (selectedItemId == item.getItemId()) return true;

                switch (item.getItemId()) {
                    case R.id.contact:
                        activity.startActivity(new Intent(activity.getApplicationContext(), ContactScreen.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                    case R.id.sos:
                        activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        activity.startActivity(new Intent(activity.getApplicationContext(), HomeScreen.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        activity.startActivity(new Intent(activity.getApplicationContext(), ProfileScreen.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        activity.startActivity(new Intent(activity.getApplicationContext(), CitizenSearchScreen.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });
    }

    /**
     * PRECONDITION: the content view of `activity` must have a reference to the three XML
     *               components used in this method.
     * PRECONDITION: `selectedItemId` must refer to one of the XML components inside a government
     *               bottom navigation bar.
     *
     * @param activity
     * @param selectedItemId
     */
    public static void setUpGovernmentNavBar(Activity activity, @IdRes int selectedItemId) {
        BottomNavigationView navBar;
        navBar = activity.findViewById(R.id.bottomNavigationViewAuthority);
        navBar.setSelectedItemId(selectedItemId);

        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (selectedItemId == item.getItemId()) return true;

                switch (item.getItemId()) {
                    case R.id.home:
                        activity.startActivity(new Intent(activity.getApplicationContext(), HomeScreenGov.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        activity.startActivity(new Intent(activity.getApplicationContext(), GovernmentSearchScreen.class));
                        activity.overridePendingTransition(0,0);
                        return true;

                }

                return false;
            }
        });
    }
}
