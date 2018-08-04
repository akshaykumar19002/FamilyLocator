package com.kumar.akshay.familylocator.MessageClasses;


import static com.kumar.akshay.familylocator.LauncherActivity.mUsername;

/**
 * Created by LENOVO on 10-05-2017.
 */

public class NotifyMessage {

    private String mUserID, mUsername;
    private String message;
    private String location;
    private String date;
    private String requestedBy;

    public NotifyMessage() {
    }

    public NotifyMessage(String x, String y, String z, String a, String b, String c) {
        mUserID = x;
        mUsername = c;
        message = y;
        location = z;
        date = a;
        requestedBy = b;
    }

    public String getmUserID() {
        return mUserID;
    }

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public void setmUserID(String mUserID) {
        this.mUserID = mUserID;
    }

    public String getMessage() {
        return message;
    }

    public String getLocation() {
        return location;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDate(String date0) {
        date = date0;
    }

    public String getDate() {
        return date;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }
}
