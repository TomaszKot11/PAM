package com.example.university.astroweathertwo.utilities;


import android.app.Activity;
import android.content.SharedPreferences;

public class SharedPreferencesUtility {

    public static Settings readSharedPreferences(SharedPreferences sharedPreferences) {

        double latitude = Double.parseDouble(sharedPreferences.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE));
        double longitutde = Double.parseDouble(sharedPreferences.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE));

        int timeValue = Integer.parseInt(sharedPreferences.getString(ProjectConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        String timeUnit = sharedPreferences.getString(ProjectConstants.PREFERNCE_TIME_UNIT_KEY, "seconds");

        return new Settings(latitude, longitutde, timeValue, timeUnit);
    }


    public static long getWeatherUpateTimeInMiliseconds(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);

        int value = Integer.parseInt(sharedPreferences.getString(ProjectConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        String unit = sharedPreferences.getString(ProjectConstants.PREFERNCE_TIME_UNIT_KEY, "seconds");

        if(unit.equals("seconds")) {
            return value * 1000;
        } else if(unit.equals("minutes")) {
            return value * 60 * 1000;
        } else {
            return 15 * 1000;
        }
    }


    public static double getLongitude(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);

        return Double.parseDouble(sharedPreferences.getString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, ProjectConstants.DMCS_LONGITUDE));

    }

    public static double getLatitude(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);

        return Double.parseDouble(sharedPreferences.getString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, ProjectConstants.DMCS_LATITUDE));
    }


    public static void writeSharedPreferences(Activity activity, Settings settings) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ProjectConstants.PREFENCES_TIME_VALUE_KEY, String.valueOf(settings.getTimeValue()));
        editor.putString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, String.valueOf(settings.getLatitude()));
        editor.putString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, String.valueOf(settings.getLongitude()));
        editor.putString(ProjectConstants.PREFERNCE_TIME_UNIT_KEY, settings.getTimeUnit());

        editor.commit();

    }
}