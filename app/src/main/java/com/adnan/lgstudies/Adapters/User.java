package com.adnan.lgstudies.Adapters;

public class User {
    String mobile, name, email, password, pimage,deviceId;
    boolean isLoggedIn;

    public User(String mobile, String name, String email, String password, String pimage, String deviceId, boolean isLoggedIn) {
        this.mobile = mobile;
        this.name = name;
        this.email = email;
        this.password = password;
        this.pimage = pimage;
        this.deviceId = deviceId;
        this.isLoggedIn = isLoggedIn;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}