package com.adnan.lgstudies.Adapters;

public class ProgressQuestionSetData {
    String batch,set_name;
    int total_attempt;

    public ProgressQuestionSetData() {
    }

    public ProgressQuestionSetData(String batch, String set_name, int total_attempt) {
        this.batch = batch;
        this.set_name = set_name;
        this.total_attempt = total_attempt;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSet_name() {
        return set_name;
    }

    public void setSet_name(String set_name) {
        this.set_name = set_name;
    }

    public int getTotal_attempt() {
        return total_attempt;
    }

    public void setTotal_attempt(int total_attempt) {
        this.total_attempt = total_attempt;
    }
}
