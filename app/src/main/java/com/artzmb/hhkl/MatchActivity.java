package com.artzmb.hhkl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.api.Api;
import com.artzmb.hhkl.entity.MatchEntity;
import com.artzmb.hhkl.model.Match;
import com.artzmb.hhkl.model.Period;
import com.artzmb.hhkl.utils.CircleTransformation;
import com.artzmb.hhkl.utils.Config;
import com.artzmb.hhkl.utils.PeriodsAdapter;
import com.artzmb.hhkl.utils.PreferencesUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MatchActivity extends AppCompatActivity implements PeriodsAdapter.OnPeriodCompleteListener {

    public static final String EXTRA_MATCH = "com.artzmb.hhkl.extra_match";

    private static final int VIEW_SCORE = 0;
    private static final int VIEW_PREMATCH = 1;

    private static int PERIODS_TO_WIN = 2;

    private Match mMatch;

    AppBarLayout mAppBarLayout;
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    RecyclerView mRecyclerViewPeriods;
    ImageView mImageViewYellowAvatar;
    ImageView mImageViewRedAvatar;
    TextView mTextViewYellowPlayer;
    TextView mTextViewRedPlayer;
    ViewFlipper mViewFlipperScore;
    RelativeLayout mRelativeLayoutSummary;
    FloatingActionButton mFab;
    TextView mTextViewScoreYellow;
    TextView mTextViewScoreRed;

    private PeriodsAdapter mPeriodsAdapter;

    private Api mApi;

    public static Intent createIntent(Context context, Match match) {
        Intent i = new Intent(context, MatchActivity.class);
        i.putExtra(EXTRA_MATCH, match);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        mMatch = getIntent().getParcelableExtra(EXTRA_MATCH);
        mRecyclerViewPeriods = (RecyclerView) findViewById(R.id.periods);
        mImageViewYellowAvatar = (ImageView) findViewById(R.id.avatar_yellow);
        mImageViewRedAvatar = (ImageView) findViewById(R.id.avatar_red);
        mTextViewYellowPlayer = (TextView) findViewById(R.id.player_yellow);
        mTextViewRedPlayer = (TextView) findViewById(R.id.player_red);
        mViewFlipperScore = (ViewFlipper) findViewById(R.id.flipper_score);
        mRelativeLayoutSummary = (RelativeLayout) findViewById(R.id.summary);
        mTextViewScoreYellow = (TextView) findViewById(R.id.score_yellow);
        mTextViewScoreRed = (TextView) findViewById(R.id.score_red);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMatch.getStatus() == Match.STATUS_COMPLETED || mMatch.getStatus() == Match.STATUS_APPROVED) {
                    Toast.makeText(MatchActivity.this, getString(R.string.match_already_completed), Toast.LENGTH_SHORT).show();
                } else {
                    int messageId = mMatch.getStatus() == Match.STATUS_IDLE ? R.string.new_match_dialog_message : R.string.cancel_match_dialog_message;
                    int positiveId = mMatch.getStatus() == Match.STATUS_IDLE ? R.string.new_match_dialog_positive : R.string.cancel_match_dialog_positive;
                    final AlertDialog dialog = new AlertDialog.Builder(MatchActivity.this)
                            .setMessage(messageId)
                            .setPositiveButton(positiveId, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    if (mMatch.getStatus() == Match.STATUS_IDLE) {
                                        mMatch.setStatus(Match.STATUS_RUNNING);
                                        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_finish));
                                        mPeriodsAdapter.updateMatch(mMatch);
                                        mViewFlipperScore.setDisplayedChild(VIEW_SCORE);
                                        updateScore();
                                    } else if (mMatch.getStatus() == Match.STATUS_RUNNING) {
                                        mMatch.setStatus(Match.STATUS_IDLE);
                                        mFab.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                                        mPeriodsAdapter.updateMatch(mMatch);
                                        if (mMatch.getScore().getPeriods().isEmpty()) {
                                            mViewFlipperScore.setDisplayedChild(VIEW_PREMATCH);
                                        }
                                    }
                                    updateStatus();
                                }
                            })
                            .setNegativeButton(R.string.new_match_dialog_negative, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
            }
        });

        setupApi();
        setupToolbar();
        setupPeriods();
    }

    @SuppressWarnings("ConstantConditions")
    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle(mMatch.toString());
                    mRelativeLayoutSummary.animate().alpha(0.0f).setDuration(300);
                    isShow = true;
                } else if (isShow) {
                    mCollapsingToolbarLayout.setTitle("");
                    mRelativeLayoutSummary.animate().alpha(1.0f).setDuration(300);
                    mRelativeLayoutSummary.setVisibility(View.VISIBLE);
                    isShow = false;
                }
            }
        });

        populateContent();
    }

    private void setupPeriods() {
        mPeriodsAdapter = new PeriodsAdapter(this, mMatch);
        mPeriodsAdapter.setOnPeriodCompleteListener(this);
        mRecyclerViewPeriods.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewPeriods.setAdapter(mPeriodsAdapter);
    }

    private void setupApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PreferencesUtils.getApiUrl(getApplicationContext()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = retrofit.create(Api.class);
    }

    private void updateScore() {
        mTextViewScoreYellow.setText(String.valueOf(mMatch.getPeriodsWonByYellow()));
        mTextViewScoreRed.setText(String.valueOf(mMatch.getPeriodsWonByRed()));

        if (!mMatch.getScore().getPeriods().isEmpty()) {
            Map<String, ArrayList<ArrayList<Integer>>> patch = new HashMap<>();
            ArrayList<ArrayList<Integer>> score = new ArrayList<>();
            for (int i = 0; i < mMatch.getScore().getPeriods().size(); i++) {
                Period period = mMatch.getScore().getPeriods().get(i);
                ArrayList<Integer> periodScore = new ArrayList<>();
                periodScore.add(period.getYellowGoals());
                periodScore.add(period.getRedGoals());
                score.add(periodScore);
            }
            patch.put("score", score);
            Call<MatchEntity> call = mApi.patchMatch(mMatch.getId(), patch);
            call.enqueue(new Callback<MatchEntity>() {
                @Override
                public void onResponse(Response<MatchEntity> response, Retrofit retrofit) {

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

        if (mMatch.getStatus() != Match.STATUS_COMPLETED) {
            if (!TextUtils.isEmpty(mCollapsingToolbarLayout.getTitle())) {
                mCollapsingToolbarLayout.setTitle(mMatch.toString());
            }
        }
    }

    private void updateStatus() {
        Map<String, Integer> patch = new HashMap<>();
        patch.put("status", mMatch.getStatus());
        Call<MatchEntity> call = mApi.patchMatch(mMatch.getId(), patch);
        call.enqueue(new Callback<MatchEntity>() {
            @Override
            public void onResponse(Response<MatchEntity> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void populateContent() {
        Picasso.with(this)
                .load(R.drawable.placeholder)
                .transform(new CircleTransformation())
                .into(mImageViewYellowAvatar);
        Picasso.with(this)
                .load(R.drawable.placeholder)
                .transform(new CircleTransformation())
                .into(mImageViewRedAvatar);
        mTextViewYellowPlayer.setText(mMatch.getYellow().getName());
        mTextViewRedPlayer.setText(mMatch.getRed().getName());
        if (mMatch.getStatus() == Match.STATUS_IDLE) {
            mViewFlipperScore.setDisplayedChild(VIEW_PREMATCH);
        } else {
            mViewFlipperScore.setDisplayedChild(VIEW_SCORE);
        }
        if (mMatch.getStatus() == Match.STATUS_COMPLETED) {
            mFab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPeriodComplete(int number, Period period) {
        mMatch.getScore().addPeriod(period);
        if (mMatch.getPeriodsWonByRed() == PERIODS_TO_WIN || mMatch.getPeriodsWonByYellow() == PERIODS_TO_WIN) {
            mMatch.setStatus(Match.STATUS_COMPLETED);
        }
        mPeriodsAdapter.updateMatch(mMatch);
        mViewFlipperScore.setDisplayedChild(VIEW_SCORE);
        updateStatus();
        updateScore();
    }
}
