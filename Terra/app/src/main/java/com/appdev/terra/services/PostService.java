package com.appdev.terra.services;

import androidx.annotation.NonNull;

import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.PostModel;
import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostService implements IDatabaseService<PostModel> {

    private FirebaseFirestore db;
    private CollectionReference postsRef;

    public PostService() {
        db = FirebaseFirestore.getInstance();
        postsRef = db.collection("Posts");
    }

    @Override
    public void get(String id, IFirestoreCallback firestoreCallback) {
        postsRef.whereEqualTo(FieldPath.documentId(), id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document.exists()) {
                    PostModel model = new PostModel(
                            document.getString("Title"),
                            document.getString("Description"),
                            document.getTimestamp("PostedAt"),
                            document.getGeoPoint("Location"),
                            StatusEnum.valueOf(document.getString("Status"))
                    );

                    firestoreCallback.onCallback(model);
                } else {
                    System.out.println("Document doesn't exist!");
                }
            } else {
                System.out.println("Task failed!");
            }

        });
    }

    public void getAllPosts(IFirestoreCallback firestoreCallback) {
        postsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<PostModel> postModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        PostModel model = new PostModel(
                                document.getString("Title"),
                                document.getString("Description"),
                                document.getTimestamp("PostedAt"),
                                document.getGeoPoint("Location"),
                                StatusEnum.valueOf(document.getString("Status"))
                        );

                        postModels.add(model);
                    }
                    firestoreCallback.onCallback(postModels);
                } else {
                    System.out.println("Task failed!");
                }
            }
        });
    }

    @Override
    public void add(PostModel model, IFirestoreCallback firestoreCallback) {
        Map<String, Object> post = new HashMap<>();
        post.put("Description", model.description);
        post.put("Location", model.location);
        post.put("PostedAt", model.postedAt);
        post.put("Title", model.title);
        post.put("Status", model.status);

        postsRef.document(model.id).set(post).addOnCompleteListener(task -> {
            System.out.println("Post added");
            firestoreCallback.onCallback(model);
        });
    }

    @Override
    public void update(PostModel model, IFirestoreCallback firestoreCallback) {
        postsRef.document(model.id).update("Description", model.description,
                "Location", model.location,
                "PostedAt", model.postedAt,
                "Title", model.title,
                "Status", model.status).addOnCompleteListener(task -> {
            System.out.println("Post updated!");
            firestoreCallback.onCallback(model);
        });
    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {
        postsRef.document(id).delete().addOnSuccessListener(unused -> {
            System.out.println("Post deleted!");
            firestoreCallback.onCallback();
        });
    }
}
