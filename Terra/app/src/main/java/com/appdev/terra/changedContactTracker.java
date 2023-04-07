package com.appdev.terra;

import java.util.ArrayList;

public class changedContactTracker {
    private static changedContactTracker instance;

    static ArrayList<ContactList> changedContacts = new ArrayList<ContactList>();

    static  ArrayList<ContactList> changedNames = new ArrayList<ContactList>();

    private changedContactTracker(){

    }
}
