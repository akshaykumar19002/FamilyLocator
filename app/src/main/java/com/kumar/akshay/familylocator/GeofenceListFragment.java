package com.kumar.akshay.familylocator;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kumar.akshay.familylocator.AdapterClasses.GeofenceListAdapter;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.GeofenceMessage;
import com.kumar.akshay.familylocator.Services.GeofenceListService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeofenceListFragment extends Fragment {

    Context context;
    FamilyDatabase fdb;
    SwipeRefreshLayout swipeRefreshLayout;
    GeofenceListAdapter geofenceListAdapter;
    ListView geofenceListView;
    ProgressBar mProgressBar;
    View view;
    GeofenceListListener geofenceListListener;
    public static String geofences = null;
    DBHelper db;
    Map<String, String> map;

    private static String TAG = "GeofenceListFragment";

    public interface GeofenceListListener {
        boolean showGeofenceMenuOnGeofenceFragment(View view, GeofenceMessage geofenceMessage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_geofence_list, null);
        fdb = FamilyDatabase.getmInstance(context);
        map = new HashMap<>();
        db = new DBHelper(context);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mProgressBar = view.findViewById(R.id.progress_bar_geofence_list);
        mProgressBar.setVisibility(View.VISIBLE);
        geofenceListView = view.findViewById(R.id.goefenceListView);
        List<GeofenceMessage> geofenceMessageList = new ArrayList<>();
        geofenceListAdapter = new GeofenceListAdapter(context, R.layout.geofence_list_message, geofenceMessageList);
        geofenceListView.setAdapter(geofenceListAdapter);
        geofenceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                GeofenceMessage geofenceMessage = (GeofenceMessage) geofenceListView.getItemAtPosition(i);
                geofenceListListener.showGeofenceMenuOnGeofenceFragment(view, geofenceMessage);
                return true;
            }
        });
        gettingData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                geofenceListAdapter.clear();
                gettingData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        geofenceListListener = (GeofenceListListener) activity;

    }

    public void gettingData() {
//        Log.v("GeofenceListFragment", "Data loaded successfully");
//        map.put("uid", LauncherActivity.mUserid);
//        fdb.addToRequestQueue("getGeofence", map);
        Cursor cursor = null;
        cursor = db.getAllGeofences();
        geofenceListAdapter.clear();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Log.v(TAG, cursor.getString(1) + cursor.getString(3) + cursor.getString(4));
                    geofenceListAdapter.add(new GeofenceMessage(cursor.getString(1), cursor.getString(3), cursor.getString(4)));
                }
                geofenceListView.setAdapter(geofenceListAdapter);
                geofenceListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        GeofenceMessage geofenceMessage = (GeofenceMessage) geofenceListView.getItemAtPosition(i);
                        geofenceListListener.showGeofenceMenuOnGeofenceFragment(view, geofenceMessage);
                        return true;
                    }
                });
                mProgressBar.setVisibility(View.GONE);
            }
        } else {
            geofenceListAdapter.clear();
            geofenceListView.setOnItemClickListener(null);
            geofenceListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"No geofence to display", "Please add one"}));
            mProgressBar.setVisibility(View.GONE);
        }
    }
}