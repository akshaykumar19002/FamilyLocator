package com.kumar.akshay.familylocator.Maps;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kumar.akshay.familylocator.R;

import static com.kumar.akshay.familylocator.R.id.map;

public class GeoFenceMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "GeoFenceMapActivity";
    GoogleMap googleMap;
    boolean mapReady = false;
    Button btnMap, btnSatellite, btnHybrid;
    Marker marker = null;
    ActionBar actionBar;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    AutoCompleteTextView autoCompleteTextView;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geofence_map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        View view = inflater.inflate(R.layout.autocomplete_layout, null);
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.search_box);
        actionBar.setCustomView(view, layoutParams);
        autoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mAdapter = new PlaceAutoCompleteAdapter(this, mGoogleApiClient, BOUNDS_GREATER_SYDNEY,
                null);
        autoCompleteTextView.setAdapter(mAdapter);
        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
            }
        });

        btnMap = (Button) findViewById(R.id.btnMap);
        btnHybrid = (Button) findViewById(R.id.btnHybrid);
        btnSatellite = (Button) findViewById(R.id.btnSatellite);

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        btnSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        btnHybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mapReady)
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


        }
    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mapReady = true;
        this.googleMap = googleMap;
        CameraPosition target = CameraPosition.builder().target(new LatLng(20.8919355, 73.7488046)).zoom(3).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null)
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng));
                else {
                    Toast.makeText(GeoFenceMapActivity.this, "Only one marker should be selected. Long Click to remove the previous marker", Toast.LENGTH_LONG).show();
                }
            }
        });
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if (marker != null) {
                    marker.remove();
                    marker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng));
                }
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker1) {
                marker1.remove();
                marker = null;
                return false;
            }
        });
    }

    public void addLatLong(View view) {
        if (marker != null) {
            LatLng latLng = marker.getPosition();
            Intent intent = new Intent();
            intent.putExtra("LatLng", latLng);
            intent.putExtra("geofenceName", getIntent().getStringExtra("geofenceName"));
            setResult(RESULT_OK, intent);
            finish();
        } else
            Toast.makeText(GeoFenceMapActivity.this, "Please select a marker at which geofence to be created...", Toast.LENGTH_LONG).show();
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            CameraPosition cameraPosition = CameraPosition.builder().target(place.getLatLng()).zoom(14).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            places.release();
        }
    };

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}