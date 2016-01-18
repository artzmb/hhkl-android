package com.artzmb.hhkl.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.artzmb.hhkl.R;
import com.artzmb.hhkl.model.Match;
import com.artzmb.hhkl.model.Period;

import java.util.ArrayList;
import java.util.Arrays;

public class PeriodsAdapter extends RecyclerView.Adapter<PeriodsAdapter.PeriodViewHolder> {

    private static final int FIRST_PERIOD = 0;
    private static final int SECOND_PERIOD = 1;
    private static final int OVERTIME = 2;

    private static final int PERIODS = 3;
    private static final int MAX_GOALS = 10;

    String[] data = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private Context mContext;
    private Match mMatch;
    private StringSpinnerAdapter adapter;
    private OnPeriodCompleteListener onPeriodCompleteListener;

    public interface OnPeriodCompleteListener {
        void onPeriodComplete(int number, Period period);
    }

    public PeriodsAdapter(Context context, Match match) {
        mContext = context;
        mMatch = match;

        adapter = new StringSpinnerAdapter(context);
        adapter.setSelectedLayoutId(R.layout.spinner_item_selected);
        adapter.setDropdownLayoutId(R.layout.spinner_item_dropdown);
        adapter.addItems(new ArrayList<>(Arrays.asList(data)));
    }

    public void setOnPeriodCompleteListener(OnPeriodCompleteListener onPeriodCompleteListener) {
        this.onPeriodCompleteListener = onPeriodCompleteListener;
    }

