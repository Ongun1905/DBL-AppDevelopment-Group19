package com.appdev.terra.services.IServices;

import com.appdev.terra.models.UserModel;

import java.util.ArrayList;

public interface IFirestoreCallback<T> {
    default void onCallback() {

    }

    default void onCallback(T model) {

    }

    default void onCallback(ArrayList<T> userModels) {

    }

    default void onCallback(T model, boolean loginSuccess, String message) {}
}
