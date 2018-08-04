package com.kumar.akshay.familylocator;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kumar.akshay.familylocator.Account.SettingsActivity;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.Login.LoginActivity;
import com.kumar.akshay.familylocator.Maps.GeoFenceMapActivity;
import com.kumar.akshay.familylocator.MessageClasses.GeofenceMessage;
import com.kumar.akshay.familylocator.MessageClasses.GroupMessage;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;
import com.kumar.akshay.familylocator.Services.AddingGroupToUser;
import com.kumar.akshay.familylocator.Services.GeoFenceTransitionsIntentService;
import com.kumar.akshay.familylocator.Services.GeofenceListService;
import com.kumar.akshay.familylocator.Services.GettingUserLocationService;
import com.kumar.akshay.familylocator.Services.GroupService;
import com.kumar.akshay.familylocator.Services.NotifyService;
import com.kumar.akshay.familylocator.Services.UpdatingUserDetailsService;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;
import static com.kumar.akshay.familylocator.Constants.ADD_GEOFENCE_REQUEST_CODE;

public class LauncherActivity extends AppCompatActivity
        implements GroupsListFragment.GroupListListener, GeofenceListFragment.GeofenceListListener,
        AddGroupDialogFragent.AddGroupDialogListener, NavigationView.OnNavigationItemSelectedListener,
        AddGeofencesDialogFragment.AddGeoFenceListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GeofenceListService.GeofenceUpdateListenerInterfaceForLauncher,
        GettingUserLocationService.LocationUpdateListenerInterfaceForLauncher, GroupService.GroupUpdateListenerInterfaceForLauncher,
        LocationListener{

    private GoogleApiClient mGoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList = null;
    private static final String TAG = "LauncherActivity";
    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    private boolean canSendLocationLauncher = true;
    public UserMessage user;
    public static String mUsername, mUserid;
    protected LocationRequest locationRequest;

    //Geofenceing Client
    private GeofencingClient mGeofencingClient;

    GeofenceListFragment geofenceFragment = null;
    ShowLocations locationFragment = null;
    GroupsListFragment groupFragment = null;
    private LatLng addGeoFenceLatLng = null;
    GeofenceMessage geofenceMessage = null;
    FamilyDatabase fdb;
    DBHelper db;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    Switch switch_location;
    NavigationView navigationView;
    Fragment fragment;
    ActionMode mActionMode;
    DrawerLayout drawerLayout;

    public static boolean internetConnectivity = false;
    public boolean gpsEnabled = false;
    public static SharedPreferences sendLocationSharedPrefs;
    Intent notify = null;

    //Service variables
    GroupService mGroupService;
    GeofenceListService mGeofenceListService;
    UpdatingUserDetailsService mUpdatingUserDetailsService;

    //Service Binder Boolean variables
    boolean mBoundGroupService = false;
    boolean mBoundUpdatingUserDetailsService = false;

    //Checking location is on or not
    Location mylocation;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    Double longitude, latitude;

    Map<String, String> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("notify");
        sendLocationSharedPrefs = getSharedPreferences(getString(R.string.send_location_shared_preference), Context.MODE_PRIVATE);
        fdb = FamilyDatabase.getmInstance(LauncherActivity.this);
        db = new DBHelper(this);
        mUserid = getIntent().getStringExtra("uid");
        user = new UserMessage(getIntent().getStringExtra("uid"), getIntent().getStringExtra("username"), getIntent().getStringExtra("userEmail"));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        buildingActivity();
        basicStartingThings();
    }

    private void buildingActivity() {
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        lm.addGpsStatusListener(new GpsStatus.Listener() {
            @Override
            public void onGpsStatusChanged(int event) {
                switch (event) {
                    case GPS_EVENT_STARTED:
                        gpsEnabled = true;
                        break;
                    case GPS_EVENT_STOPPED:
                        gpsEnabled = false;
                        break;
                }
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.send_location_drawer).setActionView(new Switch(this));
        switch_location = (Switch) navigationView.getMenu().findItem(R.id.send_location_drawer).getActionView();
        switch_location.setChecked(sendLocationSharedPrefs.getBoolean("sendLocation", true));
        switch_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean switch_bool = switch_location.isChecked();
                SharedPreferences.Editor editor = sendLocationSharedPrefs.edit();
                editor.putBoolean("sendLocation", switch_bool);
                editor.commit();
                Log.v(TAG, "current : " + switch_bool);
                if (notify != null) {
                    notify = new Intent(LauncherActivity.this, NotifyService.class);
                    stopService(notify);
                    startService(notify);
                } else
                    startService(notify);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void buildingVerifyEmailActivity() {
        setContentView(R.layout.unable_to_verify);
        Button sendVerifyMail = (Button) findViewById(R.id.verify_email);
        sendVerifyMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                user.sendEmailVerification()
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(LauncherActivity.this, "Verification Email sent", Toast.LENGTH_SHORT).show();
//                            }
//                        });
            }
        });
    }


    private void basicStartingThings() {
        checkingInternetConnectivity();
        //Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LauncherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(LauncherActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this,
                    Manifest.permission.INTERNET) && ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this,
                    Manifest.permission.ACCESS_NETWORK_STATE) && ActivityCompat.shouldShowRequestPermissionRationale(LauncherActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(LauncherActivity.this,
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
        buildGoogleApiClient();
        createLocationRequest();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        Log.v(TAG, "onCreate");

        if (notify != null) {
            notify = new Intent(LauncherActivity.this, NotifyService.class);
            stopService(notify);
        } else
            notify = new Intent(LauncherActivity.this, NotifyService.class);
        notify.putExtra("canSendLocation", canSendLocationLauncher);
        startService(notify);
        buildingActivity();
//        mUserid = user1.getUid();
//        onSignedInInitialize(user1.getDisplayName());
        callServices();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int permissionLocation = ContextCompat.checkSelfPermission(LauncherActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            getMyLocation();
        }
        switch (requestCode) {
            case 10000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    basicStartingThings();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
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

    private void callServices() {
        Intent i = new Intent(LauncherActivity.this, GroupService.class);
        i.putExtra("UserName", user.getUsername());
        i.putExtra("UserID", user.getUid());
        i.putExtra("UserEmail", user.getUserEmail());
        bindService(i, mGroupServiceConnection, Context.BIND_AUTO_CREATE);
        Intent intent = new Intent(LauncherActivity.this, GeofenceListService.class);
        intent.putExtra("UserName", user.getUsername());
        intent.putExtra("uid", user.getUid());
        intent.putExtra("UserEmail", user.getUserEmail());
        bindService(intent, mGeofenceServiceConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "GeofenceListService");
        Intent in = new Intent(LauncherActivity.this, GettingUserLocationService.class);
        in.putExtra("uid", mUserid);
        bindService(in, mLocationServiceConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "GettingUserLocationService");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        check();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void populateGeofenceList() {
        mGeofenceList.clear();
        Cursor cursor = null;
        cursor = db.getAllGeofences();
        if (cursor != null)
            while (cursor.moveToNext()) {
                Log.v(TAG, cursor.getString(1) + cursor.getString(3) + cursor.getString(4));

                mGeofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(cursor.getString(1))

                        // Set the circular region of this geofence.
                        .setCircularRegion(
                                cursor.getShort(3),
                                cursor.getShort(4),
                                Constants.GEOFENCE_RADIUS_IN_METERS
                        )

                        // Set the expiration duration of the geofence. This geofence gets automatically
                        // removed after this period of time.
                        .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                        // Set the transition types of interest. Alerts are only generated for these
                        // transition. We track entry and exit transitions in this sample.
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT)

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
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, GeoFenceTransitionsIntentService.class);
        intent.putExtra("UserName", user.getUsername());
        intent.putExtra("UserID", user.getUid());
        intent.putExtra("UserEmail", user.getUserEmail());
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mConnectivityReciever, filter);
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onDestroy called");
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void addGeofencesButtonHandler() {
        populateGeofenceList();
        if (mGeofenceList.size() > 0)
            addGeofence();
        else
            Toast.makeText(this, "Please add a geofence first", Toast.LENGTH_LONG).show();
    }

    private void addGeofence() {
        try {
            mGeofencingClient.addGeofences(
                    // The GeofenceRequest object.
                    getGeoFencingRequest(),
                    // A pending intent that that is reused when calling removeGeofences(). This
                    // pending intent is used to generate an intent when a matched geofence
                    // transition is observed.
                    getGeofencePendingIntent()
            ).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.v(TAG, "Geofences Added");
                    if (geofenceMessage != null) {
                        //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("geofenceList/" + user.getUid());
                        //databaseReference.push().setValue(geofenceMessage);
                        Log.v(TAG, "Geofence created");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Failed to create geofence");
                    Toast.makeText(LauncherActivity.this, "Failed to create geofence.", Toast.LENGTH_LONG).show();
                }
            });
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (id == R.id.nav_refresh) {
            addGeofencesButtonHandler();
            Toast.makeText(this, "Refreshing Geofences", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_geofence_list) {
            tabLayout.getTabAt(0).select();
        } else if (id == R.id.nav_users_list) {
            tabLayout.getTabAt(1).select();
        } else if (id == R.id.nav_groups) {
            tabLayout.getTabAt(2).select();
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, "Guys, try this app!! /nfamily.locator@gmail.com");
            startActivity(i);
        } else if (id == R.id.nav_feedback) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Family Locator App");
            startActivity(i);
        } else if (id == R.id.about_user_drawer) {
            //About User

        } else if (id == R.id.signout_drawer) {
            Intent i = new Intent(this, LoginActivity.class);
            LoginActivity.updatingPreferences(-1, "", "");
            startActivity(i);
        } else if (id == R.id.send_location_drawer) {
            canSendLocationLauncher = sendLocationSharedPrefs.getBoolean("sendLocation", true);
            switch_location.setChecked(!canSendLocationLauncher);
            SharedPreferences.Editor editor = sendLocationSharedPrefs.edit();
            editor.putBoolean("sendLocation", switch_location.isChecked());
            editor.commit();

            if (notify != null) {
                notify = new Intent(LauncherActivity.this, NotifyService.class);
                stopService(notify);
                Log.v(TAG, "current1 : " + !canSendLocationLauncher);
            } else
                notify = new Intent(LauncherActivity.this, NotifyService.class);
            startService(notify);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mConnectivityReciever);
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    public void checkingInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null) {
            internetConnectivity = false;
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        } else if (info.isConnected())
            internetConnectivity = true;

    }

    @Override
    public void onDialogAddButtonClick(DialogFragment dialog, String geofenceName) {
        startActivityForResult(new Intent(this, GeoFenceMapActivity.class).putExtra("geofenceName", geofenceName), ADD_GEOFENCE_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
//                    case Activity.RESULT_CANCELED:
//                        finish();
//                        break;
                }
                break;
        }
        if (requestCode == ADD_GEOFENCE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.v(TAG, "Adding a new geofence");
                mGoogleApiClient.connect();
                addGeoFenceLatLng = (LatLng) data.getExtras().get("LatLng");
                populateGeofenceList();
                if (addGeoFenceLatLng != null) {
                    //add it to database and also to geofence list
                    Log.v(TAG, data.getStringExtra("geofenceName") + String.valueOf(addGeoFenceLatLng.latitude) + String.valueOf(addGeoFenceLatLng.longitude));
                    mGeofenceList.add(new Geofence.Builder()
                            // Set the request ID of the geofence. This is a string to identify this
                            // geofence.
                            .setRequestId(data.getStringExtra("geofenceName"))

                            // Set the circular region of this geofence.
                            .setCircularRegion(
                                    addGeoFenceLatLng.latitude,
                                    addGeoFenceLatLng.longitude,
                                    Constants.GEOFENCE_RADIUS_IN_METERS
                            )

                            // Set the expiration duration of the geofence. This geofence gets automatically
                            // removed after this period of time.
                            .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                            // Set the transition types of interest. Alerts are only generated for these
                            // transition. We track entry and exit transitions in this sample.
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT)

                            // Create the geofence.
                            .build());
                    addGeofence();
                    map.clear();
                    geofenceMessage = new GeofenceMessage(data.getStringExtra("geofenceName"), String.valueOf(addGeoFenceLatLng.latitude), String.valueOf(addGeoFenceLatLng.longitude));
                    map.put("geofence_name", data.getStringExtra("geofenceName"));
                    map.put("geofence_uid", mUserid);
                    map.put("geofence_lat", String.valueOf(addGeoFenceLatLng.latitude));
                    map.put("geofence_lng", String.valueOf(addGeoFenceLatLng.longitude));
                    fdb.addToRequestQueue("addGeofence", map);
                    fdb.addToMessageRequestQueue("addGeofence:" + mUserid);
                }
            }
        }
    }

    @Override
    public void addGroup(String gName) {
        if (user != null) {
            map.clear();
            map.put("group_name", gName);
            map.put("group_admin_id", mUserid);
            fdb.addToRequestQueue("createGroup", map);
            fdb.addToMessageRequestQueue("addGroup:" + mUserid);
            Toast.makeText(LauncherActivity.this, "Group Added", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(LauncherActivity.this, "Unknown Error!!!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean showMenuOnGroupsFragment(View view, GroupMessage groupMessage) {
        if (mActionMode != null) {
            return false;
        }
        getSupportActionBar().hide();
        mActionMode = startActionMode(mActionModeCallback);
        view.setSelected(true);
        return true;
    }

    @Override
    public boolean showGeofenceMenuOnGeofenceFragment(View view, GeofenceMessage geofenceMessage) {
        if (mActionMode != null) {
            return false;
        }
        getSupportActionBar().hide();
        this.geofenceMessage = geofenceMessage;
        mActionMode = startActionMode(mActionModeCallback);
        view.setSelected(true);
        return true;

    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        populateGeofenceList();
        if (mylocation != null) {
            latitude=mylocation.getLatitude();
            longitude=mylocation.getLongitude();
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    geofenceFragment = new GeofenceListFragment();
                    return geofenceFragment;
                case 1:
                    locationFragment = new ShowLocations();
                    Bundle bundle = new Bundle();
                    bundle.putString("Username", mUsername);
                    bundle.putString("uid", mUserid);
                    locationFragment.setArguments(bundle);
                    return locationFragment;
                case 2:
                    groupFragment = new GroupsListFragment();
                    return groupFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Geofences";
                case 1:
                    return "Location";
                case 2:
                    return "Groups";
            }
            return null;
        }
    }

    public void createDialog(View view) {
        int currentFragment = mViewPager.getCurrentItem();
        switch (currentFragment) {
            case 0:
                AddGeofencesDialogFragment addGeofencesDialogFragment = new AddGeofencesDialogFragment();
                addGeofencesDialogFragment.show(getSupportFragmentManager(), "AddGeofenceDialogFragment");
                break;
            case 2:
                AddGroupDialogFragent addGroupDialogFragment = new AddGroupDialogFragent();
                addGroupDialogFragment.show(getSupportFragmentManager(), "AddGroupDialogFragment");
                break;
        }
    }

    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contextual_menu_launcher, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (mViewPager.getCurrentItem() == 0) {
                switch (item.getItemId()) {
                    case R.id.cus_edit:
                        //edit geofence
                        break;
                    case R.id.cus_delete:
                        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("geofenceList/" + user.getUid());
//                                databaseReference.child(geofenceMessage.getGeofenceId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        //fdb.removeGeofence(geofenceMessage.getGeofenceName(), geofenceMessage.getLatitude(), geofenceMessage.getLongitude());
//                                        populateGeofenceList();
//                                        if (mGeofenceList != null)
//                                            addGeofence();
//                                        Toast.makeText(LauncherActivity.this, "Geofence deleted successfully", Toast.LENGTH_LONG).show();
//                                    }
//                                });
                            }
                        });
                }
            } else if (mViewPager.getCurrentItem() == 2) {
                switch (item.getItemId()) {
                    case R.id.cus_edit:
                        Toast.makeText(LauncherActivity.this, "Custom edit for group", Toast.LENGTH_LONG).show();
                    case R.id.cus_delete:
                        Toast.makeText(LauncherActivity.this, "Custom delete for group", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            getSupportActionBar().show();
        }
    };

    private BroadcastReceiver mConnectivityReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (info == null) {
                internetConnectivity = false;
                Snackbar.make(drawerLayout, "No Internet Connectivity", Snackbar.LENGTH_LONG).show();

            } else if (info.isConnected())
                internetConnectivity = true;
            displayConnectivityDetails();
        }
    };

    private void displayConnectivityDetails() {
        String message = null;
        if (internetConnectivity) {
            if (!gpsEnabled)
                message = "Location is turned off";
        } else {
            if (gpsEnabled) {
                message = "No internet connectivity";
            } else
                message = "No internet connectivity and location is turnned off";
        }
        if (message != null && drawerLayout != null) {
            Snackbar.make(drawerLayout, message, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void updateGeofenceDataForLauncher() {
        addGeofencesButtonHandler();
        geofenceFragment.gettingData();
    }

    @Override
    public void updateLocationDataForLauncher() {
        locationFragment.gettingDataFromDatabase();
    }

    @Override
    public void updateGroupDataForLauncher() {
        if (groupFragment != null)
            groupFragment.gettingData();
    }

    private ServiceConnection mGeofenceServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GeofenceListService.GeofenceBinder binder = (GeofenceListService.GeofenceBinder) service;
            mGeofenceListService = binder.getService();
            mGeofenceListService.startServiceActions(LauncherActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    private ServiceConnection mLocationServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GettingUserLocationService.LocationBinder binder = (GettingUserLocationService.LocationBinder) service;
            GettingUserLocationService gettingUserLocationService = binder.getService();
            gettingUserLocationService.startLocationServiceActions(LauncherActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    private ServiceConnection mGroupServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GroupService.GroupBinder binder = (GroupService.GroupBinder) service;
            GroupService groupService = binder.getService();
            groupService.startGroupServiceActions(LauncherActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };


    public void check(){
        int permissionLocation = ContextCompat.checkSelfPermission(LauncherActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
        }else{
            getMyLocation();
        }
    }

    private void getMyLocation(){
        if(mGoogleApiClient !=null) {
            if (mGoogleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(LauncherActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(mGoogleApiClient, locationRequest, this);
                    PendingResult<LocationSettingsResult> result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(mGoogleApiClient, builder.build());
                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:
                                    // All location settings are satisfied.
                                    // You can initialize location requests here.
                                    int permissionLocation = ContextCompat
                                            .checkSelfPermission(LauncherActivity.this,
                                                    Manifest.permission.ACCESS_FINE_LOCATION);
                                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                        mylocation = LocationServices.FusedLocationApi
                                                .getLastLocation(mGoogleApiClient);
                                    }
                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    // Location settings are not satisfied.
                                    // But could be fixed by showing the user a dialog.
                                    try {
                                        // Show the dialog by calling startResolutionForResult(),
                                        // and check the result in onActivityResult().
                                        // Ask to turn on GPS automatically
                                        status.startResolutionForResult(LauncherActivity.this,
                                                REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    // Location settings are not satisfied.
                                    // However, we have no way
                                    // to fix the
                                    // settings so we won't show the dialog.
                                    // finish();
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

}
