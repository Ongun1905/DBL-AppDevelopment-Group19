package com.appdev.terra.services.IServices;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public interface IDatabaseService<T> {
    void get(String id, IFirestoreCallback firestoreCallback);
    void add(T model, IFirestoreCallback firestoreCallback);
    void update(T model, IFirestoreCallback firestoreCallback);
    void remove(String id, IFirestoreCallback firestoreCallback);
}
