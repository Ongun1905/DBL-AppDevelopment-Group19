package com.appdev.terra.services;

import com.appdev.terra.models.PostModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;

public class PostService implements IDatabaseService<PostModel> {

    @Override
    public void get(String id, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void add(PostModel Model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void update(PostModel Model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {

    }
}
