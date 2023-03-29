package com.appdev.terra.services;

import com.appdev.terra.models.RegisterModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private CollectionReference tokenRef;

    public static String logedInUserId;

    public AccountService() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
        tokenRef = db.collection("AuthTokens");
    }

    UserService userService = new UserService();
    public void login(Long phoneNumber, String password, IFirestoreCallback firestoreCallback) {
        userService.get(phoneNumber, new IFirestoreCallback<UserModel>() {
            @Override
            public void onCallback(UserModel model) {
                if (password == model.password) {
                    firestoreCallback.onCallback(model, true, "Login sucessful!");
                    logedInUserId = model.id;
                } else {
                    firestoreCallback.onCallback(model, false, "Password doesn't match!");
                }
            }
        });
    }

    public void register(RegisterModel model, IFirestoreCallback firestoreCallback) {
        userService.get(model.phoneNumber, new IFirestoreCallback<UserModel>() {
            @Override
            public void onCallback(UserModel model1) {
                if (model1 == null) {
                    if (model.password == model.confirmPassword ) {
                        Map<String, Object> user = new HashMap<>();
                        user.put("address", model.address);
                        user.put("mobileNmbr", model.phoneNumber);
                        user.put("name", model.name);
                        user.put("surname", model.surname);
                        user.put("password", model.password);

                        usersRef.document().set(user).addOnCompleteListener(task -> {
                            System.out.println("User added");
                            firestoreCallback.onCallback(model, true, "Register Successful!");
                        });
                    } else {
                        firestoreCallback.onCallback(model, false, "Confirm password and password doesn't match!");
                    }
                } else {
                    firestoreCallback.onCallback(model, false, "A user with the same number exists!");
                }
            }
        });
    }

    // Only for authorized users
    public void validateToken(String username, String token, IFirestoreCallback firestoreCallback) {
        tokenRef.whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document.exists()) {
                    if (document.getString("token") == token) {
                        firestoreCallback.onCallback(true, "Successful login.");
                    } else {
                        firestoreCallback.onCallback(false, "Invalid access token!");
                    }
                } else {
                    firestoreCallback.onCallback(false, "User with the username not found!");
                    System.out.println("Document doesn't exist!");
                }
            } else {
                System.out.println("Task failed!");
            }
        });
    }
}
