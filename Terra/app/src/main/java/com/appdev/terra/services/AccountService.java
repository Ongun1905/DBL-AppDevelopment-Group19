package com.appdev.terra.services;

import com.appdev.terra.models.AuthTokenModel;
import com.appdev.terra.models.RegisterModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountService {

    public static UserModel logedInUserModel;
    public static boolean isAuthorized = false;

    public static boolean nearbyNotificationSent = false;
    UserService userService;
    AuthTokenService authTokenService;

    public AccountService() {
        userService = new UserService();
        authTokenService = new AuthTokenService();
    }


    public void login(Long phoneNumber, String password, IFirestoreCallback firestoreCallback) {
        userService.get(phoneNumber, new IFirestoreCallback<UserModel>() {
            @Override
            public void onCallback(UserModel model) {
                System.out.println("bok");
                if (model == null) {
                    firestoreCallback.onCallback(model, false, "The user doesn't exist!");
                } else {
                    if (password.equals(model.password)) {
                        logedInUserModel = model;
                        isAuthorized = false;
                        firestoreCallback.onCallback(model, true, "Login sucessful!");
                    } else {
                        firestoreCallback.onCallback(model, false, "Incorrect password!");
                    }
                }
            }
        });


    }


    // Only for authorized users
    public void validateToken(String username, String token, IFirestoreCallback firestoreCallback) {
        authTokenService.get(username, new IFirestoreCallback<AuthTokenModel>() {
            @Override
            public void onCallback(AuthTokenModel model) {
                System.out.println(model.token + model.username);
                if (model == null) {
                    firestoreCallback.onCallback(model,false, "User with the username not found!");
                } else {
                    if (token.equals(model.token)) {
                        isAuthorized = true;
                        firestoreCallback.onCallback(model, true, "Login sucessful!");
                    } else {
                        firestoreCallback.onCallback(model, false, "Invalid access token!");
                    }
                }
            }
        });
    }
}
