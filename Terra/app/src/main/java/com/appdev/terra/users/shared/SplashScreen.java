package com.appdev.terra.users.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appdev.terra.R;
import com.appdev.terra.users.citizen.UserLoginScreen;
import com.appdev.terra.users.government.AuthorityLoginScreen;

public class SplashScreen extends AppCompatActivity {

    Button buttonCitizen;
    //Button buttonGov;
    Button buttonAuthority;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        buttonCitizen = (Button) findViewById(R.id.splash_citizen_button);
        buttonAuthority = (Button) findViewById(R.id.splash_authority_button);

        buttonCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, UserLoginScreen.class);
                startActivity(intent);

            }
        });

        buttonAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, AuthorityLoginScreen.class);
                startActivity(intent);

            }
        });

    }
}