package com.adnan.lgstudies.Adapters;


public class Notification {
    private String message;

    public Notification() {
        // Default constructor required for Firebase
    }

    public Notification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
