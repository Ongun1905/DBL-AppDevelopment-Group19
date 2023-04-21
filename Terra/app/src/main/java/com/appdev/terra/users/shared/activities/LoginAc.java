package com.appdev.terra.users.shared.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appdev.terra.R;
import com.appdev.terra.users.government.activities.PostCollectionFeedGovAc;
import com.appdev.terra.models.AuthTokenModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;

public abstract class LoginAc extends AppCompatActivity {
    // GUI elements
    protected Button loginButton;
    protected EditText username;
    protected EditText authToken;

    // Services
    protected final AccountService accountService = new AccountService();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Link all GUI elements
        ImageButton backButton = findViewById(R.id.back_button);
        loginButton = findViewById(R.id.login_button);
        username = findViewById(R.id.editTextUsername);
        authToken = findViewById(R.id.editTextAuthToken);

        // Set the back button to lead back to the `SplashAc` activity when pressed
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                startActivity(intent);

            }
        });

        // Set the login button to conditionally lead to the `PostCollectionFeedGovAc` activity
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

                accountService.validateToken(
                        username.getText().toString(), authToken.getText().toString(),
                        new IFirestoreCallback<AuthTokenModel>() {
                            @Override
                            public void onCallback(boolean loginSuccess, String message) {
                                Toast.makeText(
                                        getApplicationContext(), message, Toast.LENGTH_SHORT
                                ).show();

                                if (loginSuccess) {
                                    Intent intent = new Intent(
                                            getApplicationContext(), PostCollectionFeedGovAc.class
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
