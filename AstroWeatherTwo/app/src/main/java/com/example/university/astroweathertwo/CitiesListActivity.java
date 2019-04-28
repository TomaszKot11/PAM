package com.example.university.astroweathertwo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.university.astroweathertwo.mainActivity.fragments.allCities.dummy.CitiesListFragment;

public class CitiesListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        attachListFragment();
    }


    private void attachListFragment() {
        CitiesListFragment citiesListFragment = new CitiesListFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_list_cities, citiesListFragment)
                .commit();
    }
}
