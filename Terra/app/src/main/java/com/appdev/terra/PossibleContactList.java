package com.appdev.terra;

public class PossibleContactList {

    String contactName;
    String status;
    String location;
    int image;

    String id;

    String phoneNumber;


    public PossibleContactList(String contactName, String status, int image, String phoneNumber,String id) {
        this.contactName = contactName;
        this.status = status;
        this.location = location;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }


    public int getImage() {
        return image;
    }

    public String getContactName() {
        return contactName;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }


}
