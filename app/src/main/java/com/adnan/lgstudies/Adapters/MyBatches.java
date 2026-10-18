package com.adnan.lgstudies.Adapters;

public class MyBatches {

    String batch_thumbnail,course_name,start_date,end_date,price,description;
    private boolean isPremium;

    public MyBatches() {
    }

    public MyBatches(String batch_thumbnail, String course_name, String start_date, String end_date, String price, String description, boolean isPremium) {
        this.batch_thumbnail = batch_thumbnail;
        this.course_name = course_name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.description = description;
        this.isPremium = isPremium;
    }

    public String getBatch_thumbnail() {
        return batch_thumbnail;
    }

    public void setBatch_thumbnail(String batch_thumbnail) {
        this.batch_thumbnail = batch_thumbnail;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
