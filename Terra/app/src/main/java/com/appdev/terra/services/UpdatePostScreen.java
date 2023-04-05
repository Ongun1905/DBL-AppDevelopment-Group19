package com.appdev.terra.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.ContactScreen;
import com.appdev.terra.MainActivity;
import com.appdev.terra.MyAdapter;
import com.appdev.terra.ProfileScreen;
import com.appdev.terra.R;
import com.appdev.terra.SearchScreen;
import com.appdev.terra.SpinnerUtils;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class UpdatePostScreen extends AppCompatActivity {
    private PostModel post;
    BottomNavigationView bottomNavigationView;
    private PostService postService = new PostService();
    private RecyclerView recyclerView;
    private CheckBoxAdapter adapter;

    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_update_post);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        description = findViewById(R.id.description);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        SpinnerUtils.populateStatusSpinner(this, statusSpinner);

        Bundle extras = getIntent().getExtras();
        String geoPointId = extras.getString("geoPointId");
        String userId = extras.getString("userId");

        adapter = new CheckBoxAdapter(getApplicationContext(), new CheckBoxAdapter.OnCheckBoxClickListener() {
            @Override
            public void onItemClick(CheckBox checkBox) {
                checkBox.toggle();
            }
        });

        recyclerView = findViewById(R.id.checkboxes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button verifyButton = findViewById(R.id.verify_button);

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

        // Get the post that was just clicked
        postService.getPostCollection(geoPointId, new IFirestoreCallback<PostCollection>() {
            @Override
            public void onCallback(PostCollection collection) {
                IFirestoreCallback.super.onCallback(collection);

                post = collection.getPostWithId(userId);

                post.qualifications.forEach((qualificationString, selected) -> {
                    if (selected) {
                        adapter.setQualificationBoolean(QualificationsEnum.valueOf(qualificationString), true);
                    }
                });

                description.setText(post.description);
            }
        });



        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.printBools();
            }
        });
    }
}
