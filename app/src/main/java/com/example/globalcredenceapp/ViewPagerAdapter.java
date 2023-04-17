package com.example.globalcredenceapp;


import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.globalcredenceapp.MyProfile_About;
import com.example.globalcredenceapp.MyProfile_Details;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new MyProfile_About();
        }
        else if (position == 1)
        {
            fragment = new MyProfile_Details();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position == 0)
        {
            title = "About";
        }
        else if (position == 1)
        {
            title = "Details";
        }

        return title;
    }
}