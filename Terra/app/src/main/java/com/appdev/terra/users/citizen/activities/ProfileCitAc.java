package com.appdev.terra.users.citizen.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appdev.terra.R;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.users.shared.utils.CheckBoxVHAdapter;
import com.appdev.terra.services.UserService;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;

public class ProfileCitAc extends AppCompatActivity {
    // Helper objects
    private CheckBoxVHAdapter checkBoxAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        // Link all GUI elements
        RecyclerView checkBoxList = findViewById(R.id.checkboxes);
        TextView userIdText = findViewById(R.id.textView20);
        Button saveButton = findViewById(R.id.SaveButton);

        // Set up the navigation bar menu at the bottom of the page
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.profile);

        // Instantiate the adapter for the qualifications checkbox list
        checkBoxAdapter = new CheckBoxVHAdapter(getApplicationContext(), CheckBox::toggle);

        // Set up the list of qualification checkboxes
        checkBoxList.setLayoutManager(new LinearLayoutManager(this));
        checkBoxList.setAdapter(checkBoxAdapter);

        // Set qualifications as selected if the user previously selected them
        AccountService.logedInUserModel.qualifications.forEach((qualification, selected) ->
            checkBoxAdapter.setQualificationBoolean(qualification, selected)
        );

        // Display the user ID
        userIdText.setText(String.format("User ID: %s", AccountService.logedInUserModel.id));

        // Set the save button to send the database the newly (de)selected qualifications
        saveButton.setOnClickListener(view -> {
            AccountService.logedInUserModel.qualifications =
                    checkBoxAdapter.getQualifications();
            (new UserService()).tryAdd(AccountService.logedInUserModel);
        });
    }
}