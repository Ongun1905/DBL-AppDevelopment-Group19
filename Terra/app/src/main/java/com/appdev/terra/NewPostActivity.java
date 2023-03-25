package com.appdev.terra;

import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class NewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newpost);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        SpinnerUtils.populateStatusSpinner(this, statusSpinner);
    }
}

