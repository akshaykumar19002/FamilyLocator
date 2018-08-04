package com.kumar.akshay.familylocator.NotifyingLocation;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kumar.akshay.familylocator.LauncherActivity;
import com.kumar.akshay.familylocator.R;
import java.util.Locale;
import java.util.StringTokenizer;

public class NotifyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String mUsername;
    GoogleMap googleMap;
    boolean mapReady = false;
    private String TAG = "NotifyActivity";
    double latitude, longitude;
    String location, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_layout);
        mUsername = getIntent().getExtras().getString("username");
        setTitle("Loading location for " + mUsername);
        location = getIntent().getExtras().getString("location");
        time = getIntent().getExtras().getString("time");


        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        StringTokenizer stringTokenizer = new StringTokenizer(location, ",");
        String[] str = new String[2];
        str[1] = str[0] = null;
        int i = 0;
        while (stringTokenizer.hasMoreElements()) {
            str[i] = stringTokenizer.nextToken();
            Toast.makeText(NotifyActivity.this, str[i], Toast.LENGTH_LONG).show();
            i++;
        }
        if (str[0] != null && str[1] != null) {
            latitude = Double.parseDouble(str[0]);
            longitude = Double.parseDouble(str[1]);
        } else {
            Toast.makeText(NotifyActivity.this, "Can't extract lat and long", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapReady = true;
        this.googleMap = googleMap;

        //Adding  a circle
        LatLng currentLocation = new LatLng(latitude, longitude);
        googleMap.addCircle(new CircleOptions().radius(50).center(currentLocation).strokeColor(Color.BLUE).fillColor(Color.argb(65, 0, 0, 255)));
        CameraPosition target = CameraPosition.builder().target(currentLocation).zoom(14).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notify_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open_in_map_menu:
                String uri = String.format(Locale.ENGLISH, "geo:%f%f?z=10", latitude, longitude);
                Uri gmmIntentUri = Uri.parse(uri);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
                return true;
            case R.id.sign_out_menu:
                //AuthUI.getInstance().signOut(this);
                return true;
            case R.id.info_button:
                Toast.makeText(this, "It shows the last known location of the requested user", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, LauncherActivity.class);
        startActivity(i);
    }
}
