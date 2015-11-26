package com.artzmb.hhkl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Spinner;

import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.PlayersAdapter;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayersActivity extends BaseActivity {

    RecyclerView mRecyclerViewPlayers;
    Spinner mSpinnerLeague;

    private List<Player> mPlayers;
    private PlayersAdapter playersAdapter;

    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, PlayersActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPlayers = MockDataGenerator.generatePlayers();

        mRecyclerViewPlayers = (RecyclerView) findViewById(R.id.players);
        mSpinnerLeague = (Spinner) findViewById(R.id.spinner_league);

        setupSpinner();
        setupPlayers();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupSpinner() {
        StringSpinnerAdapter spinnerAdapter = new StringSpinnerAdapter(this);
        spinnerAdapter.addItems(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues))));
        mSpinnerLeague.setAdapter(spinnerAdapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupPlayers() {
        playersAdapter = new PlayersAdapter(this);
        mRecyclerViewPlayers.addItemDecoration(new DividerItemDecoration(this, 1));
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPlayers.setAdapter(playersAdapter);
        playersAdapter.setItems(mPlayers);
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_players;
    }
}
