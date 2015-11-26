package com.artzmb.hhkl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.StandingsLine;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.StandingsAdapter;

import java.util.List;

public class StandingsFragment extends Fragment {

    private static final String EXTRA_SCHEDULE = "com.artzmb.hhkl.extra_schedule";
    private Schedule mSchedule;
    private List<StandingsLine> mLines;

    RecyclerView mRecyclerViewStandings;

    private StandingsAdapter mStandingsAdapter;

    public static StandingsFragment newInstance(Schedule schedule) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SCHEDULE, schedule);
        StandingsFragment fragment = new StandingsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSchedule = getArguments().getParcelable(EXTRA_SCHEDULE);
        mLines = MockDataGenerator.generateStandings();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_standings, container, false);
        mRecyclerViewStandings = (RecyclerView) v.findViewById(R.id.standings);
        setupMatches();
        return v;
    }

    private void setupMatches() {
        if (mSchedule != null) {

            mStandingsAdapter = new StandingsAdapter(getContext());
            mRecyclerViewStandings.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewStandings.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            mRecyclerViewStandings.setAdapter(mStandingsAdapter);
            mStandingsAdapter.setItems(mLines);
        }
    }
}
