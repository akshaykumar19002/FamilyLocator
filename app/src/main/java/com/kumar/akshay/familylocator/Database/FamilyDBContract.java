package com.kumar.akshay.familylocator.Database;

import android.provider.BaseColumns;

public final class FamilyDBContract implements BaseColumns {

    //Database Name
    public static final String DATABASE_NAME = "OUR_FAMILY_LOCATOR_DB";

    //Database Version
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_URL = "https://akshayakshaykumar929.000webhostapp.com/";

    //Tables' name
    public static final String LOCATIONS_TABLE_NAME = "LOCATION_TABLE";
    public static final String USERS_TABLE_NAME = "USERS_TABLE";
    public static final String GEOFENCE_LIST_TABLE_NAME = "GEOFENCES_LIST";
    public static final String GROUPS_TABLE_NAME = "GROUPS";
    public static final String REQUESTS_TABLE_NAME = "REQUESTS";

    //Columns in locations tableA
    public static final String LOCATION_ID_COLUMN = "LOCATION_ID";
    public static final String LOCATIONS_COLUMN_LOCATION = "LOCATION";
    public static final String LOCATIONS_COLUMN_USER = "LOCATION_USER_ID";
    public static final String LOCATIONS_COLUMN_TIME = "LOCATION_TIME";

    //Columns in users table
    public static final String USER_UID = "UID";
    public static final String USER_COLUMN_USER_EMAIL = "USER_EMAIL";
    public static final String USER_COLUMN_USER_NAME = "USER_NAME";
    public static final String USER_COLUMN_USER_PASSWORD = "USER_PASSWORD";
    public static final String USER_COLUMN_CURRENT_USER = "CURRENT_USER";

    //Columns in geofences table
    public static final String GEOFENCE_COLUMN_ID = "GEOFENCE_ID";
    public static final String GEOFENCE_NAME_COLUMN = "GEOFENCE_NAME";
    public static final String GEOFENCE_UID_COLUMN = "GEOFENCE_UID";
    public static final String GEOFENCE_LATITUDE_COLUMN = "GEOFENCE_LATITUDE";
    public static final String GEOFENCE_LONGITUDE_COLUMN = "GEOFENCE_LONGITUDE";

    //Columns in Groups table
    public static final String GROUPS_COLUMN_ID = "GROUP_ID";
    public static final String GROUPS_NAME_COLUMN = "GROUP_NAME";
    public static final String GROUPS_ADMIN_NAME_COLUMN = "GROUP_ADMIN_NAME";
    public static final String GROUPS_USERS_COLUMN = "GROUP_USERS";

    //Columns for request table
    public static final String REQUEST_COLUMN_ID = "REQUEST_ID";
    public static final String REQUEST_INIT_UID = "REQUEST_INIT_UID";
    public static final String REQUEST_GID = "REQUEST_GID";
    public static final String REQUEST_END_UID = "REQUEST_END_UID";

}
