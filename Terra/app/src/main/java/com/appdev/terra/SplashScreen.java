package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SplashScreen extends AppCompatActivity {

    Button buttonCitizen;
    Button buttonGov;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        buttonCitizen = (Button) findViewById(R.id.button);
        buttonGov = (Button) findViewById(R.id.button2);

        buttonCitizen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, HomeScreen.class);
                startActivity(intent);

            }
        });

        buttonGov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashScreen.this, HomeScreenGov.class);
                startActivity(intent);

            }
        });
    }
}