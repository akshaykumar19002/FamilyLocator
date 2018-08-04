package com.kumar.akshay.familylocator.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.kumar.akshay.familylocator.MessageClasses.GroupMessage;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;

import static com.kumar.akshay.familylocator.Database.FamilyDBContract.*;


public class DBHelper extends SQLiteOpenHelper {


    public static final String TAG = "FamilyDatabase";
    SQLiteDatabase sqLiteDatabase;
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        sqLiteDatabase = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Locations Table
        db.execSQL("CREATE TABLE " + LOCATIONS_TABLE_NAME + " ( " + LOCATION_ID_COLUMN + " TEXT, " + LOCATIONS_COLUMN_USER
                + " TEXT, " + LOCATIONS_COLUMN_TIME + " TEXT, " + LOCATIONS_COLUMN_LOCATION + " TEXT)");

        //Users Table
        db.execSQL("CREATE TABLE " + USERS_TABLE_NAME + " ( " + USER_UID + " TEXT, " + USER_COLUMN_USER_NAME
                + " TEXT, " + USER_COLUMN_USER_EMAIL + " TEXT, " + USER_COLUMN_CURRENT_USER + " INTEGER)");

        //GeofenceName Table
        db.execSQL("CREATE TABLE " + GEOFENCE_LIST_TABLE_NAME + " ( " + GEOFENCE_COLUMN_ID + " TEXT, " + GEOFENCE_NAME_COLUMN
                + " TEXT, " + GEOFENCE_UID_COLUMN + " TEXT, " + GEOFENCE_LATITUDE_COLUMN + " TEXT, " + GEOFENCE_LONGITUDE_COLUMN + " TEXT)");

        db.execSQL("CREATE TABLE " + GROUPS_TABLE_NAME + " ( " + GROUPS_COLUMN_ID + " TEXT, " + GROUPS_NAME_COLUMN
                + " TEXT, " + GROUPS_ADMIN_NAME_COLUMN + " TEXT, " + GROUPS_USERS_COLUMN + " TEXT)");

