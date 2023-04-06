package com.appdev.terra.models;

import com.appdev.terra.enums.QualificationsEnum;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserModel {
    public String id;
    public String name;
    public String surname;
    public Long phoneNumber;
    public GeoPoint address;
    public ArrayList<String> contactIds;
    public ArrayList<UserModel> contacts;
    public String password;
    public Map<String, Boolean> checkBoxes;

    public UserModel() {
        checkBoxes = new HashMap<>();
        for (QualificationsEnum qualification : QualificationsEnum.values()){
            checkBoxes.put(qualification.name(), false);
        }
    }


}