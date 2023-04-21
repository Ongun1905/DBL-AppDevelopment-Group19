package com.appdev.terra.users.government.activities;

import android.content.Intent;
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
import com.appdev.terra.services.helpers.PostCollection;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;
import com.google.firebase.Timestamp;

public class UpdatePostGovAc extends AppCompatActivity {
    // Services
    private PostService postService = new PostService("__GOV__");


    // Helper objects
    private PostModel post;
    private StatusEnum status;
    private CheckBoxAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_government_update_post);

        // Link all GUI elements
        EditText description = findViewById(R.id.description);
        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        RecyclerView checkBoxList = findViewById(R.id.checkboxes);
        Button verifyButton = findViewById(R.id.verify_button);
        Button rejectButton = findViewById(R.id.reject_button);

        // Set up the navigation bar menu at the bottom of the page
        BottomNavBarBuilder.setUpGovernmentNavBar(this, R.id.home);

        // Instantiate the adapter for the qualifications checkbox list
        adapter = new CheckBoxAdapter(
                getApplicationContext(),
                new CheckBoxAdapter.OnCheckBoxClickListener() {
                    @Override
                    public void onItemClick(CheckBox checkBox) {
                        checkBox.toggle();
                    }
                }
        );

        // Set up the list of qualification checkboxes
        checkBoxList.setLayoutManager(new LinearLayoutManager(this));
        checkBoxList.setAdapter(adapter);

        // Get the post that was just clicked
        Bundle extras = getIntent().getExtras();
        String geoPointId = extras.getString("geoPointId");
        String userId = extras.getString("userId");

        postService.getPostCollection(geoPointId, new IFirestoreCallback<PostCollection>() {
            @Override
            public void onCallback(PostCollection collection) {
                IFirestoreCallback.super.onCallback(collection);

                post = collection.getPostWithId(userId);

                post.qualifications.forEach((qualificationString, selected) -> {
                    adapter.setQualificationBoolean(
                            QualificationsEnum.valueOf(qualificationString),
                            selected
                    );
                });

                description.setText(post.description);

                SpinnerUtils.populateStatusSpinnerWithInitialValue(
                        getApplicationContext(),
                        statusSpinner,
                        post.status.toString()
                );
            }
        });

        // Set the status of the original post as the status in the dropdown menu
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

        // Make buttons update the post with the appropriate `verified` status
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost(description.getText().toString(), true);
            }
        });

        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePost(description.getText().toString(), false);
            }
        });
    }

    // TODO: Document that crap
    private void updatePost(String description, boolean verified) {
        PostModel updatedPost = new PostModel(
                description,
                Timestamp.now(),
                post.location,
                status,
                adapter.getQualifications(),
                verified,
                post.userId
        );

        postService.update(updatedPost, new IFirestoreCallback() {
            @Override
            public void onCallback() {
                IFirestoreCallback.super.onCallback();
            }
        });

        Intent intent = new Intent(getApplicationContext(), PostCollectionFeedGovAc.class);
        startActivity(intent);
    }
}