        Log.v(TAG, "Database Created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    //Users TABLE Methods
    public void insertDataIntoUsersTable(String userId, String userName, String userEmail, int currentUser) {
        ContentValues cv = new ContentValues();
        cv.put(USER_UID, userId);
        cv.put(USER_COLUMN_USER_NAME, userName);
        cv.put(USER_COLUMN_USER_EMAIL, userEmail);
        cv.put(USER_COLUMN_CURRENT_USER, currentUser);
        long result = sqLiteDatabase.insert(USERS_TABLE_NAME, null, cv);
        if (result >= 0) {
            Log.v(TAG, "Data inserted successfully");
        }
    }

    public void removeAUser(String uid) {
        sqLiteDatabase.delete(USERS_TABLE_NAME, USER_UID + " = ?", new String[]{uid});
        Log.v(TAG, "Specified location removed from database");
    }

    public Cursor getUserTableData() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + USERS_TABLE_NAME, null);
        Log.v(TAG, "User data retrieved from database");
        return cursor;
    }

    public void recreatingTables() {
        Log.v(TAG, "Clearing database tables");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LOCATIONS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GEOFENCE_LIST_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public String[] getAllUserIds() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + USER_UID + " FROM " + USERS_TABLE_NAME, null);
        int count = cursor.getCount();
        Log.v(TAG, "Count_user = " + count);
        String userIds[] = new String[count];
        if (count == 0) {
            return null;
        } else {
            int i = 0;
            while (cursor.moveToNext()) {
                userIds[i] = cursor.getString(0);
                i++;
            }
            Log.v(TAG, userIds.toString());
            return userIds;
        }
    }

    public String getUserNameFromUserId(String userid) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + USER_COLUMN_USER_NAME + " FROM " + USERS_TABLE_NAME + " WHERE " + USER_UID + " = ?", new String[]{userid});
        if (cursor.getCount() > 0 && cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }

    public boolean checkIfCurrentUser(String uid) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + USER_COLUMN_CURRENT_USER + " FROM " + USERS_TABLE_NAME + " WHERE " + USER_UID + " = ?", new String[]{uid});
        if (cursor.getCount() == 1 && cursor.moveToNext()) {
            if (cursor.getString(0).equals(uid))
                return false;
        }
        return true;
    }

    public boolean verifyUserId(String uid){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + USER_UID + " FROM " + USERS_TABLE_NAME, null);
        if (cursor.moveToNext()) {
            if (uid.equals(cursor.getString(0)))
                return false;
        }
        return true;
    }

    public UserMessage getUserMessageFromUserId(String uid){
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + USERS_TABLE_NAME + " WHERE " + USER_UID + " =? ", new String[]{uid});
        if (cursor.moveToNext()){
            return new UserMessage(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        }
        return null;
    }

    ///////
    /////////
    ///////////
    //Location Table methods
    public void insertDataIntoLocationsTable(String location_id, String location_uid, String location_time, String location) {
        ContentValues cv = new ContentValues();
        cv.put(LOCATION_ID_COLUMN, location_id);
        cv.put(LOCATIONS_COLUMN_USER, location_uid);
        cv.put(LOCATIONS_COLUMN_TIME, location_time);
        cv.put(LOCATIONS_COLUMN_LOCATION, location);
        long result = sqLiteDatabase.insert(LOCATIONS_TABLE_NAME, null, cv);
        if (result >= 0) {
            Log.v(TAG, "Location Inserted Successfully");
        }
    }

    public void removeASpecificUserLocation(String location_id) {
        sqLiteDatabase.delete(LOCATIONS_TABLE_NAME, "LOCATION_ID = ?", new String[]{location_id});
        Log.v(TAG, "Specified location removed from database");
    }

    public Cursor getLocationTableDataForSpecificUser(String uid) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + LOCATIONS_TABLE_NAME + " where " +
                LOCATIONS_COLUMN_USER + " = ?  ORDER BY " + LOCATION_ID_COLUMN + " desc", new String[]{uid});
        Log.v(TAG, "Location data retrieved from database");
        return cursor;
    }

    public String[] getAllLocationIds() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + LOCATION_ID_COLUMN + " FROM " + LOCATIONS_TABLE_NAME, null);
        int count = cursor.getCount();
        Log.v(TAG, "Count = " + count);
        String locationIds[] = new String[count];
        if (count == 0) {
            return new String[]{"No data in database"};
        } else {
            int i = 0;
            while (cursor.moveToNext()) {
                locationIds[i] = cursor.getString(0);
                i++;
            }
            Log.v(TAG, locationIds.toString());
            return locationIds;
        }
    }

    public boolean verifyLocation(String id) {
        String[] ids = getAllLocationIds();
        for (String lid : ids) {
            if (id.equals(lid))
                return false;
        }
        return true;
    }

    ///////
    /////
    //////////
    ////////
    //Geofence List Table
    public void insertDataIntoGeofenceList(String geofenceId, String geofence_name, String uid, String lat, String lng) {
        ContentValues cv = new ContentValues();
        cv.put(GEOFENCE_COLUMN_ID, geofenceId);
        cv.put(GEOFENCE_NAME_COLUMN, geofence_name);
        cv.put(GEOFENCE_UID_COLUMN, uid);
        cv.put(GEOFENCE_LATITUDE_COLUMN, lat);
        cv.put(GEOFENCE_LONGITUDE_COLUMN, lng);
        long result = sqLiteDatabase.insert(GEOFENCE_LIST_TABLE_NAME, null, cv);
        if (result >= 0) {
            Log.v(TAG, "Geofence Inserted Successfully");
        }
    }

    public Cursor getAllGeofences() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + GEOFENCE_LIST_TABLE_NAME, null);
        Log.v(TAG, "Got all the geofences");
        if (cursor.getCount() > 0)
            return cursor;
        return null;
    }

    public boolean verifyGeofence(String gId, String gName, String lat, String lng) {
        Log.v(TAG, gId+gName+lat+lng);
        Cursor cursor = getAllGeofences();
        if (cursor != null)
            while (cursor.moveToNext()) {
            Log.v(TAG, "geofence:  " + cursor.getString(0)+cursor.getString(1)+cursor.getString(3)+cursor.getString(4));
                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String latittude = cursor.getString(3);
                String lo = cursor.getString(4);
                if (id != null && name != null && latittude != null && lo != null)
                    if (id.equals(gId) && name.equals(gName) && latittude.equals(lat) && lo.equals(lng)) {
                        return false;
                    }
            }
        return true;
    }

    public String[] getAllGeofenceNames() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + GEOFENCE_NAME_COLUMN + " FROM " + GEOFENCE_LIST_TABLE_NAME, null);
        int count = cursor.getCount();
        Log.v(TAG, "Count = " + count);
        String locationIds[] = new String[count];
        if (count == 0) {
            return new String[]{"No data in database"};
        } else {
            int i = 0;
            while (cursor.moveToNext()) {
                locationIds[i] = cursor.getString(0);
                i++;
            }
            Log.v(TAG, locationIds.toString());
            return locationIds;
        }
    }

    public void removeGeofence(String geofenceName, String lat, String lng) {
        int result = sqLiteDatabase.delete(GEOFENCE_LIST_TABLE_NAME, GEOFENCE_NAME_COLUMN + " = ? and " + GEOFENCE_LATITUDE_COLUMN + " = ? and " +
                GEOFENCE_LONGITUDE_COLUMN + " = ?", new String[]{geofenceName, lat, lng});
        if (result > 0) {
            Toast.makeText(context, "Geofence Removed", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Geofence not removed", Toast.LENGTH_LONG).show();
    }


    public void insertDummyDataIntoGeofenceList(SQLiteDatabase db, String geofence_name, String lat, String lng) {
        ContentValues cv = new ContentValues();
        cv.put(GEOFENCE_NAME_COLUMN, geofence_name);
        cv.put(GEOFENCE_LATITUDE_COLUMN, lat);
        cv.put(GEOFENCE_LONGITUDE_COLUMN, lng);
        long result = db.insert(GEOFENCE_LIST_TABLE_NAME, null, cv);
        if (result >= 0) {
            Log.v(TAG, "Geofence Inserted Successfully");
        }
    }

    ////
    ///
    ///
    //Groups table
    public void insertGroupIntoGroupsTable(String gid, String gName, String gAdminName, String users) {
        ContentValues cv = new ContentValues();
        cv.put(GROUPS_COLUMN_ID, gid);
        cv.put(GROUPS_NAME_COLUMN, gName);
        cv.put(GROUPS_ADMIN_NAME_COLUMN, gAdminName);
        cv.put(GROUPS_USERS_COLUMN, users);
        long result = sqLiteDatabase.insert(GROUPS_TABLE_NAME, null, cv);
        if (result != -1) {
            Log.v(TAG, "Group inserted successfully");
        }
    }

    public void removeGroupFromGroupsTable(String gId) {
        int result = sqLiteDatabase.delete(GROUPS_TABLE_NAME, GROUPS_COLUMN_ID + " = ?", new String[]{gId});
        if (result > 0) {
            Log.v(TAG, "Group removed successfully");
        }
    }

    public Cursor getAllGroupsFromGroupTable() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + GROUPS_TABLE_NAME, null);
        Log.v(TAG, "Groups list retrieved from database");
        return cursor;
    }

    public String[] getAllGroupNamesFromGroupTable() {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + GROUPS_NAME_COLUMN + " FROM " + GROUPS_TABLE_NAME, null);
        int count = cursor.getCount();
        Log.v(TAG, "Count_groups = " + count);
        String groups[] = new String[count];
        if (count == 0) {
            return new String[]{"No data in database"};
        } else {
            int i = 0;
            while (cursor.moveToNext()) {
                groups[i] = cursor.getString(0);
                i++;
            }
            Log.v(TAG, groups.toString());
            return groups;
        }
    }

    public boolean checkIfGroupExists(String gName) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT " + GROUPS_NAME_COLUMN + " FROM " + GROUPS_TABLE_NAME + " WHERE " + GROUPS_NAME_COLUMN + " = ?", new String[]{gName});
        if (cursor.getCount() > 0) {
            return true;
        } else
            return false;
    }

    public boolean verifyGroup(GroupMessage gm) {
        Cursor cursor = getAllGroupsFromGroupTable();
        while (cursor.moveToNext()) {
            if (gm.getGroupid().equals(cursor.getString(0)) && gm.getGroupname().equals(cursor.getString(1))) {
                return false;
            }
        }
        return true;
    }
}
