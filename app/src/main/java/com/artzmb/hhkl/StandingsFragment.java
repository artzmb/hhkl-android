package com.artzmb.hhkl;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.entity.StandingsEntity;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.StandingsLine;
import com.artzmb.hhkl.utils.Config;
import com.artzmb.hhkl.utils.DataMapper;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.StandingsAdapter;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class StandingsFragment extends Fragment implements TableActivity.OnRequestListener {

    private static final String EXTRA_TYPE = "com.artzmb.hhkl.extra_type";

    private String mTableType;
    private List<StandingsLine> mLines;

    RecyclerView mRecyclerViewStandings;

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
        mRecyclerViewStandings = (RecyclerView) v.findViewById(R.id.standings);
        mRecyclerViewStandings.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewStandings.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mStandingsAdapter = new StandingsAdapter(getContext());
        mRecyclerViewStandings.setAdapter(mStandingsAdapter);
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
        requestStandings(leagueLevel);
    }

    private void setupApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
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
                            }
                        }, 100);
                    }
                }).run();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setupMatches() {
        mStandingsAdapter.setItems(mLines);
    }
}
