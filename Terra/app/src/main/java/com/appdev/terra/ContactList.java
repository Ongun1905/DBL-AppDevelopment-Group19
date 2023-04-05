package com.appdev.terra;

public class ContactList {

    String contactName;
    String status;
    String location;
    int image;

    String phoneNumber;


    public ContactList(String contactName, String status, int image, String phoneNumber) {
        this.contactName = contactName;
        this.status = status;
        this.location = location;
        this.image = image;
        this.phoneNumber = phoneNumber;
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
