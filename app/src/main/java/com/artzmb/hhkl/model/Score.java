package com.artzmb.hhkl.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Score implements Parcelable {

    private ArrayList<Period> periods = new ArrayList<>();

    public void addPeriod(Period period) {
        periods.add(period);
    }

    public ArrayList<Period> getPeriods() {
        return periods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(periods);
    }

    public Score() {
    }

    protected Score(Parcel in) {
        this.periods = in.createTypedArrayList(Period.CREATOR);
    }

    public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
        public Score createFromParcel(Parcel source) {
            return new Score(source);
        }

        public Score[] newArray(int size) {
            return new Score[size];
        }
    };
}
