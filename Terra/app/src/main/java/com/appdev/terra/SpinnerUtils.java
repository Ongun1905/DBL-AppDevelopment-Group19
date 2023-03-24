package com.appdev.terra;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SpinnerUtils {
    public static void populateStatusSpinner(Context context, Spinner spinner) {
        // Define your status options as an array
        String[] statusOptions = {"Urgent (Life or Death)", "In Need of Help (Secondary Resources Needed)", "Resolved"};

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, statusOptions);

        // Set the drop-down layout style for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);
    }
}

