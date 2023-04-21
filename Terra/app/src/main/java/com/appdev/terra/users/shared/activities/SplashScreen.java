package com.appdev.terra.users.shared.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.appdev.terra.R;
import com.appdev.terra.users.citizen.activities.LoginCitAc;
import com.appdev.terra.users.government.activities.LoginGovAc;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Link all GUI elements
        Button buttonCitizen = findViewById(R.id.splash_citizen_button);
        Button buttonGovernment = findViewById(R.id.splash_authority_button);

        // Make buttons direct to their respective login pages
        buttonCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginCitAc.class);
                startActivity(intent);
            }
        });

        buttonGovernment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginGovAc.class);
                startActivity(intent);
            }
        });
    }
}