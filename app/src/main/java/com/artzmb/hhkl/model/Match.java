package com.artzmb.hhkl.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Match implements Parcelable {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATUS_IDLE, STATUS_RUNNING, STATUS_COMPLETED})
    public @interface Status {}
    public static final int STATUS_IDLE = 0;
    public static final int STATUS_RUNNING= 1;
    public static final int STATUS_COMPLETED = 2;

    private Player yellow;
    private Player red;
    private Score score;
    @Status
    private int status = STATUS_IDLE;

    public Match(Match m) {
        this.yellow = m.yellow;
        this.red = m.red;
    }

    public Match(Player yellow, Player red) {
        this.yellow = yellow;
        this.red = red;
    }

    public void swap() {
        Player temp = yellow;
        yellow = red;
        red = temp;
    }

    public int getPeriodsWonByYellow() {
        int periodsWon = 0;
        if (score != null) {
            for (int i = 0; i < score.getPeriods().size(); i++) {
                Period period = score.getPeriods().get(i);
                if (period.getYellowGoals() > period.getRedGoals()) {
                    periodsWon += 1;
                }
            }
        }
        return periodsWon;
    }

    public int getPeriodsWonByRed() {
        int periodsWon = 0;
        if (score != null) {
            for (int i = 0; i < score.getPeriods().size(); i++) {
                Period period = score.getPeriods().get(i);
                if (period.getRedGoals() > period.getYellowGoals()) {
                    periodsWon += 1;
                }
            }
        }
        return periodsWon;
    }

    public Player getYellow() {
        return yellow;
    }

    public void setYellow(Player yellow) {
        this.yellow = yellow;
    }

    public Player getRed() {
        return red;
    }

    public void setRed(Player red) {
        this.red = red;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    @Status
    public int getStatus() {
        return status;
    }

    public void setStatus(@Status int status) {
        this.status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.yellow, 0);
        dest.writeParcelable(this.red, 0);
        dest.writeParcelable(this.score, 0);
        dest.writeInt(this.status);
    }

    @SuppressWarnings("ResourceType")
    protected Match(Parcel in) {
        this.yellow = in.readParcelable(Player.class.getClassLoader());
        this.red = in.readParcelable(Player.class.getClassLoader());
        this.score = in.readParcelable(Score.class.getClassLoader());
        this.status = in.readInt();
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        public Match createFromParcel(Parcel source) {
            return new Match(source);
        }

        public Match[] newArray(int size) {
            return new Match[size];
        }
    };
}
