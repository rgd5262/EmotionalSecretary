package com.example.nago.es_2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    // Count number of tabs
    private int tabCount;

    public ViewPagerAdapter(FragmentManager fm, int count) {
        super(fm); tabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;

        // Returning the current tabs
        switch (position) {
            case 0:
                frag = new TabFragment1();
                break;
            case 1:
                frag = new TabFragment2();
                break;
            case 2:
                frag = new TabFragment3();
                break;
        }

        return frag;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}