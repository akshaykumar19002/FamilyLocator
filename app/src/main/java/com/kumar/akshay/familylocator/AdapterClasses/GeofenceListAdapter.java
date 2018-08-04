package com.kumar.akshay.familylocator.AdapterClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.kumar.akshay.familylocator.GeofenceListActivity;
import com.kumar.akshay.familylocator.MessageClasses.GeofenceMessage;
import com.kumar.akshay.familylocator.R;

import java.util.ArrayList;
import java.util.List;

public class GeofenceListAdapter extends ArrayAdapter<GeofenceMessage> {

    Context context;
    public GeofenceListAdapter(@NonNull Context context, int resource, List<GeofenceMessage> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.geofence_list_message, parent, false);
        }

        GeofenceMessage geofenceMessage = getItem(position);

        TextView geofenceNametextView = view.findViewById(R.id.geofenceNameTextView);
        geofenceNametextView.setText(geofenceMessage.getGeofenceName());

        TextView latLngtextView = view.findViewById(R.id.geofenceLatLngTextView);
        latLngtextView.setText(String.format("%.6f", Double.parseDouble(geofenceMessage.getLatitude()))+ "," + String.format("%.6f", Double.parseDouble(geofenceMessage.getLongitude())));

        return view;
    }

}
