package com.appdev.terra.services.IServices;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public interface IDatabaseService<T> {
    T get(String id);
    void add(T Model);
    void update(T Model);
    void remove(String id);
}
