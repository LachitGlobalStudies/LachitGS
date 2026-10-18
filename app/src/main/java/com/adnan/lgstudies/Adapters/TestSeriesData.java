package com.adnan.lgstudies.Adapters;

public class TestSeriesData {

    String title,batch_name;
    boolean isPremium;

    public TestSeriesData() {

    }

    public TestSeriesData(String title, String batch_name, boolean isPremium) {
        this.title = title;
        this.batch_name = batch_name;
        this.isPremium = isPremium;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBatch_name() {
        return batch_name;
    }

    public void setBatch_name(String batch_name) {
        this.batch_name = batch_name;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }
}
