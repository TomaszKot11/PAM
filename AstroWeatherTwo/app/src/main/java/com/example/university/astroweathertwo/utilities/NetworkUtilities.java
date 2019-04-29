package com.example.university.astroweathertwo.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkUtilities {

    public static boolean isConnectedToTheWeb(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         if(!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
            Toast.makeText(context, "Network connection not available", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
