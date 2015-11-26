package com.artzmb.hhkl.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.artzmb.hhkl.R;
import com.artzmb.hhkl.model.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {

    private static final int VIEW_SCORE = 0;
    private static final int VIEW_PREMATCH = 1;

    private Context mContext;
    private List<Match> mMatches;

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
                if (match.getStatus() == Match.STATUS_IDLE) {
                    holder.mButtonStartMatch.setVisibility(holder.mButtonStartMatch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    holder.mRelativeLayoutDetails.setVisibility(View.GONE);
                } else {
                    holder.mRelativeLayoutDetails.setVisibility(holder.mRelativeLayoutDetails.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    holder.mButtonStartMatch.setVisibility(View.GONE);
                }
            }
        });
        Picasso.with(holder.mView.getContext())
                .load(R.drawable.monica)
                .transform(new CircleTransformation())
                .into(holder.mImageViewYellowAvatar);
        Picasso.with(holder.mView.getContext())
                .load(R.drawable.monica)
                .transform(new CircleTransformation())
                .into(holder.mImageViewRedAvatar);
        holder.mTextViewYellowScore.setText(String.valueOf(match.getPeriodsWonByYellow()));
        holder.mTextViewRedScore.setText(String.valueOf(match.getPeriodsWonByRed()));
        holder.mTextViewYellowAlias.setText(match.getYellow().getName());
        holder.mTextViewRedAlias.setText(match.getRed().getName());
        holder.mTextViewYellowName.setText(match.getYellow().getName());
        holder.mTextViewRedName.setText(match.getRed().getName());
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

    class MatchViewHolder extends RecyclerView.ViewHolder {

        View mView;
        LinearLayout mLinearlayoutWrapper;
        ImageView mImageViewYellowAvatar;
        ImageView mImageViewRedAvatar;
        TextView mTextViewYellowScore;
        TextView mTextViewRedScore;
        TextView mTextViewYellowAlias;
        TextView mTextViewRedAlias;
        TextView mTextViewYellowName;
        TextView mTextViewRedName;
        ViewFlipper mViewFlipperScore;
        RelativeLayout mRelativeLayoutDetails;
        Button mButtonStartMatch;

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
            mTextViewYellowName = (TextView) itemView.findViewById(R.id.name_yellow);
            mTextViewRedName = (TextView) itemView.findViewById(R.id.name_red);
            mViewFlipperScore = (ViewFlipper) itemView.findViewById(R.id.flipper_score);
            mRelativeLayoutDetails = (RelativeLayout) itemView.findViewById(R.id.details);
            mButtonStartMatch = (Button) itemView.findViewById(R.id.start_match);
        }
    }
}
