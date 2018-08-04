package com.kumar.akshay.familylocator;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kumar.akshay.familylocator.AdapterClasses.MessageAdapter;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.LocationMessage;
import com.kumar.akshay.familylocator.MessageClasses.NotifyMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShowLocations extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "ShowLocations";
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private String mUserID,currentUserName, locat = null;
    private Button getCurrentLcation;
    private SwipeRefreshLayout swipeRefreshLayout;
    FamilyDatabase fdb;
    DBHelper db;
    View view;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_message_layout, container, false);
        Bundle bundle = getArguments();
        fdb = FamilyDatabase.getmInstance(context);
        db = new DBHelper(context);
        mUserID = bundle.getString("uid");
        getCurrentLcation = view.findViewById(R.id.get_current_user_location);
        mProgressBar = view.findViewById(R.id.progressBar);
        mMessageListView = view.findViewById(R.id.message_list_view);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        // Initialize message ListView and its adapter
        mMessageAdapter = new MessageAdapter(context, R.layout.item_message);
        gettingDataFromDatabase();
        return view;
    }

    public void gettingDataFromDatabase(){
//        Map<String, String>map = new HashMap<>();
//        map.clear();
//        map.put("uid", mUserID);
//        fdb.addToRequestQueue("getLocations", map);
        DBHelper db = new DBHelper(context);
        Cursor cursor = db.getLocationTableDataForSpecificUser(mUserID);
        mMessageAdapter.clear();
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                mMessageAdapter.add(new LocationMessage(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
                mProgressBar.setVisibility(View.GONE);
            }
        } else {
            mMessageAdapter.clear();
            mMessageListView.setOnItemClickListener(null);
            mMessageListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"No geofence to display", "Please add one"}));
            mProgressBar.setVisibility(View.GONE);
        }
        mMessageListView.setAdapter(mMessageAdapter);
    }

    public void getCurrentUserLocation(View view) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            NotifyMessage notifyMessage = new NotifyMessage(mUserID, getResources().
                    getString(R.string.get_location_message), "needed",
                    formattedDate,LauncherActivity.mUserid, LauncherActivity.mUsername);
            Log.v(TAG, formattedDate+LauncherActivity.mUsername);
            //mMessagesDatabaseReference.push().setValue(notifyMessage);
    }

    @Override
    public void onRefresh() {
        mMessageAdapter.clear();
        Log.v(TAG, "Clearing data");
        gettingDataFromDatabase();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}