package com.example.tomek.astroweatherone.mainActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.renderscript.Sampler;
import android.support.design.theme.MaterialComponentsViewInflater;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.example.tomek.astroweatherone.R;
import com.example.tomek.astroweatherone.SettingsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
                    element.refreshUI(bundle);
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


    public void updateUI() {
        // TODO: communiacte with fragments here
        //Toast.makeText(this, "Weather updated!", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, String.valueOf(subscribersList.isEmpty()), Toast.LENGTH_SHORT).show();
        for(SunMoonRefreshableUI subscriber : subscribersList) {
            subscriber.refreshUI(new Bundle());
        }
    }


    private class WeatherBackgroundTask extends AsyncTask<Object, Object, Bundle> {

        @Override
        protected void onPostExecute(Bundle o) {
            super.onPostExecute(o);

            // TODO: pass here bundle
            MainActivity.this.updateUI();
        }

        @Override
        protected Bundle doInBackground(Object... activites) {

            //TODO: send here requests
            return new Bundle();
        }

    }

    public void addSubscriberFragment(SunMoonRefreshableUI subsriber) {
        this.subscribersList.add(subsriber);
    }


    public interface SunMoonRefreshableUI {
        void refreshUI(Bundle bundle);
    }

}
