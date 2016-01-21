package com.artzmb.hhkl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.utils.Config;
import com.artzmb.hhkl.utils.DataMapper;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import java.util.ArrayList;
import java.util.Arrays;

public class FixtureActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_ERROR = 1;
    private static final int VIEW_DATA = 2;

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Spinner mSpinnerLeague;
    ViewFlipper mViewFlipper;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button mReloadButton;

    private PagerAdapter mPagerAdapter;

    private Schedule mSchedule;
    private int mLastActiveDay;
    private int mLastSelectedDay;

    private Api mApi;

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

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pages);
        mSpinnerLeague = (Spinner) findViewById(R.id.spinner_league);
        mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mReloadButton = (Button) findViewById(R.id.reload);

        setupApi();
        setupSpinner();
        setupSwipeRefreshLayout();
        setupViewFlipper();
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_fixture;
    }

    private void setupApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
    }

    @SuppressWarnings("ConstantConditions")
    private void setupSpinner() {
        StringSpinnerAdapter spinnerAdapter = new StringSpinnerAdapter(this);
        spinnerAdapter.addItems(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues))));
        mSpinnerLeague.setAdapter(spinnerAdapter);
        mSpinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewFlipper.setDisplayedChild(VIEW_LOADING);
                requestFixture(position + 1, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupTabs() {

        mPagerAdapter = new DayPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        for (int i = 1; i <= mSchedule.getDays().size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab());
        }
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mLastActiveDay);
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupViewFlipper() {
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.setDisplayedChild(VIEW_LOADING);
                requestFixture(mSpinnerLeague.getSelectedItemPosition() + 1, false);
            }
        });
    }

    private void requestFixture(int leagueLevel, final boolean saveSelectedDay) {
        Call<DaysEntity> call = mApi.getMatches(leagueLevel);
        call.enqueue(new Callback<DaysEntity>() {
            @Override
            public void onResponse(final Response<DaysEntity> response, Retrofit retrofit) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mSchedule = DataMapper.transform(response.body());
                        if (saveSelectedDay) {
                            mLastActiveDay = mLastSelectedDay;
                        } else {
                            for (int i = mSchedule.getDays().size() - 1; i >= 0; i--) {
                                if (mSchedule.getDays().get(i).isActive()) {
                                    mLastActiveDay = i;
                                    break;
                                }
                            }
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setupTabs();
                                mViewFlipper.setDisplayedChild(VIEW_DATA);
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }, 100);
                    }
                }).run();
            }

            @Override
            public void onFailure(Throwable t) {
                mViewFlipper.setDisplayedChild(VIEW_ERROR);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mLastSelectedDay = mTabLayout.getSelectedTabPosition();
        requestFixture(mSpinnerLeague.getSelectedItemPosition() + 1, true);
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
            return String.format("%s %d", getString(R.string.tab_day), mSchedule.getDays().get(position).getNumber());
        }

        @Override
        public int getCount() {
            return mSchedule.getDays().size();
        }
    }
}