    @Override
    public PeriodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PeriodViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_period, parent, false));
    }

    @Override
    public void onBindViewHolder(final PeriodViewHolder holder, final int position) {
        if (position == 0) {
            holder.mView.setPadding(0, mContext.getResources().getDimensionPixelOffset(R.dimen.stepper_first_element_margin_top), 0, 0);
        }
        if (position == PERIODS - 1) {
            holder.mViewConnector.setVisibility(View.GONE);
        }
        if ((mMatch.getStatus() == Match.STATUS_COMPLETED || mMatch.getStatus() == Match.STATUS_APPROVED) &&
                (mMatch.getScore().getPeriods().size() - 1) < position) {
            holder.mView.setVisibility(View.GONE);
        } else {
            holder.mView.setVisibility(View.VISIBLE);
            holder.mTextViewStepper.setText(String.valueOf(position + 1));
            holder.mSpinnerYellowGoals.setAdapter(adapter);
            holder.mSpinnerRedGoals.setAdapter(adapter);
            holder.mTextViewYellowPlayer.setText(mMatch.getYellow().getName());
            holder.mTextViewRedPlayer.setText(mMatch.getRed().getName());
            holder.mTextViewYellowPlayer2.setText(mMatch.getYellow().getName());
            holder.mTextViewRedPlayer2.setText(mMatch.getRed().getName());
            switch (position) {
                case FIRST_PERIOD:
                    holder.mTextViewPeriodName.setText(R.string.first_period);
                    break;
                case SECOND_PERIOD:
                    holder.mTextViewPeriodName.setText(R.string.second_period);
                    holder.mViewYellowPin.setBackgroundResource(R.drawable.red_side);
                    holder.mViewYellowPin2.setBackgroundResource(R.drawable.red_side);
                    holder.mViewRedPin.setBackgroundResource(R.drawable.yellow_side);
                    holder.mViewRedPin2.setBackgroundResource(R.drawable.yellow_side);
                    break;
                case OVERTIME:
                    holder.mTextViewPeriodName.setText(R.string.overtime);
                    break;
            }
            if (mMatch.getScore().getPeriods().size() > position) {
                holder.mViewIdle.setVisibility(View.GONE);
                holder.mViewRunning.setVisibility(View.GONE);
                holder.mViewCompleted.setVisibility(View.VISIBLE);
                holder.mViewConnector.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.mTextViewYellowGoals.setText(String.valueOf(mMatch.getScore().getPeriods().get(position).getYellowGoals()));
                holder.mTextViewRedGoals.setText(String.valueOf(mMatch.getScore().getPeriods().get(position).getRedGoals()));
            } else {
                holder.mViewIdle.setVisibility(
                        mMatch.getScore().getPeriods().size() == position && mMatch.getStatus() != Match.STATUS_IDLE
                                ? View.GONE : View.VISIBLE);
                holder.mViewRunning.setVisibility(
                        mMatch.getScore().getPeriods().size() == position && mMatch.getStatus() != Match.STATUS_IDLE
                                ? View.VISIBLE : View.GONE);
                holder.mViewCompleted.setVisibility(View.GONE);
            }
            holder.mButtonComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPeriodCompleteListener != null) {
                        int yellowGoals = Integer.parseInt((String) holder.mSpinnerYellowGoals.getSelectedItem());
                        int redGoals = Integer.parseInt((String) holder.mSpinnerRedGoals.getSelectedItem());
                        if ((yellowGoals != MAX_GOALS && redGoals != MAX_GOALS) || (yellowGoals == MAX_GOALS && redGoals == MAX_GOALS)) {
                            Toast.makeText(mContext, R.string.wrong_score, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        onPeriodCompleteListener.onPeriodComplete(position, new Period(yellowGoals, redGoals));
                    }
                }
            });
            if ((mMatch.getStatus() == Match.STATUS_COMPLETED || mMatch.getStatus() == Match.STATUS_APPROVED) &&
                    (mMatch.getScore().getPeriods().size() - 1) < (position + 1)) {
                holder.mViewConnector.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return PERIODS;
    }

    public void updateMatch(Match match) {
        mMatch = match;
        notifyDataSetChanged();
    }

    class PeriodViewHolder extends RecyclerView.ViewHolder {

        View mView;
        View mViewConnector;
        View mViewIdle;
        View mViewRunning;
        View mViewCompleted;
        TextView mTextViewStepper;
        TextView mTextViewPeriodName;
        Spinner mSpinnerYellowGoals;
        Spinner mSpinnerRedGoals;
        TextView mTextViewYellowPlayer;
        TextView mTextViewRedPlayer;
        Button mButtonComplete;
        TextView mTextViewYellowGoals;
        TextView mTextViewRedGoals;
        TextView mTextViewYellowPlayer2;
        TextView mTextViewRedPlayer2;
        View mViewYellowPin;
        View mViewRedPin;
        View mViewYellowPin2;
        View mViewRedPin2;

        public PeriodViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mViewConnector = itemView.findViewById(R.id.connector);
            mViewIdle = itemView.findViewById(R.id.idle);
            mViewRunning = itemView.findViewById(R.id.running);
            mViewCompleted = itemView.findViewById(R.id.completed);
            mTextViewStepper = (TextView) itemView.findViewById(R.id.stepper);
            mTextViewPeriodName = (TextView) itemView.findViewById(R.id.period_name);
            mSpinnerYellowGoals = (Spinner) itemView.findViewById(R.id.yellow_goals);
            mSpinnerRedGoals = (Spinner) itemView.findViewById(R.id.red_goals);
            mTextViewYellowPlayer = (TextView) itemView.findViewById(R.id.yellow_player);
            mTextViewRedPlayer = (TextView) itemView.findViewById(R.id.red_player);
            mButtonComplete = (Button) itemView.findViewById(R.id.complete);
            mTextViewYellowGoals = (TextView) itemView.findViewById(R.id.yellow_goals2);
            mTextViewRedGoals = (TextView) itemView.findViewById(R.id.red_goals2);
            mTextViewYellowPlayer2 = (TextView) itemView.findViewById(R.id.yellow_player2);
            mTextViewRedPlayer2 = (TextView) itemView.findViewById(R.id.red_player2);
            mViewYellowPin = itemView.findViewById(R.id.yellow_pin);
            mViewRedPin = itemView.findViewById(R.id.red_pin);
            mViewYellowPin2 = itemView.findViewById(R.id.yellow_pin2);
            mViewRedPin2 = itemView.findViewById(R.id.red_pin2);
        }
    }
}
