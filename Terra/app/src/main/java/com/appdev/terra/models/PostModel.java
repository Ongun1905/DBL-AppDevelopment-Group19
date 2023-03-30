package com.appdev.terra.models;

import com.appdev.terra.enums.StatusEnum;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;

public class PostModel {
    // 6 characters, 2 decimals: ???.??
    private static final String GEO_POINT_INDEX_FORMAT = "[%06.2f, %06.2f]";

    public String geoId;
    public String title;
    public String description;
    public Timestamp postedAt;
    public GeoPoint location;
    public StatusEnum status;

    public PostModel(
            String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status
    ) {
        this.geoId = makeGeoId(location.getLatitude(), location.getLongitude());
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.location = location;
        this.status = status;
    }

    public PostModel(
            String title, String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status
    ) {
        this.geoId = makeGeoId(latitude, longitude);
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.location = new GeoPoint(latitude, longitude);
        this.status = status;
    }

    public static PostModel fromHashMap(HashMap<String, Object> map) {
        return new PostModel(
                (String)     map.get("title"),
                (String)     map.get("description"),
                (Timestamp)  map.get("postedAt"),
                (GeoPoint)   map.get("location"),
                StatusEnum.valueOf((String) map.get("status"))
        );
    }

    public static PostModel sosPost(GeoPoint location) {
        return PostModel.sosPost(location.getLatitude(), location.getLongitude());
    }

    public static PostModel sosPost(double latitude, double longitude) {
        Timestamp now = Timestamp.now();
        return new PostModel(
                "[SOS] New emergency!!",
                "Emergency reported through use of the SOS button. " +
                        "Location: [lat: " + latitude + ", lon: " + longitude + "]. " +
                        "Posted at: " + now.toDate(),
                now,
                latitude,
                longitude,
                StatusEnum.NEED_EQUIPMENT
        );
    }

    public static String makeGeoId(double latitude, double longitude) {
        return String.format(GEO_POINT_INDEX_FORMAT, latitude, longitude);
    }
}
