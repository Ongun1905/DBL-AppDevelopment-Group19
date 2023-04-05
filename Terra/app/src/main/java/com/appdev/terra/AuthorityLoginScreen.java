package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appdev.terra.models.AuthTokenModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;

public class AuthorityLoginScreen extends AppCompatActivity {

    ImageButton buttonBack;
    Button buttonLoginAuthority;

    EditText username;
    EditText authToken;

    AccountService accountService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authority_login_screen);

        buttonBack = (ImageButton) findViewById(R.id.authority_back_button);
        buttonLoginAuthority = (Button) findViewById(R.id.login_button_authority);

        username = (EditText) findViewById(R.id.editTextTextEmailAddress);
        authToken = (EditText) findViewById(R.id.editTextTextPassword);

        accountService = new AccountService();

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
                if (!username.getText().toString().matches("") && !authToken.getText().toString().matches("")) {
                    accountService.validateToken(username.getText().toString(), authToken.getText().toString(), new IFirestoreCallback<AuthTokenModel>() {
                        @Override
                        public void onCallback(AuthTokenModel model, boolean loginSuccess, String message) {
                            if (!loginSuccess) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                System.out.println(message);
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AuthorityLoginScreen.this, HomeScreenGov.class);
                                startActivity(intent);
                                System.out.println(message);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in your credentials.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}