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
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TableActivity extends BaseActivity {

    private static final int TAB_BRIEF = 0;
    private static final int TAB_YELLOW = 1;
    private static final int TAB_RED = 2;

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Spinner mSpinnerLeague;

    private PagerAdapter mPagerAdapter;
    private Schedule mSchedule;

    private List<OnRequestListener> requestListeners = new ArrayList<>();

    public interface OnRequestListener {
        void onRequest(int leagueLevel);
    }

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, TableActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    public void registerListener(OnRequestListener listener) {
        if (!requestListeners.contains(listener)) {
            requestListeners.add(listener);
        }
    }

    public void unregisterListener(OnRequestListener listener) {
        if (requestListeners.contains(listener)) {
            requestListeners.remove(listener);
        }
    }

    public void notifyListeners(int leagueLevel) {
        for (int i = 0; i < requestListeners.size(); i++) {
            requestListeners.get(i).onRequest(leagueLevel);
        }
    }

    public int getLeagueLevel() {
        return mSpinnerLeague.getSelectedItemPosition() + 1;
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
        mViewPager.setOffscreenPageLimit(2);

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
        mSpinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                notifyListeners(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
            switch (position) {
                case TAB_BRIEF:
                    return StandingsFragment.newInstance(Api.BRIEF);
                case TAB_YELLOW:
                    return StandingsFragment.newInstance(Api.YELLOW);
                case TAB_RED:
                    return StandingsFragment.newInstance(Api.RED);
            }
            return null;
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
