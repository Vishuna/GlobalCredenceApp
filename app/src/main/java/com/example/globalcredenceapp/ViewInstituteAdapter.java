package com.example.globalcredenceapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewInstituteAdapter extends FragmentPagerAdapter {

    public ViewInstituteAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new Institute_About();
        } else if (position == 1) {
            fragment = new Institute_Details();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "About";
        } else if (position == 1) {
            title = "Details";
        }

        return title;
    }
}