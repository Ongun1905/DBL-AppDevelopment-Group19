package com.appdev.terra.services;

import com.appdev.terra.models.EarthquakeModel;
import com.appdev.terra.services.IServices.IDatabaseService;

public class EarthquakeService implements IDatabaseService<EarthquakeModel> {
    @Override
    public EarthquakeModel get(String id) {
        return null;
    }

    @Override
    public void add(EarthquakeModel Model) {

    }

    @Override
    public void update(EarthquakeModel Model) {

    }

    @Override
    public void remove(String id) {

    }
}
