package com.kumar.akshay.familylocator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kumar.akshay.familylocator.AdapterClasses.GroupsListAdapter;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.GroupMessage;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupsListFragment extends Fragment {

    Context context;
    FamilyDatabase fdb;
    SwipeRefreshLayout swipeRefreshLayout;
    GroupsListAdapter groupsListAdapter;
    ListView groupListView;
    ProgressBar mProgressBar;
    View view;

    GroupListListener groupListListener;
    DBHelper db;

    public static String TAG = "GroupListFragment";
    Map<String, String> map;

    public GroupsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        map = new HashMap<>();
        view = inflater.inflate(R.layout.activity_group_list, null);
        fdb = FamilyDatabase.getmInstance(context);
        db = new DBHelper(context);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mProgressBar =  view.findViewById(R.id.progress_bar_group_list);
        mProgressBar.setVisibility(View.VISIBLE);
        groupListView = view.findViewById(R.id.groupListView);

        List<GroupMessage> friendlyMessages = new ArrayList<>();
        groupsListAdapter = new GroupsListAdapter(context, R.layout.geofence_list_message, friendlyMessages);
        groupListView.setAdapter(groupsListAdapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GroupMessage groupMessage = (GroupMessage) groupListView.getItemAtPosition(i);
                Intent intent = new Intent(context, ShowGroups.class);
                intent.putExtra("usersInGroup", groupMessage.getUsersInGroup());
                context.startActivity(intent);
            }
        });
        if (LauncherActivity.internetConnectivity) {
            groupListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    groupListListener.showMenuOnGroupsFragment(view, (GroupMessage) groupListView.getItemAtPosition(i));
                    return true;
                }
            });
            gettingData();
        } else
            Snackbar.make(view, "No Internet Connectivity", Snackbar.LENGTH_LONG).show();


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!LauncherActivity.internetConnectivity) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    groupsListAdapter.clear();
                    gettingData();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    public void gettingData() {
        Log.v("GroupListFragment", "Data loaded successfully");
//        map.put("uid", LauncherActivity.mUserid);
//        fdb.addToRequestQueue("getGroups", map);
        groupsListAdapter.clear();
        Cursor cursor = db.getAllGroupsFromGroupTable();
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                Log.v("GroupListFragment", cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
                groupsListAdapter.add(new GroupMessage(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GroupMessage groupMessage = (GroupMessage) groupListView.getItemAtPosition(i);
                    Intent intent = new Intent(context, ShowGroups.class);
                    intent.putExtra("usersInGroup", groupMessage.getUsersInGroup());
                    intent.putExtra("groupId", groupMessage.getGroupid());
                    intent.putExtra("group_name", groupMessage.getGroupname());
                    context.startActivity(intent);
                }
            });
            groupListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    GroupMessage groupMessage = (GroupMessage) groupListView.getItemAtPosition(i);
                    groupListListener.showMenuOnGroupsFragment(view, groupMessage);
                    return true;
                }
            });
            mProgressBar.setVisibility(View.GONE);
        } else {
            groupListView.setOnItemClickListener(null);
            groupListView.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, new String[]{"No groups to display", "Please add one"}));
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface GroupListListener {
        boolean showMenuOnGroupsFragment(View view, GroupMessage groupMessage);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        groupListListener = (GroupListListener) activity;
    }
}

