package com.example.globalcredenceapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.globalcredenceapp.Login;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private Context mCtx;

    private VolleySingleton(Context context){
        mCtx=context;
        mRequestQueue= getRequestQueue();
    }

    public static  synchronized VolleySingleton getInstance(Context context){
        if(mInstance==null)
        {
            mInstance=new VolleySingleton(context);

        }
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue==null){
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public void addToRequestQueue(StringRequest req){
            getRequestQueue().add(req);
    }


}
