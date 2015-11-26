package com.artzmb.hhkl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Spinner;

import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class TableActivity extends BaseActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Spinner mSpinnerLeague;

    private PagerAdapter mPagerAdapter;
    private Schedule mSchedule;

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, TableActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSchedule = MockDataGenerator.generateSchedule();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pages);
        mSpinnerLeague = (Spinner) findViewById(R.id.spinner_league);

        setupSpinner();
        setupTabs();
    }

    private void setupTabs() {

        mPagerAdapter = new StandingsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        String[] standings = getResources().getStringArray(R.array.standings);
        for (int i = 0; i < standings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @SuppressWarnings("ConstantConditions")
    private void setupSpinner() {
        StringSpinnerAdapter spinnerAdapter = new StringSpinnerAdapter(this);
        spinnerAdapter.addItems(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues))));
        mSpinnerLeague.setAdapter(spinnerAdapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_table;
    }

    private class StandingsPagerAdapter extends FragmentStatePagerAdapter {
        public StandingsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return StandingsFragment.newInstance(mSchedule);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] standings = getResources().getStringArray(R.array.standings);
            return standings[position];
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.standings).length;
        }
    }
}
