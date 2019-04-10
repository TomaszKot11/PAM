package com.example.tomek.astroweatherone.utilities;

import android.content.SharedPreferences;

public class SharedPreferencesUtility {

    public static Settings readSharedPreferences(SharedPreferences sharedPreferences) {

        double latitude = Double.parseDouble(sharedPreferences.getString(StringConstants.PREFERENCE_LATITTUDE_KEY, StringConstants.DMCS_LATITUDE));
        double longitutde = Double.parseDouble(sharedPreferences.getString(StringConstants.PREFERENCES_LONGITUTDE_KEY, StringConstants.DMCS_LONGITUDE));

        int timeValue = Integer.parseInt(sharedPreferences.getString(StringConstants.PREFENCES_TIME_VALUE_KEY, "15"));
        String timeUnit = sharedPreferences.getString(StringConstants.PREFERNCE_TIME_UNIT_KEY, "seconds");

        return new Settings(latitude, longitutde, timeValue, timeUnit);
    }


    public static long getWeatherUpateTimeInMiliseconds(int value, String unit) {
        if(unit.equals("seconds")) {
            return value * 1000;
        } else if(unit.equals("minutes")) {
            return value * 60 * 1000;
        } else {
            return 15 * 1000;
        }
    }
}
