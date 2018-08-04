package com.kumar.akshay.familylocator.Firebase;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MySingleton {

    private static MySingleton mInstance;
    private static Context context;
    private RequestQueue requestQueue;

    private MySingleton (Context con){
        context = con;
        requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySingleton getmInstance(Context context){
        if (mInstance == null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public <T> void addToRequestQueue(final String type, String url, final Map<String, String> data){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://akshayakshaykumar929.000webhostapp.com/FamLoc/"+url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (type.equals("userLogin")){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MainActivity", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return data;
            }
        };
        getRequestQueue().add(stringRequest);
    }

}
