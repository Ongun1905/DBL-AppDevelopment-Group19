package com.appdev.terra.services;

import androidx.annotation.NonNull;

import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.models.EarthquakeModel;
import com.appdev.terra.models.PostModel;
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

public class EarthquakeService implements IDatabaseService<EarthquakeModel> {

    private FirebaseFirestore db;
    private CollectionReference earthquakesRef;

    public EarthquakeService() {
        db = FirebaseFirestore.getInstance();
        earthquakesRef = db.collection("Earthquakes");
    }

    @Override
    public void get(String id, IFirestoreCallback firestoreCallback) {
        earthquakesRef.whereEqualTo(FieldPath.documentId(), id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                if (document.exists()) {
                    EarthquakeModel model = new EarthquakeModel();
                    model.id = document.getId();
                    model.location = document.getGeoPoint("Location");
                    model.magnitude = document.getDouble("Magnitude");
                    firestoreCallback.onCallback(model);
                } else {
                    System.out.println("Document doesn't exist!");
                }
            } else {
                System.out.println("Task failed!");
            }

        });
    }

    public void getAllEarthquakes(IFirestoreCallback firestoreCallback) {
        earthquakesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<EarthquakeModel> earthquakeModels = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        EarthquakeModel model = new EarthquakeModel();
                        model.id = document.getId();
                        model.location = document.getGeoPoint("Location");
                        model.magnitude = document.getDouble("Magnitude");
                        earthquakeModels.add(model);
                    }
                    firestoreCallback.onCallback(earthquakeModels);
                } else {
                    System.out.println("Task failed!");
                }
            }
        });
    }

    @Override
    public void add(EarthquakeModel model, IFirestoreCallback firestoreCallback) {
        Map<String, Object> earthquake = new HashMap<>();
        earthquake.put("Magnitude", model.magnitude);
        earthquake.put("Location", model.location);

        earthquakesRef.document().set(earthquake).addOnCompleteListener(task -> {
            System.out.println("Earthquake alert added.");
            firestoreCallback.onCallback(model);
        });
    }

    @Override
    public void update(EarthquakeModel model, IFirestoreCallback firestoreCallback) {
        earthquakesRef.document(model.id).update("Magnitude", model.magnitude,
                "Location", model.location).addOnCompleteListener(task -> {
            System.out.println("Earthquake alert updated!");
            firestoreCallback.onCallback(model);
        });
    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {
        earthquakesRef.document(id).delete().addOnSuccessListener(unused -> {
            System.out.println("Earthquake alert deleted!");
            firestoreCallback.onCallback();
        });
    }
}
