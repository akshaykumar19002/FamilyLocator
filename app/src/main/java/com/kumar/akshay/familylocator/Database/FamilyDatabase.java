package com.kumar.akshay.familylocator.Database;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kumar.akshay.familylocator.Firebase.MySingleton;
import com.kumar.akshay.familylocator.LauncherActivity;
import com.kumar.akshay.familylocator.Login.LoginActivity;
import com.kumar.akshay.familylocator.MessageClasses.UserMessage;
import com.kumar.akshay.familylocator.Services.GeofenceListService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class FamilyDatabase {

    private static FamilyDatabase mInstance;
    private static Context context;
    private RequestQueue requestQueue;
    AlertDialog.Builder alertDialog;
    public GeofenceData geofenceData;
    public GroupsData groupsData;
    public GettingUsersLocationData usersLocationData;
    String messageUrl;
    String pass;
    DBHelper db;
    public String TAG = "famDB";

    private FamilyDatabase(Context context) {
        this.context = context;
        db = new DBHelper(context);
        requestQueue = getRequestQueue();
        messageUrl = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/sendMessage.php";
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized FamilyDatabase getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FamilyDatabase(context);
        }
        return mInstance;
    }

    public <T> void addToMessageRequestQueue(final String message) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, messageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("FamMessage", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                return params;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    public <T> void addToRequestQueue(final String type, final Map<String, String> data) {
        String url = null;
        switch (type) {
            case "userLogin":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/login.php";
                break;
            case "register":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/register.php";
                break;
            case "getGeofence":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/getGeofences.php";
                break;
            case "addGeofence":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/insertGeofences.php";
                break;
            case "updateLocation":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/insertLocation.php";
                break;
            case "getGroups":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/getGroups.php";
                break;
            case "createGroup":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/createGroup.php";
                break;
            case "getUsers":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/getUsers.php";
                break;
            case "gettingUserLocations":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/getLocations.php";
                break;
            case "addUserToGroup":
                url = "https://akshayakshaykumar929.000webhostapp.com/FamLoc/addUserToGroup.php";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                alertDialog = new AlertDialog.Builder(context);
                switch (type) {
                    case "userLogin":
                        alertDialog.setTitle("Login!!");
                        if (result == "failed") {
                            alertDialog.setMessage("Login failed");
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(new Intent(context, LoginActivity.class));
                                }
                            });
                            alertDialog.show();
                        } else {
                            Log.e("famLoc", result);
                            String[] data = result.split(",");
                            if (data.length > 3) {
                                UserMessage userMessage = new UserMessage(data[0], data[1], data[2]);
                                db.insertDataIntoUsersTable(data[0], data[1], data[2], 1);
                                userMessage.setUserPass(data[3]);
                                Intent intent1 = new Intent(context, GeofenceListService.class);
                                intent1.putExtra("UserName", userMessage.getUsername());
                                intent1.putExtra("uid", userMessage.getUid());
                                intent1.putExtra("UserEmail", userMessage.getUserEmail());
                                context.startService(intent1);
                                Log.v(TAG, "GeofenceListService");
                                Intent intent = new Intent(context, LauncherActivity.class);
                                intent.putExtra("uid", userMessage.getUid());
                                intent.putExtra("username", userMessage.getUsername());
                                intent.putExtra("userEmail", userMessage.getUserEmail());
                                LoginActivity.updatingPreferences(1, data[2], pass);
                                context.startActivity(intent);
                            } else {
                                Intent intent = new Intent(context, LauncherActivity.class);
                                LoginActivity.updatingPreferences(-1, "", "");
                                context.startActivity(intent);
                            }
                        }
                        break;
                    case "register":
                        alertDialog.setTitle("Register!!");
                        alertDialog.setMessage(result);
                        alertDialog.setPositiveButton("Login", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                context.startActivity(new Intent(context, LoginActivity.class));
                            }
                        });
                        alertDialog.show();
                        break;
                    case "getGeofence":
                        Log.e("famLoc", result);
                        if (result != "failed")
                            geofenceData.dataForGeofences(result);
                        break;
                    case "addGeofence":
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        break;
                    case "updateLocation":
                        if (result.equals("Location updated")) {
                            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case "getGroups":
                        if (result != "failed") {
                            groupsData.dataForGroups(result);
                        }
                        break;
                    case "createGroup":
                        alertDialog.setTitle("Group!!");
                        if (result.equals("Group created")) {
                            alertDialog.setMessage(result);
                        } else
                            alertDialog.setMessage(result);
                        alertDialog.show();
                        break;
                    case "getUsers":
                        if (result != "failed") {
                            groupsData.dataForGroups(result);
                        }
                        Log.e("famLoc", result);
                        break;
                    case "gettingUserLocations":
                        if (result != "failed") {
                            usersLocationData.dataForUsersLocations(result);
                        }
                        break;
                    case "addUserToGroup":
                        if (!result.equals("User added to the group")){
                            alertDialog.setTitle("Register!!");
                            alertDialog.setMessage(result);
                            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    context.startActivity(new Intent(context, LauncherActivity.class));
                                }
                            });
                            alertDialog.show();
                        }
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", "Error : " + error.toString() + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (type.equals("userLogin")) {
                    pass = data.get("pass");
                    data.remove("pass");
                    data.put("pass", getMD5(pass));
                }
                return data;
            }
        };
        getRequestQueue().add(stringRequest);
    }

    /**
     * Converts the given string into md5 hash converted string
     *
     * @param input string on which md5 hash is to be applied
     * @return  result of the given string after md5 hash is applied
     */
    public static final String getMD5(final String input) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(input.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public interface GeofenceData {

        /*
        *
        *
        *
        * @params result
        *        string of data response from http request
        */
        void dataForGeofences(String result);
    }

    public interface GroupsData {
        void dataForGroups(String result);
    }

    public interface GettingUsersLocationData {
        void dataForUsersLocations(String result);
    }

}