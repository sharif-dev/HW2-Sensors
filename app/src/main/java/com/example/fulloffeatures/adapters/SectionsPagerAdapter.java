package com.example.fulloffeatures.adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.fulloffeatures.R;
import com.example.fulloffeatures.fragments.AlarmFragment;
import com.example.fulloffeatures.fragments.ScreenLockerFragment;
import com.example.fulloffeatures.fragments.ShakeItFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context mContext;
    private String[] tabTitles;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        tabTitles = context.getResources().getStringArray(R.array.tab_title);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AlarmFragment();
            case 1:
                return new ShakeItFragment();
            default:
                return new ScreenLockerFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}