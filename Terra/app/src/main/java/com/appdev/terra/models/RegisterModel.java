package com.appdev.terra.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RegisterModel {
    public String userId;
    public String name;
    public String surname;
    public Long phoneNumber;
    public GeoPoint address;
    public String password;
    public String confirmPassword;
}
