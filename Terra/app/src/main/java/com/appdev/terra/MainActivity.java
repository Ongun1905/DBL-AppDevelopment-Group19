package com.appdev.terra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.GeoPoint;

import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    // Services
    PostService postService = new PostService();

    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        //UI Navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.sos);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(), ContactScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.sos:
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

        Button sendButton = (Button) findViewById(R.id.button);
        Button sosButton = (Button) findViewById(R.id.sos_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

                if (userLocationOption == null) {
                    Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userLocationOption.isPresent()) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                postService.getNearbyPostCollections(userLocationOption.get(), new IFirestoreCallback<PostCollection>() {
                    @Override
                    public void onCallback(ArrayList<PostCollection> collections) {
                        IFirestoreCallback.super.onCallback(collections);

                        for (PostCollection collection : collections) {
                            System.out.println(collection.getLocation());
                        }
                    }
                });

//                postService.get(PostModel.makeGeoId(userLocationOption.get()), new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(PostModel model) {
//                        IFirestoreCallback.super.onCallback(model);
//
//                        System.out.println("Found a post! It has as description:\n" + model.description);
//                    }
//                });

//                postService.remove(PostModel.makeGeoId(userLocationOption.get()), new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(PostModel model) {
//                        IFirestoreCallback.super.onCallback(model);
//
//                        System.out.println("Removed post with description:\n" + model.description);
//                    }
//                });

//                postService.getAllPosts(new IFirestoreCallback<PostModel>() {
//                    @Override
//                    public void onCallback(ArrayList<PostModel> models) {
//                        IFirestoreCallback.super.onCallback(models);
//
//                        for (PostModel model : models) {
//                            System.out.println(model.description);
//                        }
//                    }
//                });
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Optional<GeoPoint> userLocationOption = locationService.getGeoPoint();

                if (userLocationOption == null) {
                    Toast.makeText(getApplicationContext(), "Location permissions not granted!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userLocationOption.isPresent()) {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                PostModel post = PostModel.sosPost(userLocationOption.get());
                postService.add(post, new IFirestoreCallback<PostModel>() {});
            }
        });
    }
}