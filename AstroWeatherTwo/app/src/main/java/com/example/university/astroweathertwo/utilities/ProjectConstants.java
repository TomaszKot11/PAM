package com.example.university.astroweathertwo.utilities;

public final class ProjectConstants {
    public static long ONE_SECOND_IN_MILISECONDS = 1000;
    // shared preferences constants
    public static final String SHARED_PREFERENCES_NAME = "APPLICATION_SETTINGS";
    public static final String PREFENCES_TIME_VALUE_KEY = "TIME_VALUE";
    public static final String PREFERNCE_TIME_UNIT_KEY = "TIME_UNIT";
    public static final String PREFERENCES_LONGITUTDE_KEY = "LONGITUDE_KEY";
    public static final String PREFERENCE_LATITTUDE_KEY = "LATITUDE_KEY";
    public static final String PREFERENCES_LOCALIZATION_STRING_KEY = "LOCALIZATION_STRING";
    public static final String PREFERENCES_TIME_TO_UPDATE_JSON_KEY = "TIME_TO_UPDATE_JSON";
    public static String DMCS_LONGITUDE = "19.4528";
    public static String DMCS_LATITUDE = "51.7460238";

    // sun info bundle keys
    public static final String BUNDLE_SUN_RISE_TIME = "SUN_RISE_TIME";
    public static final String BUNDLE_SUN_RISE_AZIMUTH = "SUN_RISE_AZIMUTH";
    public static final String BUNDLE_SUN_SET_TIME = "SUN_SET_TIME";
    public static final String BUNDLE_SUN_SET_AZIMUTH = "SUN_SET_AZIMUTH";
    public static final String BUNDLE_SUN_CIVIL_EVENING_TWILIGHT = "SUN_CIVIL_EVENING_TWILIGHT";
    public static final String BUNDLE_SUN_CIVIL_MORNING_TWILIGHT ="SUN_CIVIL_MORNING_TWILIGHT";

    // Moon info bundle keys
    public static final String BUNDLE_MOON_RISE_TIME = "MOON_RISE_TIME";
    public static final String BUNDLE_MOON_SET_TIME = "MOON_SET_TIME";
    public static final String BUNDLE_MOON_NEW_MOON = "MOON_NEW_MOON";
    public static final String BUNDLE_MOON_FULL = "MOON_FULL_MOON";
    //TODO: lines benath are propably wrong
    public static final String BUNDLE_MOON_PHASE = "MOON_PHASE";
    public static final String BUNDLE_MOON_SYNODIC = "MOON_SYNODIC";

    // to validate EditText Input
    public static final double LATITUDE_MIN = 0.0;
    public static final double LATITUDE_MAX = 90.0;
    public static final double LONGITUDE_MIN = 0.0;
    public static final double LONGITUDE_MAX = 180.0;

    // api request sending frequency
    public static final int MINUTES_TILL_NEXT_API_REQUEST_IN_MINUTES = 30;

    // name of the file to which the weather json is serialized
    public static final String SERIALIZED_WEATHER_JSON_FILE_NAME = "weather_json.json";
}