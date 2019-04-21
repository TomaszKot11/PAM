package com.example.tomek.astroweatherone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.tomek.astroweatherone.mainActivity.fragments.SettingsFragment;
import com.example.tomek.astroweatherone.utilities.Settings;
import com.example.tomek.astroweatherone.utilities.SharedPreferencesUtility;

public class SettingsActivity extends AppCompatActivity implements SettingsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        initializeSettingsFragment();
    }



    private void initializeSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.subscribeListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_settings, settingsFragment)
                .commit();
    }


    @Override
    public void onFragmentInteraction(Settings settings) {
        SharedPreferencesUtility.writeSharedPreferences(this, settings);
    }
}
