package com.adnan.lgstudies.Adapters;

public class ProgressReport {
    String batch,test_series,name,pimage;
    int marks_obtained,total_user,rank;

    public ProgressReport() {
    }

    public ProgressReport(String batch, String test_series, String name, String pimage, int marks_obtained, int total_user, int rank) {
        this.batch = batch;
        this.test_series = test_series;
        this.name = name;
        this.pimage = pimage;
        this.marks_obtained = marks_obtained;
        this.total_user = total_user;
        this.rank = rank;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getTest_series() {
        return test_series;
    }

    public void setTest_series(String test_series) {
        this.test_series = test_series;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public int getMarks_obtained() {
        return marks_obtained;
    }

    public void setMarks_obtained(int marks_obtained) {
        this.marks_obtained = marks_obtained;
    }

    public int getTotal_user() {
        return total_user;
    }

    public void setTotal_user(int total_user) {
        this.total_user = total_user;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
