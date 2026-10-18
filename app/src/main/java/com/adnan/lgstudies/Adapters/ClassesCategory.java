package com.adnan.lgstudies.Adapters;

public class ClassesCategory {
    String class_category,videos_no,notes_no,excercises;

    public ClassesCategory() {
    }

    public ClassesCategory(String class_category, String videos_no, String notes_no, String excercises) {
        this.class_category = class_category;
        this.videos_no = videos_no;
        this.notes_no = notes_no;
        this.excercises = excercises;
    }

    public String getClass_category() {
        return class_category;
    }

    public void setClass_category(String class_category) {
        this.class_category = class_category;
    }

    public String getVideos_no() {
        return videos_no;
    }

    public void setVideos_no(String videos_no) {
        this.videos_no = videos_no;
    }

    public String getNotes_no() {
        return notes_no;
    }

    public void setNotes_no(String notes_no) {
        this.notes_no = notes_no;
    }

    public String getExcercises() {
        return excercises;
    }

    public void setExcercises(String excercises) {
        this.excercises = excercises;
    }
}
