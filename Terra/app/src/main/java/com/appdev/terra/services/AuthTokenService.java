package com.appdev.terra.services;

import com.appdev.terra.models.AuthTokenModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AuthTokenService implements IDatabaseService<AuthTokenModel> {

    private FirebaseFirestore db;
    private CollectionReference authTokenRef;

    public AuthTokenService() {
        db = FirebaseFirestore.getInstance();
        authTokenRef = db.collection("AuthTokens");
    }

    public void get(String username, IFirestoreCallback firestoreCallback) {
        authTokenRef.whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().size() == 0) {
                    firestoreCallback.onCallback(null);
                } else {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        AuthTokenModel authTokenModel = new AuthTokenModel();
                        authTokenModel.id = document.getId();
                        authTokenModel.token = document.getString("token");
                        authTokenModel.username = document.getString("username");
                        firestoreCallback.onCallback(authTokenModel);
                    } else {
                        System.out.println("Document doesn't exist!");
                    }
                }
            } else {
                System.out.println("Task failed!");
            }
        });
    }

    @Override
    public void add(AuthTokenModel model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void update(AuthTokenModel model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {

    }
}
