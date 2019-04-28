package com.example.university.astroweathertwo.utilities.api;

import com.android.volley.Request;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ApiRequester {
    private static ApiRequester sInstance;

    Context mContext;
    RequestQueue mRequestQueue;

    public static synchronized ApiRequester getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ApiRequester(context);
        }
        return sInstance;
    }

    private ApiRequester(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        mRequestQueue.add(request);
    }
}
