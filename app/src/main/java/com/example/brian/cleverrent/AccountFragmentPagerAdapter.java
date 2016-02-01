package com.example.brian.cleverrent;


import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by brian on 1/31/16.
 */
public class AccountFragmentPagerAdapter extends FragmentPagerAdapter {
    int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "User", "Billing"};
    private Context context;

    public AccountFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return AccountPageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
