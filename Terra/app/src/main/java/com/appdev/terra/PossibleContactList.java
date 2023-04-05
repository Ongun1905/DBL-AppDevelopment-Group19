package com.appdev.terra;

public class PossibleContactList {

    String contactName;
    String status;
    String location;
    int image;


    public PossibleContactList(String contactName, String status, int image) {
        this.contactName = contactName;
        this.status = status;
        this.location = location;
        this.image = image;
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
