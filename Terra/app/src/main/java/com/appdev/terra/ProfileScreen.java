package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.CheckBoxAdapter;
import com.appdev.terra.services.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private CheckBoxAdapter adapter;

    TextView userIdText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        userIdText = findViewById(R.id.textView20);
        userIdText.setText("User ID: " + AccountService.logedInUserModel.id);
        adapter = new CheckBoxAdapter(getApplicationContext(), new CheckBoxAdapter.OnCheckBoxClickListener() {
            @Override
            public void onItemClick(CheckBox checkBox) {
                checkBox.toggle();
            }
        });

        recyclerView = findViewById(R.id.checkboxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        AccountService.logedInUserModel.qualifications.forEach((qualification, selected) -> {
            adapter.setQualificationBoolean(qualification, selected);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(), ContactScreen.class));
                        overridePendingTransition(0,0);
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
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        Button saveButton = findViewById(R.id.SaveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountService.logedInUserModel.qualifications = adapter.getQualifications();
                (new UserService()).tryAdd(AccountService.logedInUserModel);
            }
        });

    }
}