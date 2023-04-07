package com.appdev.terra;

public class ContactList {

    String contactName;
    String status;
    String location;
    int image;

    String phoneNumber;
    String id;

    boolean changedStatus;

     String changedName;


    public ContactList(String contactName, String status, int image, String phoneNumber,String id) {
        this.contactName = contactName;
        this.status = status;
        this.location = location;
        this.image = image;
        this.phoneNumber = phoneNumber;
        this.id = id;
        changedStatus = false;
        changedName = contactName;
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

    public void setContactName(String newName){
        contactName = newName;
    }
    public boolean isChangedStatus() {
        return changedStatus;
    }

    public void setChangedStatus(boolean changedStatus) {
        this.changedStatus = changedStatus;
    }

    public String getChangedName() {
        return changedName;
    }

    public void setChangedName(String changedName) {
        this.changedName = changedName;
    }

}
