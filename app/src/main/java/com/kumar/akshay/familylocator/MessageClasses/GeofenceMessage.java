package com.kumar.akshay.familylocator.MessageClasses;

/**
 * Created by LENOVO on 03-08-2017.
 */

public class GeofenceMessage {
    String  geofenceId, geofenceName, latitude, longitude;

    public GeofenceMessage(){}

    public GeofenceMessage(String geofenceId, String geofenceName, String latitude, String longitude){
        this.geofenceId = geofenceId;
        this.geofenceName = geofenceName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GeofenceMessage(String geofenceName, String latitude, String longitude) {
        this.geofenceName = geofenceName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getGeofenceId() {
        return geofenceId;
    }

    public void setGeofenceId(String geofenceId) {
        this.geofenceId = geofenceId;
    }

    public String getGeofenceName() {
        return geofenceName;
    }

    public void setGeofenceName(String geofenceName) {
        this.geofenceName = geofenceName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
