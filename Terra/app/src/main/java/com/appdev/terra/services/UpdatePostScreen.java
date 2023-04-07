package com.appdev.terra.services;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.ContactScreen;
import com.appdev.terra.HomeScreenGov;
import com.appdev.terra.MainActivity;
import com.appdev.terra.ProfileScreen;
import com.appdev.terra.R;
import com.appdev.terra.SearchScreen;
import com.appdev.terra.SpinnerUtils;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Optional;

public class UpdatePostScreen extends AppCompatActivity {
    private PostModel post;
    BottomNavigationView bottomNavigationView;
    private PostService postService = new PostService("__GOV__");
    private RecyclerView recyclerView;
    private CheckBoxAdapter adapter;
    private StatusEnum status;
    private LocationService locationService;

    private boolean verified;

    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_update_post);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        description = findViewById(R.id.description);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        SpinnerUtils.populateStatusSpinner(this, statusSpinner);
//
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

        // Get the post that was just clicked
        postService.getPostCollection(geoPointId, new IFirestoreCallback<PostCollection>() {
            @Override
            public void onCallback(PostCollection collection) {
                IFirestoreCallback.super.onCallback(collection);

                post = collection.getPostWithId(userId);

                post.qualifications.forEach((qualificationString, selected) -> {
                    adapter.setQualificationBoolean(QualificationsEnum.valueOf(qualificationString), selected);
                });

                description.setText(post.description);
            }
        });

        Button verifyButton = findViewById(R.id.verify_button);
        Button rejectButton = findViewById(R.id.reject_button);

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
        
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = StatusEnum.valueOf((String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected
            }
        });


        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostModel updatedPost = new PostModel(
                        description.getText().toString(),
                        Timestamp.now(),
                        post.location,
                        status,
                        adapter.getQualifications(),
                        true,
                        post.userId
                );

                postService.update(updatedPost, new IFirestoreCallback() {
                    @Override
                    public void onCallback() {
                        IFirestoreCallback.super.onCallback();
                    }
                });

                Intent intent = new Intent(getApplicationContext(), HomeScreenGov.class);
                startActivity(intent);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostModel updatedPost = new PostModel(
                        description.getText().toString(),
                        Timestamp.now(),
                        post.location,
                        status,
                        adapter.getQualifications(),
                        true,
                        post.userId
                );

                postService.update(updatedPost, new IFirestoreCallback() {
                    @Override
                    public void onCallback() {
                        IFirestoreCallback.super.onCallback();
                    }
                });

                Intent intent = new Intent(getApplicationContext(), HomeScreenGov.class);
                startActivity(intent);
            }
        });
    }
}
