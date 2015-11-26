package com.artzmb.hhkl.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Schedule implements Parcelable {

    private ArrayList<Day> days = new ArrayList<>();

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(days);
    }

    public Schedule() {
    }

    protected Schedule(Parcel in) {
        this.days = in.createTypedArrayList(Day.CREATOR);
    }

    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        public Schedule createFromParcel(Parcel source) {
            return new Schedule(source);
        }

        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}
