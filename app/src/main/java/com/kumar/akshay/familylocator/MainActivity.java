package com.kumar.akshay.familylocator;


import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kumar.akshay.familylocator.Account.SettingsActivity;
import com.kumar.akshay.familylocator.Services.GettingUserLocationService;
import com.kumar.akshay.familylocator.Services.UpdatingUserDetailsService;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.Services.NotifyService;
import com.kumar.akshay.familylocator.Services.GeoFenceTransitionsIntentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AddGeofencesDialogFragment.AddGeoFenceListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList;
    private static final String TAG = "MainActivity";
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    //public SharedPreferences signupPref;

    public static String mUsername;
    protected LocationRequest locationRequest;

    private FloatingActionButton addGeofencesFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //String [] userIdsArray = new FamilyDatabase(MainActivity.this).getAllUserIds();
        FamilyDatabase fdb = FamilyDatabase.getmInstance(MainActivity.this);
        //fdb.recreatingTables();

//        //writing preference
//        signupPref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = signupPref.edit();
//        editor.putBoolean(getString(R.string.signupFor),true);
//        editor.commit();
//
//        //Readin preference
//        signupPref = getPreferences(Context.MODE_PRIVATE);
//        boolean signupFormality = signupPref.getBoolean(getString(R.string.signupFor), false);


        setContentView(R.layout.activity_main);
        addGeofencesFAB = (FloatingActionButton) findViewById(R.id.addGeofencesButton);
        addGeofencesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        //Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10000);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        mGeofenceList = new ArrayList<>();
        mUsername = ANONYMOUS;
        createLocationRequest();

        // Initialize Firebase components
        //mFirebaseAuth = FirebaseAuth.getInstance();

        populateGeofenceList();
        buildGoogleApiClient();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.notify_dialog_messsage)
                .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent notify = new Intent(MainActivity.this, NotifyService.class);
                        startService(notify);
                        NotifyService.canSendLocation = true;
                    }
                })
                .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        NotifyService.canSendLocation = false;
                    }
                }).show();

//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser user1 = firebaseAuth.getCurrentUser();
//                if (user1 != null) {
//                    // User is signed in
//                    if (!user1.isEmailVerified()) {
//                        RelativeLayout rl = new RelativeLayout(MainActivity.this);
//                        Button sendVerifyMail = new Button(MainActivity.this);
//                        sendVerifyMail.setText("Send Verification Email");
//                        sendVerifyMail.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                user1.sendEmailVerification()
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(MainActivity.this, "Verification Email sent", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }
//                        });
//                        sendVerifyMail.setBackgroundColor(Color.rgb(100, 110, 220));
//                        TextView tv = new TextView(MainActivity.this);
//                        tv.setText("Please Verify your email id to continue ...");
//                        tv.setTextSize(32);
//                        tv.setId(1);
//                        sendVerifyMail.setId(2);
//                        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams
//                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        tvParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                        tvParams.addRule(RelativeLayout.CENTER_VERTICAL);
//                        tvParams.addRule(RelativeLayout.ABOVE, sendVerifyMail.getId());
//                        tvParams.setMargins(35, 0, 25, 75);
//
//                        RelativeLayout.LayoutParams sendEmailParams = new RelativeLayout.LayoutParams
//                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        sendEmailParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                        sendEmailParams.addRule(RelativeLayout.CENTER_VERTICAL);
//
//                        rl.addView(tv, tvParams);
//                        rl.addView(sendVerifyMail, sendEmailParams);
//                        rl.setBackgroundColor(Color.rgb(100, 110, 220));
//                        setContentView(rl);
//                    } else
//                        setContentView(R.layout.activity_main);
//                    onSignedInInitialize(user1.getDisplayName());
//                } else {
//                    onSignedOutCleanup();
//                    callingFirebaseUI();
//                }
//                user = user1;
//                Intent i = new Intent(MainActivity.this, UpdatingUserDetailsService.class);
//                i.putExtra("UserName", user.getDisplayName());
//                i.putExtra("UserID", user.getUid());
//                i.putExtra("UserEmail", user.getEmail());
//                startService(i);
//                Log.v(TAG, "UpdatingUserDetailsService" + user.isEmailVerified());
//
//                Intent in = new Intent(MainActivity.this, GettingUserLocationService.class);
//                startService(in);
//                Log.v(TAG, "GettingUserLocationService");
//            }
//        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void createLocationRequest() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
//        if (requestCode == RC_SIGN_IN) {
//            IdpResponse response = IdpResponse.fromResultIntent(data);
//
//            // Successfully signed in
//            if (resultCode == ResultCodes.OK) {
//                //                startActivity(new Intent(Intent.ACTION_MAIN));
//                //                finish();
//                Toast.makeText(this, "Signed in!!", Toast.LENGTH_LONG).show();
//                return;
//            } else {
//                // Sign in failed
//                if (response == null) {
//                    // User pressed back button
//                    Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
//                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
//                    return;
//                }
//
//                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
//                    Toast.makeText(this, "Unknown Error", Toast.LENGTH_LONG).show();
//                    return;
//                }
//            }
//
//            Toast.makeText(this, "Unknown Signin response", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if (mLastLocation != null) {
//        if (mLastLocation != null) {
//            latitute = mLastLocation.getLatitude();
//            longitude = mLastLocation.getLongitude();
//        }

