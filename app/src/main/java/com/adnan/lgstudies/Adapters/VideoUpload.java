package com.adnan.lgstudies.Adapters;

public class VideoUpload {
    String postkey, videoId, topic_title, date, time, thumbnail;
    boolean isPremium;

    public VideoUpload() {
    }

    public VideoUpload(String postkey, String videoId, String topic_title, String date, String time, String thumbnail, boolean isPremium) {
        this.postkey = postkey;
        this.videoId = videoId;
        this.topic_title = topic_title;
        this.date = date;
        this.time = time;
        this.thumbnail = thumbnail;
        this.isPremium = isPremium;
    }

    public String getPostkey() {
        return postkey;
    }

    public void setPostkey(String postkey) {
        this.postkey = postkey;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTopic_title() {
        return topic_title;
    }

    public void setTopic_title(String topic_title) {
        this.topic_title = topic_title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
