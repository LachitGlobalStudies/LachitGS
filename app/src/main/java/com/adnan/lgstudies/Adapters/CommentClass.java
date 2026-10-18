package com.adnan.lgstudies.Adapters;

public class CommentClass {
    String name,comment,time,profile;

    public CommentClass() {

    }

    public CommentClass(String name, String comment, String time, String profile) {
        this.name = name;
        this.comment = comment;
        this.time = time;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
