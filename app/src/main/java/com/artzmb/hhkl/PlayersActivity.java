package com.artzmb.hhkl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.entity.PlayersEntity;
import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.utils.Config;
import com.artzmb.hhkl.utils.DataMapper;
import com.artzmb.hhkl.utils.DividerItemDecoration;
import com.artzmb.hhkl.utils.PlayersAdapter;
import com.artzmb.hhkl.utils.StringSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class PlayersActivity extends BaseActivity {

    private static final int VIEW_LOADING = 0;
    private static final int VIEW_ERROR = 1;
    private static final int VIEW_DATA = 2;

    RecyclerView mRecyclerViewPlayers;
    Spinner mSpinnerLeague;
    ViewFlipper mViewFlipper;
    Button mReloadButton;

    private List<Player> mPlayers;
    private PlayersAdapter mPlayersAdapter;

    private Api mApi;

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
        mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
        mSpinnerLeague = (Spinner) findViewById(R.id.spinner_league);
        mReloadButton = (Button) findViewById(R.id.reload);

        setupApi();
        setupSpinner();
        setupPlayers();
        setupViewFlipper();
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
        mSpinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mViewFlipper.setDisplayedChild(VIEW_LOADING);
                requestPlayers(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        StringSpinnerAdapter spinnerAdapter = new StringSpinnerAdapter(this);
        spinnerAdapter.addItems(new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.leagues))));
        mSpinnerLeague.setAdapter(spinnerAdapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setupPlayers() {
        mPlayersAdapter = new PlayersAdapter(this);
        mRecyclerViewPlayers.addItemDecoration(new DividerItemDecoration(this, 1));
        mRecyclerViewPlayers.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPlayers.setAdapter(mPlayersAdapter);
        mPlayersAdapter.setItems(mPlayers);
    }

    private void setupViewFlipper() {
        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewFlipper.setDisplayedChild(VIEW_LOADING);
                requestPlayers(mSpinnerLeague.getSelectedItemPosition() + 1);
            }
        });
    }

    private void requestPlayers(int leagueLevel) {
        Call<PlayersEntity> call = mApi.getPlayers(leagueLevel);
        call.enqueue(new Callback<PlayersEntity>() {
            @Override
            public void onResponse(Response<PlayersEntity> response, Retrofit retrofit) {
                mViewFlipper.setDisplayedChild(VIEW_DATA);
                mPlayersAdapter.setItems(DataMapper.transform(response.body()));
                mPlayersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                mViewFlipper.setDisplayedChild(VIEW_ERROR);
            }
        });
    }

    @Override
    protected int provideContentViewLayoutResourceId() {
        return R.layout.activity_players;
    }
}
