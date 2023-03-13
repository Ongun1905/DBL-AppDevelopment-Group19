package com.appdev.terra.models;

import com.appdev.terra.enums.StatusEnum;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class PostModel {
    String id;
    String title;
    String description;
    Timestamp postedAt;
    public GeoPoint location;
    StatusEnum status;
}
