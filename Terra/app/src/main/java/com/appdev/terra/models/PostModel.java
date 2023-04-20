package com.appdev.terra.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.services.AccountService;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostModel {
    // 6 characters, 2 decimals: ???.??
    private static final String GEO_POINT_INDEX_FORMAT = "[%06.2f, %06.2f]";

    public String geoId;
    public String description;
    public Timestamp postedAt;
    public GeoPoint location;
    public StatusEnum status;
    public HashMap<String, Boolean> qualifications = new HashMap<>();
    public boolean verified;

    public final String userId;

    public PostModel(
            String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications
    ) {
        this(description, postedAt, location, status, qualifications, false, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified
    ) {
        this(description, postedAt, location, status, qualifications, verified, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String description,
            Timestamp postedAt, double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications
    ) {
        this(description, postedAt, latitude, longitude, status, qualifications, false, AccountService.logedInUserModel.id);
    }

    public PostModel(
            String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, String userId
    ) {
        this(description, postedAt, location, status, qualifications, false, userId);
    }

    public PostModel(
            String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, String userId
    ) {
        this(description, postedAt, latitude, longitude, status, qualifications, false, userId);
    }

    public PostModel(
            String description,
            Timestamp postedAt, GeoPoint location, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified, String userId
    ) {
        this(description, postedAt, location.getLatitude(), location.getLongitude(), status, qualifications, verified, userId);
    }

    private PostModel(
            String description, Timestamp postedAt,
            double latitude, double longitude, StatusEnum status,
            HashMap<QualificationsEnum, Boolean> qualifications, boolean verified, String userId
    ) {
        this.geoId = makeGeoId(latitude, longitude);
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

        for (QualificationsEnum qualification : QualificationsEnum.values()) {
            requirements.put(qualification, false);
        }

        return new PostModel(
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

    public String getTitle(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(this.location.getLatitude(), this.location.getLongitude(),1);

            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            return this.location.toString();
        }
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