//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                .addLocationRequest(locationRequest);
//        builder.setAlwaysShow(true);
//        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(
//                mGoogleApiClient,
//                builder.build()
//        ).setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void populateGeofenceList() {
        for (Map.Entry<String, LatLng> entry : Constants.LANDMARKS.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            Constants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)

                    // Create the geofence.
                    .build());
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private GeofencingRequest getGeoFencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT | GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeoFenceTransitionsIntentService.class);
//        intent.putExtra("UserName", user.getDisplayName());
//        intent.putExtra("UserID", user.getUid());
//        intent.putExtra("UserEmail", user.getEmail());
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onResult(@NonNull Status status) {
//        final Status status = locationSettingsResult.getStatus();
//        switch (status.getStatusCode()) {
//            case LocationSettingsStatusCodes.SUCCESS:
//                // NO need to show the dialog;
//
//                break;
//
//            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                //  GPS turned off, Show the user a dialog
//                try{
//                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
//                }catch (IntentSender.SendIntentException e)
//                {
//                    Log.e(TAG,e.toString());
//                }
//                break;
//
//            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                // Location settings are unavailable so not possible to show any dialog now
//                break;
//        }
    }

    public void addGeofencesButtonHandler(View view) {
        if (!mGoogleApiClient.isConnected()) {
            Toast.makeText(this, getString(R.string.not_connected), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    // The GeofenceRequest object.
                    getGeoFencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (mAuthStateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;
            case R.id.info_button:
                Toast.makeText(this, "Please turn Internet and location on before opening the app", Toast.LENGTH_LONG).show();
                return true;
        }
        return true;
    }

    private void onSignedInInitialize(String displayName) {
        mUsername = displayName;
    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
        a_builder.setCancelable(false);
        a_builder.setMessage("Do you want to close this app?")
                .setTitle("Exit App")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void getUsersListButtonHandler(View view) {
//        if (user != null) {
//            Intent in = new Intent(this, UsersList.class);
//            startActivity(in);
//        } else
//            Toast.makeText(this, "Unable to authenticate the user. Please try to reopen the app.", Toast.LENGTH_LONG).show();
    }

    public void createDialog(){
        AddGeofencesDialogFragment addGeofencesDialogFragment = new AddGeofencesDialogFragment();
        addGeofencesDialogFragment.show(getSupportFragmentManager(), "AddGeofenceDialogFragment");
    }

    @Override
    public void onDialogAddButtonClick(DialogFragment dialog, String geofenceName) {

    }
}