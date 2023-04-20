package com.appdev.terra.users.citizen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appdev.terra.R;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.users.shared.CheckBoxAdapter;
import com.appdev.terra.services.UserService;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;

public class ProfileScreen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CheckBoxAdapter adapter;

    TextView userIdText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.profile);

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