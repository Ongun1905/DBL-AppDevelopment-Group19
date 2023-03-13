package com.appdev.terra.services;

import android.content.ContentValues;
import android.util.Log;

import androidx.annotation.NonNull;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.helpers.Container;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserService implements IDatabaseService<UserModel> {

    private FirebaseFirestore db;
    private CollectionReference usersRef;

    public UserService() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");
    }


    // do not use
    @Override
    public void get(String id, IFirestoreCallback firestoreCallback) {
        usersRef.whereEqualTo(FieldPath.documentId(), id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document.exists()) {
                    UserModel model = new UserModel();
                    model.id = document.getId();
                    model.name = document.getString("name");
                    model.address = document.getGeoPoint("address");
                    model.surname = document.getString("surname");
                    model.password = document.getString("password");
                    model.phoneNumber = document.getLong("mobileNmbr");
                    model.contactIds = (ArrayList<String>) document.get("contacts");
                    firestoreCallback.onCallback(model);
                } else {
                    System.out.println("Document doesn't exist!");
                }
            } else {
                System.out.println("Task failed!");
            }

        });
    }

    public void getAllUsers(IFirestoreCallback firestoreCallback) {
        usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<UserModel> userModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        UserModel model = new UserModel();
                        model.id = document.getId();
                        model.name = document.getString("name");
                        model.address = document.getGeoPoint("address");
                        model.surname = document.getString("surname");
                        model.password = document.getString("password");
                        model.phoneNumber = document.getLong("mobileNmbr");
                        model.contactIds = (ArrayList<String>) document.get("contacts");
                        userModels.add(model);
                    }
                    firestoreCallback.onCallback(userModels);
                } else {
                    System.out.println("Task failed!");
                }
            }
        });
    }

    @Override
    public void add(UserModel model, IFirestoreCallback firestoreCallback) {
        Map<String, Object> user = new HashMap<>();
        user.put("address", model.address);
        user.put("contacts", model.contactIds);
        user.put("mobileNmbr", model.phoneNumber);
        user.put("name", model.name);
        user.put("surname", model.surname);
        user.put("password", model.password);

        usersRef.document().set(user).addOnCompleteListener(task -> {
            System.out.println("User added");
            firestoreCallback.onCallback(model);
        });
    }

    @Override
    public void update(UserModel model, IFirestoreCallback firestoreCallback) {
        usersRef.document(model.id).update("address", model.address,
                "contacts", model.contactIds,
                "mobileNmbr", model.phoneNumber,
                "password", model.password,
                "name", model.name,
                "surname", model.surname).addOnCompleteListener(task -> {
                    System.out.println("User updated!");
                    firestoreCallback.onCallback(model);
                });

    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {
        usersRef.document(id).delete().addOnSuccessListener(unused -> {
            System.out.println("User deleted!");
            firestoreCallback.onCallback();
        });
    }
}
