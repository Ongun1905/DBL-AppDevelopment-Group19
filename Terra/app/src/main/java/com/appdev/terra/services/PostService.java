package com.appdev.terra.services;

import android.location.Location;

import androidx.annotation.NonNull;

import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.helpers.PostCollection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostService implements IDatabaseService<PostModel> {

    private FirebaseFirestore db;
    private CollectionReference postsRef;
    private final String userId;

    public PostService() {
        db = FirebaseFirestore.getInstance();
        postsRef = db.collection("Posts");
        userId = AccountService.logedInUserModel.id;
    }

    public PostService(String userId) {
        db = FirebaseFirestore.getInstance();
        postsRef = db.collection("Posts");
        this.userId = userId;
    }

    @Override
    public void get(String geoPointId, IFirestoreCallback firestoreCallback) {
        postsRef.whereEqualTo(FieldPath.documentId(), geoPointId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("Task failed!");
                return;
            }

            DocumentSnapshot document = task.getResult().getDocuments().get(0);

            if (!document.exists()) {
                System.out.println("Document doesn't exist!");
                return;
            }

            this.getPostCollection(geoPointId, new IFirestoreCallback<PostCollection>() {
                @Override
                public void onCallback(PostCollection collection) {
                    IFirestoreCallback.super.onCallback(collection);
                    PostModel post = collection.getPostWithId(userId);

                    if (post == null) {
                        System.out.println("User didn't have a post in this location!");
                        return;
                    }

                    firestoreCallback.onCallback(post);
                }
            });
        });
    }

    public void getAllPosts(IFirestoreCallback firestoreCallback) {
        postsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Task failed!");
                    return;
                }

                ArrayList<PostModel> postModels = new ArrayList<>();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    PostCollection collection = PostCollection.fromFirebaseDocument(document);
                    postModels.addAll(collection.getPosts());
                }

                firestoreCallback.onCallback(postModels);
            }
        });
    }

    @Override
    public void add(PostModel model, IFirestoreCallback firestoreCallback) {
        postsRef.whereEqualTo(FieldPath.documentId(), model.geoId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Task failed!");
                    return;
                }

                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                if (documents.size() == 0) {
                    PostCollection newCollection = new PostCollection(model.location, model.status);

                    newCollection.addPost(userId, model);

                    postsRef.document(model.geoId).set(newCollection.toFirebasePostCollection()).addOnCompleteListener(t -> {
                        System.out.println("Post added");
                        firestoreCallback.onCallback(model);
                    });
                } else {
                    DocumentSnapshot document = documents.get(0);

                    if (!document.exists()) {
                        System.out.println("Document doesn't exist!");
                        return;
                    }

                    PostCollection collection = PostCollection.fromFirebaseDocument(document);

                    collection.addPost(model.userId, model);

                    postsRef.document(model.geoId).set(collection.toFirebasePostCollection()).addOnCompleteListener(t -> {
                        System.out.println("Post added");
                        firestoreCallback.onCallback(model);
                    });
                }
            }
        });
    }

    @Override
    public void update(PostModel model, IFirestoreCallback firestoreCallback) {
        add(model, firestoreCallback);
    }

    @Override
    public void remove(String geoPointId, IFirestoreCallback firestoreCallback) {
        postsRef.whereEqualTo(FieldPath.documentId(), geoPointId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Task failed!");
                    return;
                }

                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                if (!document.exists()) {
                    System.out.println("Document doesn't exist!");
                    return;
                }

                PostCollection collection = PostCollection.fromFirebaseDocument(document);
                PostModel removedPost = collection.removePost(userId);

                if (removedPost == null) {
                    System.out.println("User didn't have a post in this location!");
                    return;
                }

                postsRef.document(geoPointId).set(collection.toFirebasePostCollection()).addOnCompleteListener(t -> {
                    System.out.println("Post added");
                    firestoreCallback.onCallback(removedPost);
                });
            }
        });
    }

    //checks if there are any nearby posts or not
    private void nearbyPostsExists(GeoPoint location, IFirestoreCallback firestoreCallback) {
        getAllPosts(new IFirestoreCallback<PostModel>() {
            @Override
            public void onCallback(ArrayList<PostModel> models) {
            for (PostModel model : models) {
                float[] results = new float[1];
                Location.distanceBetween(location.getLatitude(), location.getLongitude(), model.location.getLatitude(), model.location.getLongitude(), results);
                if (results[0] < 4000) {
                    firestoreCallback.onCallback(true, "Nearby posts exist!");
                }
            }
            firestoreCallback.onCallback(false, "No nearby posts exist!");
            }
        });
    }

    // returns all the nearby posts if exists
    public void getNearbyPosts(GeoPoint location, IFirestoreCallback firestoreCallback) {
        getAllPosts(new IFirestoreCallback<PostModel>() {
            @Override
            public void onCallback(ArrayList<PostModel> models) {
                ArrayList<PostModel> postModels = new ArrayList<>();
                for (PostModel model : models) {
                    float[] results = new float[1];
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), model.location.getLatitude(), model.location.getLongitude(), results);
                    if (results[0] < 4000) {
                        postModels.add(model);
                    }
                }
                firestoreCallback.onCallback(postModels);
            }
        });
    }

    // Gets all threads of nearby posts
    public void getNearbyPostCollections(GeoPoint location, IFirestoreCallback firestoreCallback) {
        postsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    System.out.println("Task failed!");
                    return;
                }

                ArrayList<PostCollection> postCollections = new ArrayList<>();
                float[] results = new float[1];

                for (QueryDocumentSnapshot document : task.getResult()) {
                    PostCollection newCollection = PostCollection.fromFirebaseDocument(document);
                    Location.distanceBetween(location.getLatitude(), location.getLongitude(), newCollection.getLatitude(), newCollection.getLongitude(), results);
                    if (results[0] < 4000) {
                        postCollections.add(newCollection);
                    }
                }

                firestoreCallback.onCallback(postCollections);
            }
        });
    }


    public void getPostCollection(String geoPointId, IFirestoreCallback firestoreCallback) {
        postsRef.whereEqualTo(FieldPath.documentId(), geoPointId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                System.out.println("Task failed!");
                return;
            }

            DocumentSnapshot document = task.getResult().getDocuments().get(0);

            if (!document.exists()) {
                System.out.println("Document doesn't exist!");
                return;
            }

            PostCollection collection = PostCollection.fromFirebaseDocument(document);

            firestoreCallback.onCallback(collection);
        });
    }
}
