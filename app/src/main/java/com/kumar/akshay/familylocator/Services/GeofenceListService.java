package com.kumar.akshay.familylocator.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.GeofenceMessage;

import java.util.HashMap;
import java.util.Map;


public class GeofenceListService extends Service implements FamilyDatabase.GeofenceData {

    private final IBinder mGeofenceBinder = new GeofenceBinder();
    public static final String TAG = "GeofenceListService";

    //Database
    FamilyDatabase fdb;
    GeofenceUpdateListenerInterfaceForLauncher geofenceUpdateListenerInterfaceForLauncher;
    String[] geofenceNameArray = null;
    boolean isPresent = false;
    Context context;
    Intent intent;

    DBHelper db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return mGeofenceBinder;
    }

    public void startServiceActions(Context context) {
        this.context = context;
        geofenceUpdateListenerInterfaceForLauncher = (GeofenceUpdateListenerInterfaceForLauncher) context;
        Log.v(TAG, "geofenceListService");
        fdb = FamilyDatabase.getmInstance(GeofenceListService.this);
        fdb.geofenceData = this;
        db = new DBHelper(GeofenceListService.this);
        Map<String, String> map = new HashMap<>();
        map.put("uid", intent.getStringExtra("uid"));
        fdb.addToRequestQueue("getGeofence", map);
    }

    /**
     * Inserts the data obtained from the http request into the database if it is valid
     *
     * @param result data obtained from the response of http request
     */
    @Override
    public void dataForGeofences(String result) {
        String[] allGeofences = result.split(":");
        if (allGeofences.length > 0) {
            for (String geo : allGeofences) {
                Log.v(TAG, geo);
                String[] singleGeofence = geo.split(",");
                if (singleGeofence.length > 1)
                    if (db.verifyGeofence(singleGeofence[0], singleGeofence[1], singleGeofence[3], singleGeofence[4])) {
                        db.insertDataIntoGeofenceList(singleGeofence[0], singleGeofence[1], singleGeofence[2], singleGeofence[3], singleGeofence[4]);
                        geofenceUpdateListenerInterfaceForLauncher.updateGeofenceDataForLauncher();
                    }
            }
        }
    }

    public interface GeofenceUpdateListenerInterfaceForLauncher {
        void updateGeofenceDataForLauncher();
    }

    public class GeofenceBinder extends Binder {

        public GeofenceListService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GeofenceListService.this;
        }
    }

}
