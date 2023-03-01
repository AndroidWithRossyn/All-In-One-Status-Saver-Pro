package com.kessi.allstatussaver.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.kessi.allstatussaver.fragment.GuideFragment;
import com.kessi.allstatussaver.fragment.WAppStatusFrag;

public class MainPagerAdapter extends FragmentPagerAdapter {


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new WAppStatusFrag();
        }  else {
            return new GuideFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

}
