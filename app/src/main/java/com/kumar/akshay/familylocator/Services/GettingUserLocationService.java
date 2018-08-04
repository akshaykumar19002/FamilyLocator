package com.kumar.akshay.familylocator.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.LocationMessage;

import java.util.HashMap;
import java.util.Map;

public class GettingUserLocationService extends Service implements FamilyDatabase.GettingUsersLocationData {

    private final IBinder mLocationBinder = new LocationBinder();
    private static final String TAG = "GettingUserLocation";
    FamilyDatabase fdb;
    LocationUpdateListenerInterfaceForLauncher locationUpdateListenerInterfaceForLauncher;
    DBHelper db;
    String uid;
    Intent intent;

    public GettingUserLocationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return mLocationBinder;
    }

    public void startLocationServiceActions(Context context){
        Log.v(TAG, "GettingUserLocationService");
        fdb = FamilyDatabase.getmInstance(GettingUserLocationService.this);
        locationUpdateListenerInterfaceForLauncher = (LocationUpdateListenerInterfaceForLauncher) context;
        fdb.usersLocationData = GettingUserLocationService.this;
        db = new DBHelper(GettingUserLocationService.this);
        uid = intent.getStringExtra("uid");
        Map<String, String> map = new HashMap<>();
        if (db.checkIfCurrentUser(uid)) {
            map.put("uid", uid);
        }
        fdb.addToRequestQueue("gettingUserLocations", map);
    }

    @Override
    public void dataForUsersLocations(String result) {
        String [] allGeofences = result.split(":");
        if (allGeofences.length > 0) {
            for (String geo : allGeofences) {
                Log.v(TAG, geo);
                String[] singleGeofence = geo.split(",");
                if (singleGeofence.length == 4) {
                    String time = singleGeofence[3];
                    String[] timeArray = time.split("-");
                    String currentTime = timeArray[0] + "-" + timeArray[1] + "-" + timeArray[2] + ":" + timeArray[3] + ":" + timeArray[4];
                    if (db.verifyLocation(singleGeofence[0])) {
                        db.insertDataIntoLocationsTable(singleGeofence[0], singleGeofence[1], singleGeofence[2], currentTime);
                        locationUpdateListenerInterfaceForLauncher.updateLocationDataForLauncher();
                    }
                }
            }
        }
    }

    public interface LocationUpdateListenerInterfaceForLauncher {
        void updateLocationDataForLauncher();
    }

    public class LocationBinder extends Binder {

        public GettingUserLocationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GettingUserLocationService.this;
        }
    }

}
