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

    public PostModel(
            String id, String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.location = location;
        this.status = status;
    }

    public PostModel(
            String id, String title, String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.location = new GeoPoint(latitude, longitude);
        this.status = status;
    }

    public static PostModel sosPost(String id, GeoPoint location) {
        return PostModel.sosPost(id, location.getLatitude(), location.getLongitude());
    }

    public static PostModel sosPost(String id, double latitude, double longitude) {
        Timestamp now = Timestamp.now();
        return new PostModel(
                id,
                "[SOS] New emergency!!",
                "Emergency reported through use of the SOS button. " +
                        "Location: [lat: " + latitude + ", lon: " + longitude + "]. " +
                        "Posted at: " + now.toDate(),
                now,
                new GeoPoint(latitude, longitude),
                StatusEnum.NEED_EQUIPMENT
        );
    }
}
