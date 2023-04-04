package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class AuthorityLoginScreen extends AppCompatActivity {

    ImageButton buttonBack;
    Button buttonLoginAuthority;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_login_screen);

        buttonBack = (ImageButton) findViewById(R.id.authority_back_button);
        buttonLoginAuthority = (Button) findViewById(R.id.login_button_authority);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthorityLoginScreen.this, SplashScreen.class);
                startActivity(intent);

            }
        });

        buttonLoginAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AuthorityLoginScreen.this, HomeScreenGov.class);
                startActivity(intent);
            }
        });

    }
}