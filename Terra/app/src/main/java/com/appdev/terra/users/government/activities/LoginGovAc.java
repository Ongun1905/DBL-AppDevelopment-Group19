package com.appdev.terra.users.government.activities;

import android.os.Bundle;

import com.appdev.terra.R;
import com.appdev.terra.users.shared.activities.LoginAc;

/**
 * An activity for the login page of government users. This page contains two fields for credential
 * input, a button to go back to the `SplashScreen` activity and a login confirmation button. When
 * the login button is clicked, either the user is requested to finish their credentials (one field
 * is empty) or they are informed that the login was (un)successful based on the credentials.
 */
public class LoginGovAc extends LoginAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_authority_login_screen);
        super.onCreate(savedInstanceState);
    }
}