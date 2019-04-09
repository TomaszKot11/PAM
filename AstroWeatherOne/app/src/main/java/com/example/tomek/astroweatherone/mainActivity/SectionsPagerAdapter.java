package com.example.tomek.astroweatherone.mainActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import com.example.tomek.astroweatherone.mainActivity.fragments.MoonFragment;
import com.example.tomek.astroweatherone.mainActivity.fragments.SunFragment;

// FragmentPagerAdapter keeps all fragments in the memory all the time (Static does the opposite thing)
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new SunFragment();
            case 1:
                return new MoonFragment();
            default:
                return null;
        }
    }

    //TODO: make some tabs this method is useful
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Sun astro";
            case 1:
                return "Moon astro";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}