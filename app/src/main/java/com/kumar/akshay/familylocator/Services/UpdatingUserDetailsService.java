package com.kumar.akshay.familylocator.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.UserCountMessage;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

public class UpdatingUserDetailsService extends IntentService {

    private String mUsername = "Anonymous", mUserID = null, mUserEmail = null;
    public boolean newUser = true;
    public int user_count = 1;
    private String user_count_key = null;
    int user_count_temp;
    String[] userIdsArray;
    FamilyDatabase fdb;

    private String TAG = "UpdatingUserDetails";

    private final IBinder mIBinder = new UpdatingUserDetailsServiceBinder();

    private UserDetailsChangeListener listener;

    public UpdatingUserDetailsService() {
        super("updatingUserDetails");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference().child("users");
//        mDatabaseReference2 = mFirebaseDatabase.getReference().child("user_count");
        mUsername = intent.getExtras().getString("UserName");
        mUserEmail = intent.getExtras().getString("UserEmail");
        mUserID = intent.getExtras().getString("UserID");
        fdb = FamilyDatabase.getmInstance(UpdatingUserDetailsService.this);
        attachDatabaseUserCountReadListener();
    }

    private void attachDatabaseUserCountReadListener() {
//        Log.v(TAG, "Database Count Read Listener Added Successfully");
//        if (mChildEventListener2 == null) {
//            mChildEventListener2 = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    UserCountMessage userCountMessage = dataSnapshot.getValue(UserCountMessage.class);
//                    user_count = Integer.parseInt(userCountMessage.getUser_count());
//                    Log.v(TAG, "user_count = " + user_count);
//                    user_count_key = dataSnapshot.getKey();
//                    user_count_temp = user_count;
//                    //userIdsArray = fdb.getAllUserIds();
//                    attachDatabaseReadListener();
//                    detachDatabaseUserCountReadListener();
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//            mDatabaseReference2.addChildEventListener(mChildEventListener2);
//        }

    }

    private void attachDatabaseReadListener() {
        Log.v(TAG, "Database Read Listener Added Successfully");
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    listener.onChange(true);
//                    UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
//                    int user_count_temp_dec = decrementUserCount();
//                    Log.v(TAG, "user_count_temp_dec = " + user_count_temp_dec);
//                    boolean isPresent = false;
//                    if (!userIdsArray[0].equals("No data in database")) {
//                        for (String usid : userIdsArray) {
//                            if (usid.equals(userMessage.getUid())) {
//                                isPresent = true;
//                            }
//                        }
//                    }
//                    if (!isPresent)
//                        //fdb.insertDataIntoUsersTable(userMessage.getUid(), userMessage.getUsername(), userMessage.getUserEmail(), userMessage.getGroupsToWhichUserBelongs());
//
//                    if (mUsername.equals(userMessage.getUsername()) && mUserEmail.equals(userMessage.getUserEmail())
//                            && mUserID.equals(userMessage.getUid())) {
//                        newUser = false;
//                    }
//                    if (user_count_temp_dec == 0) {
//                        detachDatabaseReadListener();
//                    }
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
//                    //fdb.removeAUser(userMessage.getUid());
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//            mDatabaseReference.addChildEventListener(mChildEventListener);
//        }
    }

    private int decrementUserCount() {
        user_count_temp = user_count_temp - 1;
        return user_count_temp;
    }

    public void detachDatabaseReadListener() {
        Log.v(TAG, "Database Read Listener Removed Successfully");
    }

    private void detachDatabaseUserCountReadListener() {
        Log.v(TAG, "Database Count Listener Removed Successfully");
    }

    public void updateDetails() {
        Log.v(TAG, "newUser = " + newUser);
        if (newUser) {
            Log.v(TAG, "Updating details");
            UserMessage userMessage = new UserMessage(mUsername, mUserID, "NIL", mUserEmail);
//            mDatabaseReference.push().setValue(userMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    mDatabaseReference2.child(user_count_key).removeValue();
//                    mDatabaseReference2.push().setValue(new UserCountMessage(String.valueOf(user_count + 1)));
//                }
//            });
        }
    }

    public class UpdatingUserDetailsServiceBinder extends Binder{

        public UpdatingUserDetailsService getService(){
            return UpdatingUserDetailsService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference().child("users");
//        mDatabaseReference2 = mFirebaseDatabase.getReference().child("user_count");
//        mUsername = intent.getExtras().getString("UserName");
//        mUserEmail = intent.getExtras().getString("UserEmail");
//        mUserID = intent.getExtras().getString("UserID");
//        fdb = new FamilyDatabase(UpdatingUserDetailsService.this);
//        attachDatabaseUserCountReadListener();
        return mIBinder;
    }

    public UserDetailsChangeListener getListener(){
        return listener;
    }

    public void setListener(UserDetailsChangeListener changeListener){
        this.listener = changeListener;
    }

    public interface UserDetailsChangeListener{
        void onChange(boolean gotUsers);
    }

}
