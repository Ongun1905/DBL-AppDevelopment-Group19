package com.appdev.terra;

public class Post {
    private String postText;
    private String username;
    private String location;
    private String level;

    private String requirements;

    private boolean verified;

    public Post(String postText, String username, String location, String level, String requirements, boolean varified) {
        this.postText = postText;
        this.username = username;
        this.location = location;
        this.level = level;
        this.requirements = requirements;
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
    public boolean requirementsMet() {return true;}
}
