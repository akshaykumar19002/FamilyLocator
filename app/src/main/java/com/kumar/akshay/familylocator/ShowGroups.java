package com.kumar.akshay.familylocator;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kumar.akshay.familylocator.AdapterClasses.*;
import com.kumar.akshay.familylocator.AdapterClasses.UserAdapter;
import com.kumar.akshay.familylocator.Database.DBHelper;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

public class ShowGroups extends AppCompatActivity {

    String [] users;
    private static final String TAG = "ShowLocations";
    private ListView mUserListView;
    private UserAdapter mUserAdapter;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    FamilyDatabase fdb;
    DBHelper db;
    Context context;
    String group_id = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        getSupportActionBar().setTitle(getIntent().getStringExtra("group_name"));
        setContentView(R.layout.activity_show_groups);
        Intent intent = getIntent();
        mProgressBar = findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.GONE);
        db = new DBHelper(context);
        mUserListView = findViewById(R.id.message_list_view);
        mUserAdapter = new UserAdapter(context, R.layout.user_message_layout);
        users = intent.getStringExtra("usersInGroup").split("$");
        group_id = intent.getStringExtra("groupId");
        swipeRefreshLayout = findViewById(R.id.swipe_container);
        mUserListView.setAdapter(mUserAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        for (String id : users){
            mUserAdapter.add(db.getUserMessageFromUserId(id));
        }
    }

    public void addUsers(View view) {
        AddUserDialogFragment addUserDialogFragment = new AddUserDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("groupId", group_id);
        addUserDialogFragment.setArguments(bundle);
        addUserDialogFragment.show(getSupportFragmentManager(), "AddUsersDialogFragment");
    }

}
