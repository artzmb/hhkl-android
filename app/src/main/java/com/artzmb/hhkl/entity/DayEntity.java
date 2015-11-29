package com.artzmb.hhkl.entity;

import java.util.ArrayList;

public class DayEntity {

    private int name;
    private ArrayList<MatchEntity> matches = new ArrayList<>();

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public ArrayList<MatchEntity> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<MatchEntity> matches) {
        this.matches = matches;
    }
}
