package com.example.brian.cleverrent;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by brian on 1/31/16.
 */
public class CommunityFragmentPagerAdapter extends FragmentPagerAdapter {
    int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Events", "People", "Listings"};
    private Context context;

    public CommunityFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AccountPageFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
