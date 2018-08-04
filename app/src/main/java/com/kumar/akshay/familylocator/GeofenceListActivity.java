package com.kumar.akshay.familylocator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.kumar.akshay.familylocator.AdapterClasses.GeofenceListAdapter;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.GeofenceMessage;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

import static android.view.View.GONE;

public class GeofenceListActivity extends AppCompatActivity{

    FamilyDatabase fdb;
    SwipeRefreshLayout swipeRefreshLayout;
    GeofenceListAdapter geofenceListAdapter;
    ListView geofenceListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_list);
        fdb = FamilyDatabase.getmInstance(GeofenceListActivity.this);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
        geofenceListView = (ListView)findViewById(R.id.goefenceListView);
        geofenceListAdapter = new GeofenceListAdapter(this, R.layout.geofence_list_message, null);
        gettingData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                geofenceListAdapter.clear();
                geofenceListView.setOnItemClickListener(null);
                gettingData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void gettingData(){
//        Cursor cursor = fdb.getAllGeofences();
//        if (cursor.getCount() != 0){
//            while (cursor.moveToNext()){
//                geofenceListAdapter.add(new GeofenceMessage(cursor.getString(1),cursor.getString(2),cursor.getString(3)));
//            }
//            geofenceListView.setAdapter(geofenceListAdapter);
////            geofenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    UserMessage userMessage = (UserMessage)geofenceListView.getAdapter().getItem(position);
////                    String username = userMessage.getUsername();
////                    Intent i = new Intent(GeofenceListActivity.this, ShowLocations.class);
////                    i.putExtra("uid",userMessage.getUid());
////                    i.putExtra("Username", username);
////                    startActivity(i);
////                }
////            });
//        }else {
//            geofenceListView.setOnItemClickListener(null);
//            geofenceListView.setAdapter(new ArrayAdapter<>(GeofenceListActivity.this, android.R.layout.simple_list_item_1, new String[]{"No geofence to display", "Please add one"}));
//        }
    }
}
