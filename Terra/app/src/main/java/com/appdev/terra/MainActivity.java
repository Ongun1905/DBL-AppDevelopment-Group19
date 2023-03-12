package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.UserService;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = new UserService();
                UserModel model = userService.get("Re86sT1MJa3Pm30whdqr");
                System.out.println("data: " + model.name);

            }
        });
    }
}