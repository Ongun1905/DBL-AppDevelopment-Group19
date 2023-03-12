package com.appdev.terra.services;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;

import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserService implements IDatabaseService<UserModel> {

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public UserService() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
    }

    @Override
    public UserModel get(String id) {
        UserModel model = new UserModel();
        usersRef.whereEqualTo(FieldPath.documentId(), id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                    if (document.exists()) {
                        UserModel model = new UserModel();
                        model.id = document.getId();
                        model.longitude = document.getGeoPoint("address").getLongitude();
                        model.latitude = document.getGeoPoint("address").getLatitude();
                        model.name = document.getString("name");
                        model.surname = document.getString("surname");
                        model.password = document.getString("password");
                        model.phoneNumber = document.getLong("mobileNmbr");
                        model.contactIds = (ArrayList<String>) document.get("contacts");

                    } else {

                    }
                }
            }
        });
        System.out.println("id: " + model.id);
        return model;
    }

    @Override
    public void add(UserModel Model) {

    }

    @Override
    public void update(UserModel Model) {

    }

    @Override
    public void remove(String id) {

    }
}
