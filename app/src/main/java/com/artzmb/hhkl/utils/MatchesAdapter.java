package com.artzmb.hhkl.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.MatchActivity;
import com.artzmb.hhkl.R;
import com.artzmb.hhkl.model.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private static final int VIEW_SCORE = 0;
    private static final int VIEW_PREMATCH = 1;

    private Context mContext;
    private List<Match> mMatches;
    private boolean mActive;

    public MatchesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MatchViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_match, parent, false));
    }

    @Override
    public void onBindViewHolder(final MatchViewHolder holder, int position) {
        final Match match = mMatches.get(position);

        holder.mLinearlayoutWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActive) {
                    mContext.startActivity(MatchActivity.createIntent(mContext, match));
                } else {
                    Toast.makeText(
                            mContext,
                            mContext.getString(R.string.day_has_not_started_yet),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Picasso.with(holder.mView.getContext())
                .load(R.drawable.placeholder)
                .transform(new CircleTransformation())
                .into(holder.mImageViewYellowAvatar);
        Picasso.with(holder.mView.getContext())
                .load(R.drawable.placeholder)
                .transform(new CircleTransformation())
                .into(holder.mImageViewRedAvatar);
        holder.mTextViewYellowScore.setText(String.valueOf(match.getPeriodsWonByYellow()));
        holder.mTextViewRedScore.setText(String.valueOf(match.getPeriodsWonByRed()));
        holder.mTextViewYellowAlias.setText(match.getYellow().getName());
        holder.mTextViewRedAlias.setText(match.getRed().getName());
        if (match.getStatus() == Match.STATUS_IDLE) {
            holder.mViewFlipperScore.setDisplayedChild(VIEW_PREMATCH);
        } else {
            holder.mViewFlipperScore.setDisplayedChild(VIEW_SCORE);
        }
    }

    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public void setItems(List<Match> matches) {
        mMatches = matches;
    }

    public void setActive(boolean active) {
        mActive = active;
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {

        View mView;
        LinearLayout mLinearlayoutWrapper;
        ImageView mImageViewYellowAvatar;
        ImageView mImageViewRedAvatar;
        TextView mTextViewYellowScore;
        TextView mTextViewRedScore;
        TextView mTextViewYellowAlias;
        TextView mTextViewRedAlias;
        ViewFlipper mViewFlipperScore;

        public MatchViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mLinearlayoutWrapper = (LinearLayout) itemView.findViewById(R.id.wrapper);
            mImageViewYellowAvatar = (ImageView) itemView.findViewById(R.id.avatar_yellow);
            mImageViewRedAvatar = (ImageView) itemView.findViewById(R.id.avatar_red);
            mTextViewYellowScore = (TextView) itemView.findViewById(R.id.score_yellow);
            mTextViewRedScore = (TextView) itemView.findViewById(R.id.score_red);
            mTextViewYellowAlias = (TextView) itemView.findViewById(R.id.alias_yellow);
            mTextViewRedAlias = (TextView) itemView.findViewById(R.id.alias_red);
            mViewFlipperScore = (ViewFlipper) itemView.findViewById(R.id.flipper_score);
        }
    }
}
