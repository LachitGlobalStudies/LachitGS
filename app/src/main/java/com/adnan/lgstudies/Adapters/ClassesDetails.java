package com.adnan.lgstudies.Adapters;

public class ClassesDetails {
    String thumbnaiil;
    String topic_title;
    String time;
    String date;
    String videoId;
    String postkey;

    public ClassesDetails() {
    }

    public ClassesDetails(String thumbnaiil, String topic_title, String time, String date, String videoId, String postkey) {
        this.thumbnaiil = thumbnaiil;
        this.topic_title = topic_title;
        this.time = time;
        this.date = date;
        this.videoId = videoId;
        this.postkey = postkey;

    }

    public String getThumbnaiil() {
        return thumbnaiil;
    }

    public void setThumbnaiil(String thumbnaiil) {
        this.thumbnaiil = thumbnaiil;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }
}
