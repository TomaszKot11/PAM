package com.example.university.astroweathertwo.mainActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.university.astroweathertwo.utilities.ProjectConstants;
import com.example.university.astroweathertwo.utilities.ScreenUtilities;
import com.example.university.astroweathertwo.utilities.Settings;
import com.example.university.astroweathertwo.utilities.SharedPreferencesUtility;
import com.example.university.astroweathertwo.utilities.api.ApiRequest;
import com.example.university.astroweathertwo.utilities.api.ApiRequester;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


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
    private long periodicWheatherUpdateTime;
    private enum ScreenSizeOrientation { PHONE_PORTRAIT, PHONE_LANDSAPE, TABLET_PORTRAIT, TABLET_LANDSAPE }
    private ScreenSizeOrientation screenOrientation = ScreenSizeOrientation.PHONE_PORTRAIT;
    private List<ApiRequestObtainable> apiSubscribers = new ArrayList<>();



    @Override
    public void onStart() {
        super.onStart();
        //TODO: read data from the file system


        String location = "lodz,pl";

        ApiRequester requestManager = ApiRequester.getInstance(this);

        ApiRequest request = new ApiRequest(Request.Method.GET, null, null, location,  new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                for(ApiRequestObtainable ob : MainActivity.this.apiSubscribers) {
                    ob.refreshUI((JSONObject)response);
                    Log.e("MAIN ACTIVITY", "INSIDE SUBSCRIBERS LIST");
                }

                Log.e("Response", ((JSONObject)response).toString());
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


        if(screenUtilities.getWidth() > 600) {
            if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // initialize landscape
                this.screenOrientation = ScreenSizeOrientation.TABLET_LANDSAPE;
                initializeTabletLayout(bundle);
            } else {
                this.screenOrientation = ScreenSizeOrientation.TABLET_PORTRAIT;
                // initialize landscape
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

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_sun, sunFragment)
                .commit();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_moon, moonFragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        Settings settings =  SharedPreferencesUtility.readSharedPreferences(getSharedPreferences(ProjectConstants.SHARED_PREFERENCES_NAME, 0));

        Bundle bundle = new Bundle();
        bundle.putString(ProjectConstants.PREFERENCES_LONGITUTDE_KEY, String.valueOf(settings.getLongitude()));
        bundle.putString(ProjectConstants.PREFERENCE_LATITTUDE_KEY, String.valueOf(settings.getLatitude()));

        for(MainActivity.SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(bundle, false, true);
        }

        startPeriodicTimeUpdate();
        startPeriodicWeatherUpates();
    }

    // TODO: change this to use shared preferences value
    private void startPeriodicWeatherUpates() {
        this.handlerTwo = new Handler();
        this.runnableTwo =  new Runnable() {
            @Override
            public void run() {

                double latitude = SharedPreferencesUtility.getLatitude(MainActivity.this);
                double longitutde = SharedPreferencesUtility.getLongitude(MainActivity.this);

                new WeatherBackgroundTask().execute(latitude, longitutde);

                periodicWheatherUpdateTime = SharedPreferencesUtility.getWeatherUpateTimeInMiliseconds(MainActivity.this);


                handler.postDelayed(this, periodicWheatherUpdateTime);
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

    @Override
    public void onFragmentInteraction(Settings settings) {
        settingsUpdated(settings);
    }

    private void settingsUpdated(Settings settings) {
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
