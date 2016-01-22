package com.artzmb.hhkl;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.entity.StandingsEntity;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.StandingsLine;
import com.artzmb.hhkl.utils.Config;
import com.artzmb.hhkl.utils.DataMapper;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.PreferencesUtils;
import com.artzmb.hhkl.utils.StandingsAdapter;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class StandingsFragment extends Fragment
        implements TableActivity.OnRequestListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String EXTRA_TYPE = "com.artzmb.hhkl.extra_type";

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_ERROR = 1;
    private static final int VIEW_DATA = 2;

    private String mTableType;
    private List<StandingsLine> mLines;
    private int mLeagueLevel;

    RecyclerView mRecyclerViewStandings;
    ViewFlipper mViewFlipper;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button mReloadButton;

    private StandingsAdapter mStandingsAdapter;
    private Api mApi;

    public static StandingsFragment newInstance(@Api.TableType String tableType) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TYPE, tableType);
        StandingsFragment fragment = new StandingsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTableType = getArguments().getString(EXTRA_TYPE);

        setupApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_standings, container, false);
        mViewFlipper = (ViewFlipper) v.findViewById(R.id.flipper);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
        mRecyclerViewStandings = (RecyclerView) v.findViewById(R.id.standings);
        mReloadButton = (Button) v.findViewById(R.id.reload);

        setupStandings();
        setupSwipeRefreshLayout();
        setupViewFlipper();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof TableActivity) {
            ((TableActivity) getActivity()).registerListener(this);
            if (mLines == null || mLines.isEmpty()) {
                requestStandings(((TableActivity) getActivity()).getLeagueLevel());
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() instanceof TableActivity) {
            ((TableActivity) getActivity()).unregisterListener(this);
        }
    }

    @Override
    public void onRequest(int leagueLevel) {
        mLeagueLevel = leagueLevel;
        mViewFlipper.setDisplayedChild(VIEW_LOADING);
        requestStandings(leagueLevel);
    }

    private void setupApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PreferencesUtils.getApiUrl(getActivity()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
    }

    private void setupStandings() {
        mRecyclerViewStandings.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewStandings.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mStandingsAdapter = new StandingsAdapter(getContext());
        mRecyclerViewStandings.setAdapter(mStandingsAdapter);
    }

    private void setupSwipeRefreshLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupViewFlipper() {
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequest(mLeagueLevel);
            }
        });
    }

    private void requestStandings(int leagueLevel) {
        Call<StandingsEntity> call = mApi.getTable(leagueLevel, mTableType);
        call.enqueue(new Callback<StandingsEntity>() {
            @Override
            public void onResponse(final Response<StandingsEntity> response, Retrofit retrofit) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mLines = DataMapper.transform(response.body());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                setupMatches();
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

    private void setupMatches() {
        mStandingsAdapter.setItems(mLines);
    }

    @Override
    public void onRefresh() {
        onRequest(mLeagueLevel);
    }
}
