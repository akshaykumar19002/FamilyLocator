package com.kumar.akshay.familylocator.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kumar.akshay.familylocator.MessageClasses.NotifyMessage;
import com.kumar.akshay.familylocator.NotifyingLocation.NotifyActivity;
import com.kumar.akshay.familylocator.R;

import static com.kumar.akshay.familylocator.LauncherActivity.ANONYMOUS;
import static com.kumar.akshay.familylocator.LauncherActivity.mUserid;
import static com.kumar.akshay.familylocator.LauncherActivity.sendLocationSharedPrefs;

public class NotifyService extends IntentService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    final String TAG = "NotifyService";
    private String mUsername;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location location = null;
    public static boolean canSendLocation = false;
    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getApplicationContext().getSharedPreferences(getString(R.string.send_location_shared_preference), Context.MODE_PRIVATE);
        SharedPreferences sp = getApplicationContext().getSharedPreferences(getString(R.string.send_location_shared_preference), Context.MODE_PRIVATE);
        canSendLocation = sp.getBoolean("sendLocation", true);
//        Log.v(TAG, "canSendLocation" + canSendLocation);
//        FirebaseApp.initializeApp(NotifyService.this);
//        mFirebaseAuth = FirebaseAuth.getInstance();
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    mUsername = user.getDisplayName();
//                    onSignedInInitialize(user.getDisplayName());
//                } else {
//                    onSignedOutCleanup();
//                }
//            }
//        };
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//
//        // Create the LocationRequest object
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
//                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
//
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
//        mDatabaseReference = mFirebaseDatabase.getReference().child("notify");
//        mGoogleApiClient.connect();
    }

    private void attachDatabaseReadListener() {
        Log.v(TAG, "Database Read Listener Added Successfully");
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    NotifyMessage notifyMessage = dataSnapshot.getValue(NotifyMessage.class);
//                    if (notifyMessage.getmUserID().equals(mUserid) && notifyMessage.getLocation().equals("needed")) {
//                        Log.v(TAG, "Got a notify location request");
//                        if (ActivityCompat.checkSelfPermission(NotifyService.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
//                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NotifyService.this,
//                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            Toast.makeText(NotifyService.this, "Permission not granted", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        if (location != null) {
//                            if(canSendLocation) {
//                                notifyMessage.setLocation("" + location.getLatitude() + "," + location.getLongitude());
//                                notifyMessage.setMessage("Got User Location");
//                                mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
//                                mDatabaseReference.push().setValue(notifyMessage);
//                            }
//                            else{
//                                notifyMessage.setLocation("Request Rejected by User");
//                                mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
//                                mDatabaseReference.push().setValue(notifyMessage);
//                            }
//
//                        } else
//                            Toast.makeText(NotifyService.this, "Unable to find your location", Toast.LENGTH_LONG).show();
//                    }else if(notifyMessage.getRequestedBy().equals(mUserid) && notifyMessage.getMessage().equals("Got User Location")) {
//                            generateNotification(notifyMessage);
//                            mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
//                    }else if (notifyMessage.getRequestedBy().equals(mUserid) && notifyMessage.getLocation().equals("Request Rejected by User")) {
//                        generateNotification(notifyMessage);
//                        mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//            mDatabaseReference.addChildEventListener(mChildEventListener);
//        }
    }

    private void generateNotification(NotifyMessage notifyMessage) {
        Intent notificationIntent = new Intent(getApplicationContext(), NotifyActivity.class);
        notificationIntent.putExtra("username", notifyMessage.getmUsername());
        notificationIntent.putExtra("location", notifyMessage.getLocation());
        notificationIntent.putExtra("time", notifyMessage.getDate());

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(NotifyActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // Define the notification settings.
        if (notifyMessage.getLocation().equals("Request Rejected by User")){
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    // In a real app, you may want to use a library like Volley
                    // to decode the Bitmap.
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher))
                    .setColor(Color.RED)
                    .setContentTitle(notifyMessage.getmUsername() + " rejected your request")
                    .setContentText(getString(R.string.geofence_transition_notification_text));
        }else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    // In a real app, you may want to use a library like Volley
                    // to decode the Bitmap.
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                            R.mipmap.ic_launcher))
                    .setColor(Color.RED)
                    .setContentTitle("Got Location for : " + notifyMessage.getmUsername())
                    .setContentText(getString(R.string.geofence_transition_notification_text))
                    .setContentIntent(notificationPendingIntent);
        }
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

    private void detachDatabaseReadListener() {
//        if (mChildEventListener != null) {
//            mDatabaseReference.removeEventListener(mChildEventListener);
//            mChildEventListener = null;
//        }
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            detachDatabaseReadListener();
            attachDatabaseReadListener();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        detachDatabaseReadListener();
        attachDatabaseReadListener();
    }

}
