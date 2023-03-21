package com.appdev.terra;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.button);
        Button addButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserService userService = new UserService();
                userService.get("Re86sT1MJa3Pm30whdqr", new IFirestoreCallback<UserModel>() {

                    @Override
                    public void onCallback(UserModel model) {
                        System.out.println("data: " + model.id);
                    }
                });
                //UserModel model = new UserModel();
                /*model.phoneNumber = Long.valueOf(234234234);
                model.password = "asdasdsa";
                model.name = "asdasdasdasd";
                model.surname = "asdasdasdasdasd";
                model.address = new GeoPoint(23.34, 4.213);
                model.contactIds = new ArrayList<>();
                userService.add(model); */


            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostService postService = new PostService();

                postService.add();

            }
        });
    }
}