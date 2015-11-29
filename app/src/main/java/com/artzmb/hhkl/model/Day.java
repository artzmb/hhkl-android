package com.artzmb.hhkl.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Day implements Parcelable {

    private int number;
    private ArrayList<Match> matches = new ArrayList<>();

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.number);
        dest.writeTypedList(matches);
    }

    public Day() {
    }

    protected Day(Parcel in) {
        this.number = in.readInt();
        this.matches = in.createTypedArrayList(Match.CREATOR);
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
