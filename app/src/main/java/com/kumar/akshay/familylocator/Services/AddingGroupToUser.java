package com.kumar.akshay.familylocator.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import com.kumar.akshay.familylocator.Database.FamilyDatabase;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

public class AddingGroupToUser extends IntentService {

    private String TAG = "AddingGroupToUser";
    private String mUsername = "Anonymous", mUserID = null, mUserEmail = null;
    public boolean newUser = true;
    FamilyDatabase fdb;

    public AddingGroupToUser() {
        super("AddingGroupToUser");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mUsername = intent.getExtras().getString("UserName");
        mUserEmail = intent.getExtras().getString("UserEmail");
        mUserID = intent.getExtras().getString("UserID");
        fdb = FamilyDatabase.getmInstance(AddingGroupToUser.this);
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        Log.v(TAG, "Database Read Listener Added Successfully");
//        if (mChildEventListener == null) {
//            mChildEventListener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Log.v(TAG, "Got a user child");
//                    UserMessage userMessage = dataSnapshot.getValue(UserMessage.class);
//                    if (mUserID.equals(userMessage.getUid())) {
//                        //String groups = fdb.getAllGroups(userMessage.getUid());
////                        Log.v(TAG, "Found a current user child, " + groups + ",,,," + userMessage.getGroupsToWhichUserBelongs());
////                        if (!groups.equals(userMessage.getGroupsToWhichUserBelongs()) && !groups.equals("NIL")) {
////                            userMessage.setGroupsToWhichUserBelongs(groups);
////                            mDatabaseReference.child(dataSnapshot.getKey()).removeValue();
////                            mDatabaseReference.push().setValue(userMessage);
////                            Log.v(TAG, "User group updated");
////                        }
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

    public void detachDatabaseReadListener() {
        Log.v(TAG, "Database Read Listener Removed Successfully");
//        if (mChildEventListener != null) {
//            mDatabaseReference.removeEventListener(mChildEventListener);
//            mChildEventListener = null;
//        }
    }
}
