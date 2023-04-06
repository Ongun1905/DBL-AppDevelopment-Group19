package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;

public class UserLoginScreen extends AppCompatActivity {

    Button loginButton;
    ImageButton backButton;

    EditText keyInput;
    EditText password;
    AccountService accountService;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login_screen);

        loginButton = (Button) findViewById(R.id.user_login_button);
        backButton = (ImageButton) findViewById(R.id.user_back_button);

        keyInput = (EditText) findViewById(R.id.editTextTextEmailAddress2);
        password = (EditText) findViewById(R.id.editTextTextPassword2);

        accountService = new AccountService();

        if (ContextCompat.checkSelfPermission(UserLoginScreen.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!keyInput.getText().toString().matches("") && !password.getText().toString().matches("")) {
                    accountService.login(Long.valueOf(keyInput.getText().toString()), password.getText().toString(), new IFirestoreCallback<UserModel>() {
                        @Override
                        public void onCallback(UserModel model, boolean loginSuccess, String message) {
                            if (!loginSuccess) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                System.out.println(message);
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(UserLoginScreen.this,HomeScreen.class);
                                startActivity(intent);
                                System.out.println(message);
                            }
                        }
                    });
                } else {
                    Toast.makeText(UserLoginScreen.this, "Please input your credentials.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginScreen.this,SplashScreen.class);
                startActivity(intent);

            }
        });
    }
}