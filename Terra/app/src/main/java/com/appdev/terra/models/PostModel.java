package com.appdev.terra.models;

import com.appdev.terra.enums.StatusEnum;

import java.util.Date;

public class PostModel {
    String id;
    String title;
    String description;
    Date postedAt;
    Double longitude;
    Double latitude;
    StatusEnum status;
}
