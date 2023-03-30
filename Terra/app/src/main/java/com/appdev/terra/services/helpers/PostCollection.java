package com.appdev.terra.services.helpers;

import com.appdev.terra.Post;
import com.appdev.terra.enums.QualificationsEnum;
import com.appdev.terra.enums.StatusEnum;
import com.appdev.terra.enums.VerificationEnum;
import com.appdev.terra.models.PostModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PostCollection {
    private GeoPoint location;
//    public String locationName;

    private StatusEnum accidentStatus;
    private VerificationEnum verificationStatus;
    private HashSet<QualificationsEnum> requestedQualifications;

    private HashMap<String, PostModel> posts;

    public PostCollection(GeoPoint location, StatusEnum accidentStatus) {
        this.location = location;
        this.accidentStatus = accidentStatus;

        this.verificationStatus = VerificationEnum.AWAITING_VERIFICATION;
        this.requestedQualifications = new HashSet<>();
        this.posts = new HashMap<>();
    }

    public PostCollection(GeoPoint location, StatusEnum accidentStatus,
                          VerificationEnum verificationStatus, HashSet<QualificationsEnum> requestedQualifications,
                          HashMap<String, PostModel> posts) {
        this.location = location;
        this.accidentStatus = accidentStatus;
        this.verificationStatus = verificationStatus;
        this.requestedQualifications = requestedQualifications;
        this.posts = posts;
    }

    public static PostCollection fromFirebaseDocument(DocumentSnapshot document) {
        // Convert HashMap representation of PostModels to PostModels
        HashMap<String, Object> _postsMap = (HashMap<String, Object>) document.getData().get("posts");
        HashMap<String, PostModel> postsMap = new HashMap<>();
        _postsMap.forEach((k, v) -> {
            postsMap.put(k, PostModel.fromHashMap((HashMap<String, Object>) v));
        });

        return new PostCollection(
                (GeoPoint) document.getGeoPoint("location"),
                StatusEnum.valueOf(document.getString("accidentStatus")),
                VerificationEnum.valueOf(document.getString("verificationStatus")),
                QualificationsEnum.decodeQualifications(document.getString("requestedQualifications")),
                postsMap
        );
    }

    public HashMap<String, Object> toFirebasePostCollection() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("location", location);
        result.put("accidentStatus", accidentStatus);
        result.put("verificationStatus", verificationStatus);
        result.put("requestedQualifications", QualificationsEnum.encodeQualifications(requestedQualifications));
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

    public VerificationEnum getVerificationStatus() {
        return verificationStatus;
    }

    public Iterator<QualificationsEnum> getRequestedQualifications() {
        return requestedQualifications.iterator();
    }

    public Collection<PostModel> getPosts() {
        return posts.values();
    }
}
