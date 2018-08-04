package com.kumar.akshay.familylocator;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.kumar.akshay.familylocator.AdapterClasses.UserAdapter;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

import static android.view.View.GONE;

public class UsersList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ListView mMessageListView;
    ProgressBar mProgressBar;
    UserAdapter mUserAdapter;
    String TAG = getClass().getSimpleName();
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_layout);
        mMessageListView = (ListView)findViewById(R.id.message_list_view);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mUserAdapter = new UserAdapter(this, R.layout.message_adapter_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        gettingData();
        getSupportActionBar().setTitle("List of Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gettingData(){
//        Cursor cursor = new FamilyDatabase(UsersList.this).getUserTableData();
//        if (cursor.getCount() != 0){
//            while (cursor.moveToNext()){
//                mUserAdapter.add(new UserMessage(cursor.getString(1),cursor.getString(0),cursor.getString(3),cursor.getString(2)));
//            }
//            mMessageListView.setAdapter(mUserAdapter);
//            mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    UserMessage userMessage = (UserMessage)mMessageListView.getAdapter().getItem(position);
//                    String username = userMessage.getUsername();
//                    Intent i = new Intent(UsersList.this, ShowLocations.class);
//                    i.putExtra("uid",userMessage.getUid());
//                    i.putExtra("Username", username);
//                    startActivity(i);
//                }
//            });
//            mProgressBar.setVisibility(GONE);
//        }else {
//            mMessageListView.setOnItemClickListener(null);
//            mProgressBar.setVisibility(GONE);
//            mMessageListView.setAdapter(new ArrayAdapter<>(UsersList.this, android.R.layout.simple_list_item_1, new String[]{"No data to display"}));
//        }
    }

    @Override
    public void onRefresh() {
        mUserAdapter.clear();
        mMessageListView.setOnItemClickListener(null);
        gettingData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
