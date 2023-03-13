package com.appdev.terra.services.IServices;

import com.appdev.terra.models.UserModel;

import java.util.ArrayList;

public interface IFirestoreCallback {
    default void onCallback() {

    }

    default void onCallback(UserModel model) {

    }

    default void onCallback(ArrayList<UserModel> userModels) {

    }
}
