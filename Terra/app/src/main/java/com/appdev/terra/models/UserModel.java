package com.appdev.terra.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public String id;
    public String name;
    public String surname;
    public Long phoneNumber;
    public GeoPoint address;
    public ArrayList<String> contactIds;
    public String password;

}
