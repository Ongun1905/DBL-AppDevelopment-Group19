package com.appdev.terra.users.government;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appdev.terra.users.shared.CheckBoxAdapter;
import com.appdev.terra.services.PostService;
import com.appdev.terra.R;
import com.appdev.terra.users.shared.SpinnerUtils;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.helpers.LocationService;
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.google.firebase.Timestamp;

public class UpdatePostScreen extends AppCompatActivity {
    private PostModel post;
    private PostService postService = new PostService("__GOV__");
    private RecyclerView recyclerView;
    private CheckBoxAdapter adapter;
    private StatusEnum status;
    private LocationService locationService;

    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        set the layout of the corresponding page
        setContentView(R.layout.activity_government_update_post);
//        set the corresponding navigation bar
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.home);
//        get the attributes of the post into the specified fields
        locationService = new LocationService(
                (LocationManager) getSystemService(Context.LOCATION_SERVICE),
                this,
                this
        );

        description = findViewById(R.id.description);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);

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

                SpinnerUtils.populateStatusSpinnerWithInitialValue(getApplicationContext(), statusSpinner, post.status.toString());
            }
        });

        Button verifyButton = findViewById(R.id.verify_button);
        Button rejectButton = findViewById(R.id.reject_button);
        
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

//      Update post with the new attributes and verify after pressing verify button
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

//        On reject update post but keep the verified field false
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
