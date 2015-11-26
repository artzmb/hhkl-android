package com.artzmb.hhkl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Period implements Parcelable {

    private int yellowGoals;
    private int redGoals;

    public int getYellowGoals() {
        return yellowGoals;
    }

    public void setYellowGoals(int yellowGoals) {
        this.yellowGoals = yellowGoals;
    }

    public int getRedGoals() {
        return redGoals;
    }

    public void setRedGoals(int redGoals) {
        this.redGoals = redGoals;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.yellowGoals);
        dest.writeInt(this.redGoals);
    }

    public Period() {
    }

    protected Period(Parcel in) {
        this.yellowGoals = in.readInt();
        this.redGoals = in.readInt();
    }

    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>() {
        public Period createFromParcel(Parcel source) {
            return new Period(source);
        }

        public Period[] newArray(int size) {
            return new Period[size];
        }
    };
}
