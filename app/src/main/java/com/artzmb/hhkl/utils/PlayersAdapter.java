package com.artzmb.hhkl.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.artzmb.hhkl.R;
import com.artzmb.hhkl.model.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder> {

    private Context mContext;
    private List<Player> mPlayers = new ArrayList<>();

    public PlayersAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_player, parent, false));
    }

    @Override
    public void onBindViewHolder(final PlayerViewHolder holder, int position) {
        Player player = mPlayers.get(position);

        Picasso.with(holder.mView.getContext())
                .load(R.drawable.placeholder)
                .transform(new CircleTransformation())
                .into(holder.mImageViewAvatar);
        holder.mTextViewName.setText(player.getName());
    }

    @Override
    public int getItemCount() {
        return mPlayers.size();
    }

    public void setItems(List<Player> players) {
        mPlayers = players;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView mImageViewAvatar;
        TextView mTextViewName;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageViewAvatar = (ImageView) itemView.findViewById(R.id.avatar);
            mTextViewName = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
