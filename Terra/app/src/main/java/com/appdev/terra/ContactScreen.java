package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ContactScreen extends AppCompatActivity {

    ListView listView;
    List list = new ArrayList<>();
    ArrayAdapter adapter;

    BottomNavigationView bottomNavigationView;
    Button buttonAddFriend;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.contact);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        return true;

                    case R.id.sos:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });



        buttonAddFriend = (Button) findViewById(R.id.add_friend);

        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactScreen.this, AddFriendScreen.class);
                startActivity(intent);

            }
        });


        listView = (ListView) findViewById(R.id.contact_list);
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");
        list.add("Daua");




        adapter = new ArrayAdapter(ContactScreen.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);






    }
}