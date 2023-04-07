package com.appdev.terra.services.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.enums.VerificationEnum;
import com.appdev.terra.models.PostModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PostCollection implements Serializable {
    private GeoPoint location;

    private StatusEnum accidentStatus;

    private HashMap<String, PostModel> posts;

    public PostCollection(GeoPoint location, StatusEnum accidentStatus) {
        this.location = location;
        this.accidentStatus = accidentStatus;
        this.posts = new HashMap<>();
    }

    public PostCollection(GeoPoint location, StatusEnum accidentStatus, HashMap<String, PostModel> posts) {
        this.location = location;
        this.accidentStatus = accidentStatus;
        this.posts = posts;
    }

    public static PostCollection fromFirebaseDocument(DocumentSnapshot document) {
        // Convert HashMap representation of PostModels to PostModels
        HashMap<String, Object> _postsMap = (HashMap<String, Object>) document.getData().get("posts");
        HashMap<String, PostModel> postsMap = new HashMap<>();
        _postsMap.forEach((k, v) -> {
            postsMap.put(k, PostModel.fromHashMap((HashMap<String, Object>) v, k));
        });

        return new PostCollection(
                (GeoPoint) document.getGeoPoint("location"),
                StatusEnum.valueOf(document.getString("accidentStatus")),
                postsMap
        );
    }

    public HashMap<String, Object> toFirebasePostCollection() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("location", location);
        result.put("accidentStatus", accidentStatus);
        result.put("posts", posts);

        return result;
    }

    public void addPost(String uid, PostModel post) {
        posts.put(uid, post);
    }

    public PostModel removePost(String uid) {
        return posts.remove(uid);
    }

    public boolean containsPostWithId(String uid) {
        return posts.containsKey(uid);
    }

    public PostModel getPostWithId(String uid) {
        return posts.get(uid);
    }

    public int getNrPosts() {
        return posts.size();
    }

    public GeoPoint getLocation() {
        return location;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
    }

    public StatusEnum getAccidentStatus() {
        return accidentStatus;
    }

    public HashMap<String, PostModel> getPostsMap() { return posts; }

    public Collection<PostModel> getPosts() {
        return posts.values();
    }

    public String getLocationName(Context context) {
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
}
