package com.appdev.terra.services.IServices;

import java.util.ArrayList;
public interface IFirestoreCallback<T> {
    default void onCallback() {

    }

    default void onCallback(T model) {

    }

    default void onCallback(ArrayList<T> models) {

    }

    default void onCallback(T model, boolean loginSuccess, String message) {}

    default void onCallback(boolean result, String message) {}

    default void onCallback(T model, String message) {}
}
