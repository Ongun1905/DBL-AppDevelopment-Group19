package com.appdev.terra.models;

import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.PostService;
import com.appdev.terra.services.UserService;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
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
    public HashMap<String, Boolean> qualifications = new HashMap<>();
    public boolean verified;

    public final String userId;

    public PostModel(
            String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications
    ) {
        this(title, description, postedAt, location, status, qualifications, false, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified
    ) {
        this(title, description, postedAt, location, status, qualifications, verified, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String title, String description,
            Timestamp postedAt, double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications
    ) {
        this(title, description, postedAt, latitude, longitude, status, qualifications, false, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, String userId
    ) {
        this(title, description, postedAt, location, status, qualifications, false, userId);
    }

    public PostModel(
            String title, String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, String userId
    ) {
        this(title, description, postedAt, latitude, longitude, status, qualifications, false, userId);
    }

    public PostModel(
            String title, String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified, String userId
    ) {
        this(title, description, postedAt, location.getLatitude(), location.getLongitude(), status, qualifications, verified, userId);
    }

    private PostModel(
            String title, String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified, String userId
    ) {
        this.geoId = makeGeoId(latitude, longitude);
        this.title = title;
        this.description = description;
        this.postedAt = postedAt;
        this.location = new GeoPoint(latitude, longitude);
        this.status = status;
        this.verified = verified;
        this.userId = userId;

        qualifications.forEach((qualification, selected) -> {
            this.qualifications.put(qualification.toString(), selected);
        });
    }

    public static PostModel fromHashMap(HashMap<String, Object> map, String userId) {
        HashMap<QualificationsEnum, Boolean> qualifications = new HashMap<>();
        HashMap<String, Boolean> qualificationsStrings = (HashMap<String, Boolean>) map.get("qualifications");

        qualificationsStrings.forEach((qualificationString, selected) -> {
            qualifications.put(QualificationsEnum.valueOf(qualificationString), selected);
        });

        return new PostModel(
                (String)                    map.get("title"),
                (String)                    map.get("description"),
                (Timestamp)                 map.get("postedAt"),
                (GeoPoint)                  map.get("location"),
                StatusEnum.valueOf((String) map.get("status")),
                qualifications,
                (boolean)                   map.get("verified"),
                userId
        );
    }

    public static PostModel sosPost(GeoPoint location) {
        return PostModel.sosPost(location.getLatitude(), location.getLongitude());
    }

    public static PostModel sosPost(double latitude, double longitude) {
        Timestamp now = Timestamp.now();

        HashMap<QualificationsEnum, Boolean> requirements = new HashMap<>();

        return new PostModel(
                "[SOS] New emergency!!",
                "Emergency reported through use of the SOS button. " +
                        "Location: [lat: " + latitude + ", lon: " + longitude + "]. " +
                        "Posted at: " + now.toDate(),
                now,
                latitude,
                longitude,
                StatusEnum.WAITING,
                requirements,
                AccountService.logedInUserModel.id
        );
    }

    public static String makeGeoId(double latitude, double longitude) {
        return String.format(GEO_POINT_INDEX_FORMAT, latitude, longitude);
    }

    public static String makeGeoId(GeoPoint location) {
        return String.format(GEO_POINT_INDEX_FORMAT, location.getLatitude(), location.getLongitude());
    }

    public ArrayList<QualificationsEnum> getSelectedQuealifications() {
        ArrayList<QualificationsEnum> result = new ArrayList<>();

        this.qualifications.forEach((qualification, selected) -> {
            if (selected) {
                result.add(QualificationsEnum.valueOf(qualification));
            }
        });

        return result;
    }
}
