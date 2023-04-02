package com.appdev.terra;

public class Post {
    private String postText;
    private String username;
    private String location;
    private String level;

    private String requirements;

    private boolean verified;
    private boolean reqmet;

    public Post(String postText, String username, String location, String level, String requirements, boolean verified, boolean reqmet) {
        this.postText = postText;
        this.username = username;
        this.location = location;
        this.level = level;
        this.verified = verified;
        this.requirements = requirements;
        this.reqmet = reqmet;
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

    public String getRequirements() {return requirements;}

    public boolean isVerified() { return verified;}
    public boolean requirementsMet() {return reqmet;}
}
