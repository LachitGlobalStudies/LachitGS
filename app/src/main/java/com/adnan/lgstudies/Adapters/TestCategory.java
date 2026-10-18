package com.adnan.lgstudies.Adapters;

public class TestCategory {

    String test_category,total_test;

    public TestCategory(String test_category, String total_test) {
        this.test_category = test_category;
        this.total_test = total_test;
    }

    public String getTest_category() {
        return test_category;
    }

    public void setTest_category(String test_category) {
        this.test_category = test_category;
    }

    public String getTotal_test() {
        return total_test;
    }

    public void setTotal_test(String total_test) {
        this.total_test = total_test;
    }
}
