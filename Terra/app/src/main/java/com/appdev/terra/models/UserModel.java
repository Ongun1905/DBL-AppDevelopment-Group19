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
    public ArrayList<UserModel> contacts;
    public String password;

    public boolean checkBox1;
    public boolean checkBox2;
    public boolean checkBox3;
    public boolean checkBox4;
    public boolean checkBox5;
    public boolean checkBox6;
    public boolean checkBox7;


}
