package com.appdev.terra.models;

import com.appdev.terra.enums.QualificationsEnum;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

public class UserModel {
    public String id;
    public String name;
    public String surname;
    public Long phoneNumber;
    public GeoPoint address;
    public ArrayList<String> contactIds;
    public ArrayList<UserModel> contacts;
    public String password;
    public HashMap<QualificationsEnum, Boolean> qualifications = new HashMap<>();

    public static UserModel fromFirebaseDocument(DocumentSnapshot document) {
        HashMap<String, Object> _qualificationsMap = (HashMap<String, Object>) document.getData().get("qualifications");
        HashMap<QualificationsEnum, Boolean> qualificationsMap = new HashMap<>();

        _qualificationsMap.forEach((qualification, selected) -> {
            qualificationsMap.put(QualificationsEnum.valueOf(qualification), (Boolean) selected);
        });

        UserModel result = new UserModel();
        result.id = document.getId();
        result.name = document.getString("name");
        result.surname = document.getString("surname");
        result.phoneNumber = document.getLong("mobileNmbr");
        result.address = document.getGeoPoint("address");
        result.password = document.getString("password");
        result.contactIds = (ArrayList<String>) document.get("contacts");
        result.contacts = new ArrayList<>();
//        for (String i : result.contactIds) {
//            userService.get(i, new IFirestoreCallback<UserModel>() {
//                @Override
//                public void onCallback(UserModel friend) {
//                    result.contacts.add(friend);
//                }
//            });
//        }

        result.qualifications = qualificationsMap;

        return result;
    }

    public HashMap<String, Object> toFirebaseUserModel() {
        HashMap<String, Object> user = new HashMap<>();
        HashMap<String, Boolean> qualifications = new HashMap<>();

        user.put("address", this.address);
        user.put("contacts", this.contactIds);
        user.put("mobileNmbr", this.phoneNumber);
        user.put("name", this.name);
        user.put("surname", this.surname);
        user.put("password", this.password);
        this.qualifications.forEach((qualification, selected) -> {
            qualifications.put(qualification.toString(), selected);
        });
        user.put("qualifications", qualifications);

        return user;
    }
}
