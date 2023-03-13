package com.appdev.terra.services;

import com.appdev.terra.models.EarthquakeModel;
import com.appdev.terra.services.IServices.IDatabaseService;
import com.appdev.terra.services.IServices.IFirestoreCallback;

public class EarthquakeService implements IDatabaseService<EarthquakeModel> {

    @Override
    public void get(String id, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void add(EarthquakeModel Model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void update(EarthquakeModel Model, IFirestoreCallback firestoreCallback) {

    }

    @Override
    public void remove(String id, IFirestoreCallback firestoreCallback) {

    }
}
