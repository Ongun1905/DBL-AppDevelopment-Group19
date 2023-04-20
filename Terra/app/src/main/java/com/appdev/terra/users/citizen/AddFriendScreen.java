package com.appdev.terra.users.citizen;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.appdev.terra.R;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.UserService;
import com.appdev.terra.users.shared.utils.BottomNavBarBuilder;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddFriendScreen extends AppCompatActivity {
    ImageButton buttonBack;

    EditText contactInputField;
    EditText userIdInputField;

    UserService userService = new UserService();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_screen);
        BottomNavBarBuilder.setUpCitizenNavBar(this, R.id.contact);

        buttonBack = (ImageButton) findViewById(R.id.add_friend_back_button);
        contactInputField = findViewById(R.id.contact_input_field);
        userIdInputField = findViewById(R.id.user_id_input_field);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddFriendScreen.this, ContactScreen.class);
                startActivity(intent);

            }
        });

        Button addContactButton = findViewById(R.id.add_contact_button);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contact = contactInputField.getText().toString().trim();
                String userId = userIdInputField.getText().toString().trim();

                if (!TextUtils.isEmpty(contact) && !TextUtils.isEmpty(userId)) {
                    processInput(userId, true);
                } else if (!TextUtils.isEmpty(contact)) {
                    processInput(contact, false);
                } else if (!TextUtils.isEmpty(userId)) {
                    processInput(userId, true);
                } else {

                    Toast.makeText(AddFriendScreen.this, "Please enter contact or user ID", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void processInput(String input,boolean choice) {
        AtomicBoolean hasAdded = new AtomicBoolean(false);
        userService.getAllUsers(new IFirestoreCallback<UserModel>() {
            @Override
            public void onCallback(ArrayList<UserModel> userModels) {
                for (UserModel userModel : userModels) {
                    if (choice){
                        if(input.equals(userModel.id)) {
                            hasAdded.set(true);
                            boolean checkDuplicate = false;

                            for (ContactList contact1 : ContactScreen.contactLists) {
                                if (userModel.id.equals(contact1.id)) {
                                    checkDuplicate = true;
                                }
                            }

                            if (!checkDuplicate) {
                                userService.get(AccountService.logedInUserModel.id, new IFirestoreCallback<UserModel>() {
                                    @Override
                                    public void onCallback(UserModel model) {
                                        UserModel newModel = model;

                                        userService.get(input, new IFirestoreCallback<UserModel>() {
                                            @Override
                                            public void onCallback(UserModel personToBeAdded) {
                                                newModel.contactIds.add(personToBeAdded.id);

                                                userService.update(newModel, new IFirestoreCallback<UserModel>() {
                                                    @Override
                                                    public void onCallback(UserModel model1) {
                                                        Toast.makeText(AddFriendScreen.this, "Added: " + input, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                        }
                        }
                    }
                    else {
                        if(input.equals(Long.toString(userModel.phoneNumber))) {
                            hasAdded.set(true);
                            boolean checkDuplicate = false;

                            for (ContactList contact1 : ContactScreen.contactLists) {
                                if (userModel.phoneNumber.equals(contact1.phoneNumber)) {
                                    checkDuplicate = true;
                                }
                            }

                            if (!checkDuplicate) {
                                userService.get(AccountService.logedInUserModel.id, new IFirestoreCallback<UserModel>() {
                                    @Override
                                    public void onCallback(UserModel model) {
                                        UserModel newModel = model;

                                        userService.get(userModel.phoneNumber, new IFirestoreCallback<UserModel>() {
                                            @Override
                                            public void onCallback(UserModel personToBeAdded) {
                                                newModel.contactIds.add(personToBeAdded.id);

                                                userService.update(newModel, new IFirestoreCallback<UserModel>() {
                                                    @Override
                                                    public void onCallback(UserModel model1) {
                                                        Toast.makeText(AddFriendScreen.this, "Added: " + input, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }


                    }
                }
            }
        });



    }
}