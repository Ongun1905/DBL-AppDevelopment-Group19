package com.appdev.terra.users.citizen.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.appdev.terra.R;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.users.shared.activities.LoginAc;

/**
 * An activity for the login page of citizen users. This page contains two fields for credential
 * input, a button to go back to the `SplashScreen` activity and a login confirmation button. When
 * the login button is clicked, either the user is requested to finish their credentials (one field
 * is empty) or they are informed that the login was (un)successful based on the credentials.
 */
public class LoginCitAc extends LoginAc {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_login_screen);
        super.onCreate(savedInstanceState);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.length() == 0 || authToken.length() == 0) {
                    Toast.makeText(
                            getApplicationContext(), "Please fill in your credentials.",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                accountService.login(
                        Long.valueOf(username.getText().toString()), authToken.getText().toString(),
                        new IFirestoreCallback<UserModel>() {
                            @Override
                            public void onCallback(boolean loginSuccess, String message) {
                                Toast.makeText(
                                        getApplicationContext(), message, Toast.LENGTH_SHORT
                                ).show();
                                if (loginSuccess) {
                                    Intent intent = new Intent(
                                            getApplicationContext(), PostCollectionFeedCitAc.class
                                    );
                                    startActivity(intent);
                                }
                            }
                        }
                );
            }
        });
    }
}