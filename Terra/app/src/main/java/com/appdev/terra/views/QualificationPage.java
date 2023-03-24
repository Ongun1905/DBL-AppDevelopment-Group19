package com.appdev.terra.views;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.appdev.terra.R;

import java.util.Arrays;


public class QualificationPage extends AppCompatActivity {

    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6, checkBox7;

    //Contains the list that will be sent to Server
    boolean[] boxArray = new boolean[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terra_qualifications_page);

        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        checkBox5 = findViewById(R.id.checkBox5);
        checkBox6 = findViewById(R.id.checkBox6);
        checkBox7 = findViewById(R.id.checkBox7);

        Button saveButton = findViewById(R.id.SaveButton);

        // Set an OnClickListener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check which CheckBoxes are checked and add them to the ArrayList
                boxArray[0] = false;
                if (checkBox1.isChecked()) {
                    boxArray[0] = true;
                }
                boxArray[1] = false;
                if (checkBox2.isChecked()) {
                    boxArray[1] = true;
                }
                boxArray[2] = false;
                if (checkBox3.isChecked()) {
                    boxArray[2] = true;
                }
                boxArray[3] = false;
                if (checkBox4.isChecked()) {
                    boxArray[3] = true;
                }
                boxArray[4] = false;
                if (checkBox5.isChecked()) {
                    boxArray[4] = true;
                }
                boxArray[5] = false;
                if (checkBox6.isChecked()) {
                    boxArray[5] = true;
                }
                boxArray[6] = false;
                if (checkBox7.isChecked()) {
                    boxArray[6] = true;
                }

                // Log the contents of the ArrayList to verify the data is being collected
                Log.d("TAG", "Boolean Array: " + Arrays.toString(boxArray));
            }
        });
    }
    }

