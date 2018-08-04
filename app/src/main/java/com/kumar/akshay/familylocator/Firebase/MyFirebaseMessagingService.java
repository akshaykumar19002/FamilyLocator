package com.kumar.akshay.familylocator.Firebase;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Login.LoginActivity;
import com.kumar.akshay.familylocator.Services.GeofenceListService;
import com.kumar.akshay.familylocator.Services.GettingUserLocationService;
import com.kumar.akshay.familylocator.Services.GroupService;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "MyFirebaseMessagingService";
    Context context;
    String uid;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        context = MyFirebaseMessagingService.this;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        LoginActivity la = new LoginActivity();
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> map = remoteMessage.getData();
            String[] message = map.get("message").split(":");
            switch (message[0]) {
                case "updateLocation":
                    if (message.length > 1) {
                        uid = message[1];
                        Intent i = new Intent(context, GettingUserLocationService.class);
                        i.putExtra("uid", uid);
                        startService(i);
                    }
                    break;
                case "addGeofence":
                    if (message.length > 1) {
                        uid = message[1];
                        Intent i = new Intent(context, GeofenceListService.class);
                        i.putExtra("uid", uid);
                        startService(i);
                    }
                    break;
                case "addGroup":
                    if (message.length > 1){
                        uid = message[1];
                        Intent i = new Intent(context, GroupService.class);
                        i.putExtra("UserID", uid);
                        startService(i);
                    }
                    break;
            }
        }
    }
}
