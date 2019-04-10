package com.example.tomek.astroweatherone.mainActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.SettingsActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static long ONE_SECOND_IN_MILISECONDS = 1000;
    private List<SunMoonRefreshableUI> subscribersList = new ArrayList<>();

    private ViewPager mViewPager;
//    private WeatherHandler handler;

    private Handler handler;
    private Runnable runnable;
    private Handler handlerTwo;
    private Runnable runnableTwo;
//    private WeatherBackgroundTask weatherBackgroundTask = new WeatherBackgroundTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    //TODO: think it over! - in which method of the lifecycle to perform this action
    @Override
    public void onResume() {
        super.onResume();
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

                handler.postDelayed(this, 3000);
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
                handler.postDelayed(this, ONE_SECOND_IN_MILISECONDS);
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
        // TODO: communiacte with fragments here
        //Toast.makeText(this, "Weather updated!", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, String.valueOf(subscribersList.isEmpty()), Toast.LENGTH_SHORT).show();
        for(SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(bundle, isTimeUpdate);
        }
    }


    private class WeatherBackgroundTask extends AsyncTask<Object, Object, Bundle> {

        @Override
        protected void onPostExecute(Bundle o) {
            super.onPostExecute(o);

            // TODO: pass here bundle
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

            AstroCalculator.Location location = new AstroCalculator.Location(Double.parseDouble(SettingsActivity.DMCS_LATITUDE),
                                                                              Double.parseDouble(SettingsActivity.DMCS_LONGITUDE));

            AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

            AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();
            AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();

            Bundle bundle = new Bundle();
            //TODO: do it in a more clever way - put whole object


            // sun rise info
            bundle.putString("SUN_RISE_TIME", sunInfo.getSunrise().toString());
            bundle.putString("SUN_RISE_AZIMUTH", String.valueOf(sunInfo.getAzimuthRise()));
            bundle.putString("SUN_SET_TIME", sunInfo.getSunset().toString());
            bundle.putString("SUN_SET_AZIMUTH", String.valueOf(sunInfo.getAzimuthSet()));
            bundle.putString("SUN_CIVIL_EVENING_TWILIGHT", sunInfo.getTwilightEvening().toString());
            bundle.putString("SUN_CIVIL_MORNING_TWILIGHT", sunInfo.getTwilightMorning().toString());

            // moon info
//            bundle.putString("MOON_");

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
