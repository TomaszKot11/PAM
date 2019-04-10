package com.example.tomek.astroweatherone.mainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.Toast;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.SettingsActivity;
import com.example.tomek.astroweatherone.utilities.Settings;
import com.example.tomek.astroweatherone.utilities.SharedPreferencesUtility;
import com.example.tomek.astroweatherone.utilities.StringConstants;

import java.util.*;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private List<SunMoonRefreshableUI> subscribersList = new ArrayList<>();
    private ViewPager mViewPager;
    private Handler handler;
    private Runnable runnable;
    private Handler handlerTwo;
    private Runnable runnableTwo;
    private long periodicWheatherUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    //TODO: think it over! - in which method of the lifecycle to perform this action
    @Override
    public void onResume() {
        super.onResume();
        Settings settings =  SharedPreferencesUtility.readSharedPreferences(getSharedPreferences(StringConstants.SHARED_PREFERENCES_NAME, 0));

        periodicWheatherUpdateTime = SharedPreferencesUtility.getWeatherUpateTimeInMiliseconds(settings.getTimeValue(), settings.getTimeUnit());

        Log.e(String.valueOf(periodicWheatherUpdateTime),String.valueOf(periodicWheatherUpdateTime));

        startPeriodicTimeUpdate();
        startPeriodicWeatherUpates();
    }

    // TODO: change this to use shared preferences value
    private void startPeriodicWeatherUpates() {
        this.handlerTwo = new Handler();
        this.runnableTwo =  new Runnable() {
            @Override
            public void run() {

                new WeatherBackgroundTask().execute();

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
                    element.refreshUI(bundle, true);
                }
                handler.postDelayed(this, StringConstants.ONE_SECOND_IN_MILISECONDS);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    // hamburger options selecting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // run Settings activity
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void updateUI(Bundle bundle, boolean isTimeUpdate) {
        for(SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(bundle, isTimeUpdate);
        }
    }


    private class WeatherBackgroundTask extends AsyncTask<Object, Object, Bundle> {

        @Override
        protected void onPostExecute(Bundle o) {
            super.onPostExecute(o);

            Toast.makeText(MainActivity.this, "Weather updated!", Toast.LENGTH_SHORT).show();

            MainActivity.this.updateUI(o, false);
        }

        @Override
        protected Bundle doInBackground(Object... activites) {

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

            AstroCalculator.Location location = new AstroCalculator.Location(Double.parseDouble(StringConstants.DMCS_LONGITUDE),
                                                                            Double.parseDouble(StringConstants.DMCS_LATITUDE));

            AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

            AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();
            AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();

            Bundle bundle = new Bundle();
            //TODO: do it in a more clever way - put whole object


            // sun rise info
            bundle.putString(StringConstants.BUNDLE_SUN_RISE_TIME, sunInfo.getSunrise().toString());
            bundle.putString(StringConstants.BUNDLE_SUN_RISE_AZIMUTH, String.valueOf(sunInfo.getAzimuthRise()));
            bundle.putString(StringConstants.BUNDLE_SUN_SET_TIME, sunInfo.getSunset().toString());
            bundle.putString(StringConstants.BUNDLE_SUN_SET_AZIMUTH, String.valueOf(sunInfo.getAzimuthSet()));
            bundle.putString(StringConstants.BUNDLE_SUN_CIVIL_EVENING_TWILIGHT, sunInfo.getTwilightEvening().toString());
            bundle.putString(StringConstants.BUNDLE_SUN_CIVIL_MORNING_TWILIGHT, sunInfo.getTwilightMorning().toString());

            // moon info
            bundle.putString(StringConstants.BUNDLE_MOON_RISE_TIME, moonInfo.getMoonset().toString());
            bundle.putString(StringConstants.BUNDLE_MOON_SET_TIME, moonInfo.getMoonrise().toString());
            bundle.putString(StringConstants.BUNDLE_MOON_NEW_MOON, moonInfo.getNextNewMoon().toString());
            bundle.putString(StringConstants.BUNDLE_MOON_FULL, moonInfo.getNextFullMoon().toString());
            //TODO: lines benath are propably wrong
            bundle.putString(StringConstants.BUNDLE_MOON_PHASE, String.valueOf(moonInfo.getIllumination()));
            bundle.putString(StringConstants.BUNDLE_MOON_SYNODIC, String.valueOf(moonInfo.getAge()));

            return bundle;
        }

    }

    public void addSubscriberFragment(SunMoonRefreshableUI subsriber) {
        this.subscribersList.add(subsriber);
    }


    public interface SunMoonRefreshableUI {
        void refreshUI(Bundle bundle, boolean isTimeUpdate);
    }

}
