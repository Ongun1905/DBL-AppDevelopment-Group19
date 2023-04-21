package com.appdev.terra.users.shared.utils;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.appdev.terra.enums.StatusEnum;

import java.util.ArrayList;
import java.util.Arrays;

public class SpinnerUtils {
    public static void populateStatusSpinner(Context context, Spinner spinner) {
        // Define your status options as an array
        ArrayList<String> statusOptions = Arrays.stream(StatusEnum.values())
                .map(StatusEnum::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, statusOptions
        );

        // Set the drop-down layout style for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);
    }

    public static void populateStatusSpinnerWithInitialValue(
            Context context, Spinner spinner, String initial
    ) {
        // Define your status options as an array
        ArrayList<String> statusOptions = Arrays.stream(StatusEnum.values())
                .map(StatusEnum::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, statusOptions
        );

        // Set the drop-down layout style for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);

        // Set the value of the spinner to the selected initial value, or the first value
        spinner.setSelection(getIndex(spinner, initial));
    }

    public static void populateLocationSpinner(
            Context context, Spinner spinner, String currentLocation
    ) {
        // Define the status options as an array
        String[] statusOptions = {currentLocation, "Another location"};

        // Create an ArrayAdapter to populate the Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, statusOptions
        );

        // Set the drop-down layout style for the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter for the Spinner
        spinner.setAdapter(adapter);
    }

    private static int getIndex(Spinner spinner, String myString){
        for (int i = 0; i < spinner.getCount() ; i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }
}

