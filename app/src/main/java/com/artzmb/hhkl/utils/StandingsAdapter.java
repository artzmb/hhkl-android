package com.artzmb.hhkl.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artzmb.hhkl.R;
import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.StandingsLine;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StandingsAdapter extends RecyclerView.Adapter<StandingsAdapter.LineViewHolder> {

    private Context mContext;
    private float mDensity;
    private List<StandingsLine> mLines = new ArrayList<>();

    public StandingsAdapter(Context context) {
        this.mContext = context;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        mDensity = dm.density;
    }

    @Override
    public LineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_standings, parent, false));
    }

    @Override
    public void onBindViewHolder(final LineViewHolder holder, int position) {
        StandingsLine line = mLines.get(position);

        holder.mTextViewPosition.setText(String.valueOf(position + 1));
        if (mDensity < 1.5) {
            holder.mTextViewPlayer.setText(line.getPlayer().getAlias());
        } else {
            holder.mTextViewPlayer.setText(line.getPlayer().getName());
        }
        holder.mTextViewPlayed.setText(String.valueOf(line.getPlayed()));
        holder.mTextViewWins.setText(String.valueOf(line.getWins()));
        holder.mTextViewOvertimeWins.setText(String.valueOf(line.getOvertimeWins()));
        holder.mTextViewOvertimeLosses.setText(String.valueOf(line.getOvertimeLosses()));
        holder.mTextViewLosses.setText(String.valueOf(line.getLosses()));
        holder.mTextViewGoalsDifference.setText(String.valueOf(line.getGoalsFor() - line.getGoalsAgainst()));
        holder.mTextViewPoints.setText(String.valueOf(line.getPoints()));
    }

    @Override
    public int getItemCount() {
        return mLines.size();
    }

    public void setItems(List<StandingsLine> lines) {
        mLines = lines;
        notifyDataSetChanged();
    }

    class LineViewHolder extends RecyclerView.ViewHolder {

        View mView;
        TextView mTextViewPosition;
        TextView mTextViewPlayer;
        TextView mTextViewPlayed;
        TextView mTextViewWins;
        TextView mTextViewOvertimeWins;
        TextView mTextViewOvertimeLosses;
        TextView mTextViewLosses;
        TextView mTextViewGoalsDifference;
        TextView mTextViewPoints;

        public LineViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mTextViewPosition = (TextView) itemView.findViewById(R.id.position);
            mTextViewPlayer = (TextView) itemView.findViewById(R.id.player);
            mTextViewPlayed = (TextView) itemView.findViewById(R.id.played);
            mTextViewWins = (TextView) itemView.findViewById(R.id.wins);
            mTextViewOvertimeWins = (TextView) itemView.findViewById(R.id.overtime_wins);
            mTextViewOvertimeLosses = (TextView) itemView.findViewById(R.id.overtime_losses);
            mTextViewLosses = (TextView) itemView.findViewById(R.id.losses);
            mTextViewGoalsDifference = (TextView) itemView.findViewById(R.id.goals_difference);
            mTextViewPoints = (TextView) itemView.findViewById(R.id.points);
        }
    }
}
