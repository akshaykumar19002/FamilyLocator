package com.kumar.akshay.familylocator.MessageClasses;

public class LocationMessage {

    private String currentLocation;
    private String time, lid, uid;

    public LocationMessage() {
    }

    public LocationMessage(String lid, String uid, String time, String currentLocation) {
        this.currentLocation = currentLocation;
        this.time = time;
        this.lid = lid;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String text) {
        this.currentLocation = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String longitude) {
        this.time = longitude;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }
}
