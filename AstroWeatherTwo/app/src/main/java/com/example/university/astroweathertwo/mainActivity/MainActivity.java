package com.example.university.astroweathertwo.mainActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.university.astroweathertwo.R;
import com.example.university.astroweathertwo.SettingsActivity;
import com.example.university.astroweathertwo.mainActivity.fragments.MoonFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.SettingsFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.SunFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.allCities.dummy.CitiesListFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments.AdditionalWeatherInformationFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments.BasicWeatherInformationFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.apiWeatherFragments.ForthcomingWeatherCondtionsFragment;
import com.example.university.astroweathertwo.utilities.*;
import com.example.university.astroweathertwo.utilities.api.ApiRequest;
import com.example.university.astroweathertwo.utilities.api.ApiRequester;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


//TODO: weathe only updates after second click?!
//TODO: when tablet is rotated the list disappears - save the state ;)
//TODO: add button to go back
public class MainActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private List<SunMoonRefreshableUI> subscribersList = new ArrayList<>();
    private Handler handler;
    private Runnable runnable;
    private Handler handlerTwo;
    private Runnable runnableTwo;
    private long periodicWeatherUpdateTime;
    private enum ScreenSizeOrientation { PHONE_PORTRAIT, PHONE_LANDSAPE, TABLET_PORTRAIT, TABLET_LANDSAPE }
    private ScreenSizeOrientation screenOrientation = ScreenSizeOrientation.PHONE_PORTRAIT;
    private List<ApiRequestObtainable> apiSubscribers = new ArrayList<>();
    private JSONObject jsonObject;
    private String location = "lodz,pl";
    private Date jsonApiTimeToUpdate;
    private static boolean APPLICATION_RUNNING = false;

    @Override
    public void onStart() {
        super.onStart();
        //TODO: read data from the file system

        this.location = SharedPreferencesUtility.getLocalizationString(this);

        if(APPLICATION_RUNNING) {
            sendWeatherApiRequest();
        } else {
            this.jsonApiTimeToUpdate = SharedPreferencesUtility.readTimeToUpdateJsonOnStart(this);

            sendWeatherApiRequestOnApplicationStart();
        }

        APPLICATION_RUNNING = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // this is propably unecessary
        APPLICATION_RUNNING = false;
    }


    public JSONObject getJsonObject() {
        return jsonObject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Settings settings =  SharedPreferencesUtility.readSharedPreferences(getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0));

        Bundle bundle = new Bundle();
        bundle.putString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, String.valueOf(settings.getLongitude()));
        bundle.putString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, String.valueOf(settings.getLatitude()));


        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        ScreenUtilities screenUtilities = new ScreenUtilities(this);
        int orientation = getResources().getConfiguration().orientation;

        Log.e("screen width", String.valueOf(screenUtilities.getWidth()));

        if(screenUtilities.getWidth() > 600) {
            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // initialize landscape
                this.screenOrientation = ScreenSizeOrientation.TABLET_LANDSAPE;
                initializeTabletLayout(bundle);
            } else {
                this.screenOrientation = ScreenSizeOrientation.TABLET_PORTRAIT;
                // initialize portrait
                initializeTabletLayout(bundle);
            }
        } else {
            // smaller device (phone)
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                this.screenOrientation = ScreenSizeOrientation.PHONE_LANDSAPE;
                // In landscape
                initializeLandsacpeLayout(bundle);
            } else {
                this.screenOrientation = ScreenSizeOrientation.PHONE_PORTRAIT;
                // In portrait
                initializePortraitLayout(bundle);
            }
        }


    }


    private void initializeTabletLayout(Bundle bundle) {
        initializeLandsacpeLayout(bundle);

        SettingsFragment settingsFragment = new SettingsFragment();


        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_settings, settingsFragment)
                .commit();

        settingsFragment.subscribeListener(this);
    }

    private void initializePortraitLayout(Bundle bundle) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mSectionsPagerAdapter.setArguments(bundle);


        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initializeLandsacpeLayout(Bundle bundle) {
        SunFragment sunFragment = new SunFragment();
        sunFragment.setArguments(bundle);

        MoonFragment moonFragment = new MoonFragment();
        moonFragment.setArguments(bundle);

        BasicWeatherInformationFragment basicWeatherInformationFragment = new BasicWeatherInformationFragment();
        AdditionalWeatherInformationFragment additionalWeatherInformationFragment = new AdditionalWeatherInformationFragment();
        ForthcomingWeatherCondtionsFragment forthcomingWeatherCondtionsFragment = new ForthcomingWeatherCondtionsFragment();

        getSupportFragmentManager()
                 .beginTransaction()
                 .add(R.id.fragment_sun, sunFragment)
                 .add(R.id.fragment_moon, moonFragment)
                 .add(R.id.fragment_basic_weather_information, basicWeatherInformationFragment)
                 .add(R.id.fragment_additional_weather_information, additionalWeatherInformationFragment)
                 .add(R.id.fragment_forthcomming_weather_information, forthcomingWeatherCondtionsFragment)
                 .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Settings settings =  SharedPreferencesUtility.readSharedPreferences(getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0));

        Bundle bundle = new Bundle();
        bundle.putString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, String.valueOf(settings.getLongitude()));
        bundle.putString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, String.valueOf(settings.getLatitude()));

        this.location = settings.getWeatherLocalizationString();

        for(MainActivity.SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(bundle, false, true);
        }

        startPeriodicTimeUpdate();
        startPeriodicWeatherUpdates();
    }

    // TODO: change this to use shared preferences value
    private void startPeriodicWeatherUpdates() {
        this.handlerTwo = new Handler();
        this.runnableTwo =  new Runnable() {
            @Override
            public void run() {

                double latitude = SharedPreferencesUtility.getLatitude(MainActivity.this);
                double longitutde = SharedPreferencesUtility.getLongitude(MainActivity.this);

                new WeatherBackgroundTask().execute(latitude, longitutde);

                periodicWeatherUpdateTime = SharedPreferencesUtility.getWeatherUpateTimeInMiliseconds(MainActivity.this);


                handler.postDelayed(this, periodicWeatherUpdateTime);
            }
        };

        handlerTwo.post(this.runnableTwo);
    }

    private void startPeriodicTimeUpdate() {
        this.handler = new Handler();
        this.runnable =  new Runnable() {
            @Override
            public void run() {
                Date currentTime = Calendar.getInstance().getTime();

                // Toast.makeText(MainActivity.this, currentTime.toString(), Toast.LENGTH_SHORT).show();
                for(SunMoonRefreshableUI element : subscribersList) {
                    Bundle bundle = new Bundle();
                    bundle.putString("DATE", String.valueOf(currentTime));
                    element.refreshUI(bundle, true, false);
                }
                handler.postDelayed(this, ProjectConstants.ONE_SECOND_IN_MILISECONDS);
            }
        };

        handler.post(this.runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(this.runnable);
        handlerTwo.removeCallbacks(this.runnableTwo);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(screenOrientation == ScreenSizeOrientation.PHONE_PORTRAIT || screenOrientation == ScreenSizeOrientation.PHONE_LANDSAPE) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
            return true;
        }

        return false;
    }

    // hamburger options selecting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(screenOrientation == ScreenSizeOrientation.PHONE_PORTRAIT || screenOrientation == ScreenSizeOrientation.PHONE_LANDSAPE) {
            int id = item.getItemId();

            // run Settings activity
            if (id == R.id.action_settings) {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendWeatherApiRequest() {
        if(!NetworkUtilities.isConnectedToTheWeb(getApplicationContext())) {
            try {
                this.jsonObject = FIleSystemUtilities.deserializeJsonFromTheFile(this, ProjectConstants.SERIALIZED_WEATHER_JSON_FILE_NAME);
            } catch(IOException | ClassNotFoundException | JSONException e) {
                Toast.makeText(MainActivity.this, "Error while retrieving json in offline mode, please connect to the Internet", Toast.LENGTH_LONG).show();

                Log.e("MainActivity", e.toString());
            }


        } else {
            ApiRequester requestManager = ApiRequester.getInstance(this);

            ApiRequest request = new ApiRequest(Request.Method.GET, null, null, this.location, new Response.Listener() {
                @Override
                public void onResponse(Object response) {

                    for (ApiRequestObtainable ob : MainActivity.this.apiSubscribers)
                        ob.refreshUI((JSONObject) response);

                    MainActivity.this.jsonObject = (JSONObject) response;
                    if (!APPLICATION_RUNNING) {
                        MainActivity.this.jsonApiTimeToUpdate = DateUtils.addMinutes(new Date(System.currentTimeMillis()), ProjectConstants.MINUTES_TILL_NEXT_API_REQUEST_IN_MINUTES);
                        SharedPreferencesUtility.writeTimeToUpdateJsonOnStart(MainActivity.this, MainActivity.this.jsonApiTimeToUpdate.toString());
                    }

                    try {
                        // serialize the obtained object to the file
                        FIleSystemUtilities.serializeJsonToTheFile(MainActivity.this, ProjectConstants.SERIALIZED_WEATHER_JSON_FILE_NAME, (JSONObject) response);
                    } catch(IOException e) {
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();

                        Log.e("MainActivity", "Error while serializing JSONObject");
                    }


                    Log.e("Response", ((JSONObject) response).toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Add error handling here
                    Log.e("API error: ", "#onErrorResponse in MainActivity");
                }
            });


            requestManager.addToRequestQueue(request);
        }
    }

    private void sendWeatherApiRequestOnApplicationStart() {
        if(jsonApiTimeToUpdate != null && (new Date(System.currentTimeMillis()).before(jsonApiTimeToUpdate)))
            return ;

        sendWeatherApiRequest();
    }

    public int getCurrentViewPagerPosition() {
        return this.mViewPager.getCurrentItem();
    }

    @Override
    public void onFragmentInteraction(Settings settings) {

        Log.e("Obtained!", settings.getWeatherLocalizationString());

        settingsUpdated(settings);
    }

    private void settingsUpdated(Settings settings) {

//        if(!this.location.equals(settings.getWeatherLocalizationString())) {
            this.location = settings.getWeatherLocalizationString();
            sendWeatherApiRequest();
//        }

        if(screenOrientation == ScreenSizeOrientation.TABLET_PORTRAIT || screenOrientation == ScreenSizeOrientation.TABLET_LANDSAPE) {
            Bundle bundle = new Bundle();

            bundle.putString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, String.valueOf(settings.getLatitude()));
            bundle.putString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, String.valueOf(settings.getLongitude()));

            SharedPreferencesUtility.writeSharedPreferences(this, settings);

            for (SunMoonRefreshableUI subscriber : subscribersList) {
                subscriber.settingsRefreshUI(bundle);
            }
        }
    }

    @Override
    public void citiesListClicked() {
        switch(this.screenOrientation) {
            case PHONE_LANDSAPE:
            case PHONE_PORTRAIT:
                break;
            case TABLET_LANDSAPE:
            case TABLET_PORTRAIT:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                CitiesListFragment citiesListFragment = new CitiesListFragment();
                ft.replace(R.id.fragment_settings, citiesListFragment);

                ft.commit();
                break;
        }
    }


    public void updateUI(Bundle bundle, boolean isTimeUpdate) {
        for(SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(bundle, isTimeUpdate, false);
        }
    }


    private class WeatherBackgroundTask extends AsyncTask<Double, Object, Bundle> {

        @Override
        protected void onPostExecute(Bundle o) {
            super.onPostExecute(o);

            //Toast.makeText(MainActivity.this, "Weather updated!", Toast.LENGTH_SHORT).show();

            MainActivity.this.updateUI(o, false);
        }

        @Override
        protected Bundle doInBackground(Double... coordinates) {

            double latitude = coordinates[0];
            double longitude = coordinates[1];

            Calendar instance = Calendar.getInstance();
            double timeOffsetGreenwich = (instance.get(Calendar.ZONE_OFFSET)) / (1000 * 60 * 60);

            AstroDateTime astroDateTime = new AstroDateTime(instance.get(Calendar.YEAR),
                    instance.get(Calendar.MONTH) + 1,
                    instance.get(Calendar.DAY_OF_MONTH),
                    instance.get(Calendar.HOUR),
                    instance.get(Calendar.MINUTE),
                    instance.get(Calendar.SECOND),
                    (int)timeOffsetGreenwich,
                    true);


            AstroCalculator.Location location = new AstroCalculator.Location(longitude, latitude);

            AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

            AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();
            AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();

            Bundle bundle = new Bundle();

            // sun rise info
            bundle.putString(ProjectConstants.BUNDLE_SUN_RISE_TIME, sunInfo.getSunrise().toString());
            bundle.putString(ProjectConstants.BUNDLE_SUN_RISE_AZIMUTH, String.valueOf(sunInfo.getAzimuthRise()));
            bundle.putString(ProjectConstants.BUNDLE_SUN_SET_TIME, sunInfo.getSunset().toString());
            bundle.putString(ProjectConstants.BUNDLE_SUN_SET_AZIMUTH, String.valueOf(sunInfo.getAzimuthSet()));
            bundle.putString(ProjectConstants.BUNDLE_SUN_CIVIL_EVENING_TWILIGHT, sunInfo.getTwilightEvening().toString());
            bundle.putString(ProjectConstants.BUNDLE_SUN_CIVIL_MORNING_TWILIGHT, sunInfo.getTwilightMorning().toString());

            // moon info
            bundle.putString(ProjectConstants.BUNDLE_MOON_RISE_TIME, moonInfo.getMoonset().toString());
            bundle.putString(ProjectConstants.BUNDLE_MOON_SET_TIME, moonInfo.getMoonrise().toString());
            bundle.putString(ProjectConstants.BUNDLE_MOON_NEW_MOON, moonInfo.getNextNewMoon().toString());
            bundle.putString(ProjectConstants.BUNDLE_MOON_FULL, moonInfo.getNextFullMoon().toString());
            //TODO: lines benath are propably wrong
            bundle.putString(ProjectConstants.BUNDLE_MOON_PHASE, String.valueOf(moonInfo.getIllumination()));
            bundle.putString(ProjectConstants.BUNDLE_MOON_SYNODIC, String.valueOf(moonInfo.getAge()));

            return bundle;
        }

    }

    public void addSubscriberSunMoonFragment(SunMoonRefreshableUI subsriber) {
        this.subscribersList.add(subsriber);
    }

    public void addSubscribeApiListener(ApiRequestObtainable apiSubscriber) {
        this.apiSubscribers.add(apiSubscriber);
    }


    public interface ApiRequestObtainable {
        void refreshUI(JSONObject jsonObject);
    }

    public interface SunMoonRefreshableUI {
        void refreshUI(Bundle bundle, boolean isTimeUpdate, boolean isLongitudeLatitudeUpdate);
        void settingsRefreshUI(Bundle bundle);
    }

}
