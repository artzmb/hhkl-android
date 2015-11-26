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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixtureActivity extends BaseActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Spinner mSpinnerLeague;

    private PagerAdapter mPagerAdapter;

    private Schedule mSchedule;

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, FixtureActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        mSchedule = MockDataGenerator.generateSchedule();

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pages);
        mSpinnerLeague = (Spinner) findViewById(R.id.spinner_league);

        setupSpinner();
        setupTabs();
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_fixture;
    }

    @SuppressWarnings("ConstantConditions")
    private void setupSpinner() {
        StringSpinnerAdapter spinnerAdapter = new StringSpinnerAdapter(this);
        spinnerAdapter.addItems(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues))));
        mSpinnerLeague.setAdapter(spinnerAdapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupTabs() {

        mPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        for (int i = 1; i <= mSchedule.getDays().size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private class DayPagerAdapter extends FragmentStatePagerAdapter {
        public DayPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DayFragment.newInstance(mSchedule.getDays().get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return String.format("%s %d", getString(R.string.tab_day), position + 1);
        }

        @Override
        public int getCount() {
            return mSchedule.getDays().size();
        }
    }
}
