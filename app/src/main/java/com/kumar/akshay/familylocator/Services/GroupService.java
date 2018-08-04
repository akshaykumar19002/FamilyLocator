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
import com.kumar.akshay.familylocator.MessageClasses.GroupMessage;

import java.util.HashMap;
import java.util.Map;

public class GroupService extends Service implements FamilyDatabase.GroupsData {

    public static final String TAG = "GroupService";
    private final IBinder binder = new GroupBinder();
    //Database
            FamilyDatabase fdb;

    String[] groupsNameArray = null;
    String userId;
    boolean isPresent = false;
    DBHelper db;
    Intent intent;
    GroupUpdateListenerInterfaceForLauncher groupUpdateListenerInterfaceForLauncher;

    public GroupService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.intent = intent;
        return binder;
    }

    public void startGroupServiceActions(Context context){
        Log.v(TAG, "onHandleIntent");
        groupUpdateListenerInterfaceForLauncher = (GroupUpdateListenerInterfaceForLauncher) context;
        fdb = FamilyDatabase.getmInstance(GroupService.this);
        fdb.groupsData = this;
        db = new DBHelper(this);
        userId = intent.getStringExtra("UserID");
        Map<String, String> map = new HashMap<>();
        map.put("uid", userId);
        fdb.addToRequestQueue("getGroups", map);
    }

    @Override
    public void dataForGroups(String result) {
        String[] allGroups = result.split(":");
        if (allGroups.length > 0) {
            for (String geo : allGroups) {
                String[] singleGeofence = geo.split(",");
                if (singleGeofence.length > 3) {
                    GroupMessage gm = new GroupMessage(singleGeofence[0], singleGeofence[1], singleGeofence[2], singleGeofence[3]);
                    if (db.verifyGroup(gm)) {
                        db.insertGroupIntoGroupsTable(singleGeofence[0], singleGeofence[1], singleGeofence[2], singleGeofence[3]);
                        groupUpdateListenerInterfaceForLauncher.updateGroupDataForLauncher();
                    }
                }
            }
        }
    }


    public interface GroupUpdateListenerInterfaceForLauncher {
        void updateGroupDataForLauncher();
    }

    public class GroupBinder extends Binder {

        public GroupService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GroupService.this;
        }
    }

}
