package com.artzmb.hhkl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.artzmb.hhkl.model.Day;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.MatchesAdapter;

public class DayFragment extends Fragment {

    private static final String EXTRA_DAY = "com.artzmb.hhkl.extra_day";
    private Day mDay;

    RecyclerView mRecyclerViewMatches;

    private MatchesAdapter mMatchesAdapter;

    public static DayFragment newInstance(Day day) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DAY, day);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDay = getArguments().getParcelable(EXTRA_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_day, container, false);
        mRecyclerViewMatches = (RecyclerView) v.findViewById(R.id.matches);
        setupMatches();
        return v;
    }

    private void setupMatches() {
        if (mDay != null) {
            mMatchesAdapter = new MatchesAdapter(getContext());
            mRecyclerViewMatches.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerViewMatches.setAdapter(mMatchesAdapter);
            mMatchesAdapter.setItems(mDay.getMatches());
            mMatchesAdapter.setActive(mDay.isActive());
        }
    }
}
