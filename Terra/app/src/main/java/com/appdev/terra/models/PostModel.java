package com.appdev.terra.models;

import com.appdev.terra.enums.StatusEnum;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class PostModel {
    public String id;
    public String title;
    public String description;
    public Timestamp postedAt;
    public GeoPoint location;
    public StatusEnum status;
}
