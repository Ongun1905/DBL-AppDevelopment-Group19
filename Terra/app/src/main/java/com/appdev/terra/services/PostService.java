package com.appdev.terra.services;

import android.location.Location;

import androidx.annotation.NonNull;

import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
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
        userId = UserService.getUserId();
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

            Map<String, Object> geoPointPosts = document.getData();

            if (!geoPointPosts.containsKey(userId)) {
                System.out.println("User didn't have a post in this location!");
                return;
            }

            HashMap<String, Object> modelObject = (HashMap<String, Object>) geoPointPosts.get(userId);

            PostModel model = new PostModel(
                    (String)     modelObject.get("title"),
                    (String)     modelObject.get("description"),
                    (Timestamp)  modelObject.get("postedAt"),
                    (GeoPoint)   modelObject.get("location"),
                    StatusEnum.valueOf((String) modelObject.get("status"))
            );

            firestoreCallback.onCallback(model);
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
                    Map<String, Object> geoPointPosts = document.getData();

                    geoPointPosts.forEach((_senderId, _modelObject) -> {
                        HashMap<String, Object> modelObject = (HashMap<String, Object>) _modelObject;

                        PostModel model = new PostModel(
                                (String)     modelObject.get("title"),
                                (String)     modelObject.get("description"),
                                (Timestamp)  modelObject.get("postedAt"),
                                (GeoPoint)   modelObject.get("location"),
                                StatusEnum.valueOf((String) modelObject.get("status"))
                        );

                        postModels.add(model);
                    });

//                    for (Object _modelObject : geoPointPosts.values()) {
//                        HashMap<String, Object> modelObject = (HashMap<String, Object>) _modelObject;
//
//                        PostModel model = new PostModel(
//                                (String)     modelObject.get("senderId"),
//                                (String)     modelObject.get("title"),
//                                (String)     modelObject.get("description"),
//                                (Timestamp)  modelObject.get("postedAt"),
//                                (GeoPoint)   modelObject.get("location"),
//                                StatusEnum.valueOf((String) modelObject.get("status"))
//                        );
//
//                        postModels.add(model);
//                    }
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
                    HashMap<String, PostModel> geoPointPosts = new HashMap<>();
                    geoPointPosts.put(userId, model);

                    postsRef.document(model.geoId).set(geoPointPosts).addOnCompleteListener(t -> {
                        System.out.println("Post added");
                        firestoreCallback.onCallback(model);
                    });
                } else {
                    DocumentSnapshot document = documents.get(0);

                    if (!document.exists()) {
                        System.out.println("Document doesn't exist!");
                        return;
                    }

                    System.out.println(document);

                    Map<String, Object> geoPointPosts = document.getData();

                    geoPointPosts.put(userId, model);

                    postsRef.document(model.geoId).set(geoPointPosts).addOnCompleteListener(t -> {
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

                Map<String, Object> geoPointPosts = document.getData();

                if (!geoPointPosts.containsKey(userId)) {
                    System.out.println("User didn't have a post in this location!");
                    return;
                }

                Object removedPost = geoPointPosts.remove(userId);

                postsRef.document(geoPointId).set(geoPointPosts).addOnCompleteListener(t -> {
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
    private void getNearbyPosts(GeoPoint location, IFirestoreCallback firestoreCallback) {
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
}
