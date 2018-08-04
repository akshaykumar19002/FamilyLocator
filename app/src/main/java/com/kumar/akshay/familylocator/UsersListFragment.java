package com.kumar.akshay.familylocator;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.kumar.akshay.familylocator.AdapterClasses.UserAdapter;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView mMessageListView;
    ProgressBar mProgressBar;
    UserAdapter mUserAdapter;
    String TAG = getClass().getSimpleName();
    SwipeRefreshLayout swipeRefreshLayout;
    Context context = null;
    private static View view;

    FamilyDatabase fdb;
    DBHelper db;

    public UsersListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            view = inflater.inflate(R.layout.user_message_layout, container, false);
            if (context != null) {
                mMessageListView = view.findViewById(R.id.message_list_view);
                mProgressBar = view.findViewById(R.id.progressBar);
                swipeRefreshLayout = view.findViewById(R.id.swipe_container);
                fdb = FamilyDatabase.getmInstance(context);
                db = new DBHelper(context);
                mUserAdapter = new UserAdapter(context, R.layout.message_adapter_layout);
                swipeRefreshLayout.setOnRefreshListener(this);
                mMessageListView.setAdapter(mUserAdapter);
                mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        UserMessage userMessage = (UserMessage) mMessageListView.getAdapter().getItem(position);
                        String username = userMessage.getUsername();
                        Intent i = new Intent(context, ShowLocations.class);
                        i.putExtra("uid", userMessage.getUid());
                        i.putExtra("Username", username);
                        startActivity(i);
                    }
                });
                if (LauncherActivity.internetConnectivity)
                    gettingData();
            }
        } catch (InflateException e) {
            Log.e(TAG, e.toString());
        }
        return view;
    }

    public void attachDatabaseReadListener() {
//        Log.v(TAG, "Database Read Listener Added Successfully");
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
//                    mUserAdapter.add(userMessage);
//                    mProgressBar.setVisibility(View.GONE);
//                    Log.v(TAG, "User added to list");
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    FragmentManager fm = getFragmentManager();
//                    FragmentTransaction transaction = fm.beginTransaction();
//                    transaction.replace(getId(), new UsersListFragment());
//                    transaction.commit();
//                    onDestroyView();
//                    new UsersListFragment();
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            };
//            mDatabaseReference.addChildEventListener(mChildEventListener);
//        }
    }

    private void gettingData() {
        Map<String,String> map = new HashMap<>();
        map.clear();
        map.put("uid", LauncherActivity.mUserid);
        fdb.addToRequestQueue("getUsers", map);
//        Cursor cursor = new FamilyDatabase(context).getUserTableData();
//        if (cursor.getCount() != 0) {
//            while (cursor.moveToNext()) {
//                mUserAdapter.add(new UserMessage(cursor.getString(1), cursor.getString(0), cursor.getString(3), cursor.getString(2)));
//                mProgressBar.setVisibility(View.GONE);
//            }
//        } else {
//            mMessageListView.setOnItemClickListener(null);
//            mProgressBar.setVisibility(View.GONE);
//            mMessageListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"No data to display"}));
//        }
    }

    @Override
    public void onRefresh() {
//        if (LauncherActivity.internetConnectivity) {
//            firebaseDatabase = FirebaseDatabase.getInstance();
//            mDatabaseReference = firebaseDatabase.getReference().child("users");
//            attachDatabaseReadListener();
//        } else {
//            mProgressBar.setVisibility(View.VISIBLE);
//            mUserAdapter.clear();
//            gettingData();
//        }
//        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

}
