package com.example.university.astroweathertwo.mainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.university.astroweathertwo.mainActivity.fragments.MoonFragment;
import com.example.university.astroweathertwo.mainActivity.fragments.SunFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Bundle bundle;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setArguments(Bundle bundle) {
        this.bundle = bundle;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                SunFragment sunFragment = new SunFragment();
                sunFragment.setArguments(bundle);

                return sunFragment;
            case 1:
                MoonFragment moonFragment = new MoonFragment();
                moonFragment.setArguments(bundle);

                return moonFragment;
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
