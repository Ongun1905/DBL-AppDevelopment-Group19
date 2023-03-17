package com.appdev.terra;

public class Post {
    private String postText;
    private String username;
    private String location;
    private String level;

    public Post(String postText, String username, String location, String level) {
        this.postText = postText;
        this.username = username;
        this.location = location;
        this.level = level;
    }

    public String getPostText() {
        return postText;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getLevel() {
        return level;
    }
}
