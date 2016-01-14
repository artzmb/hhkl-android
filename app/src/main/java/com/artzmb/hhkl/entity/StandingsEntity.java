package com.artzmb.hhkl.entity;

import com.artzmb.hhkl.model.StandingsLine;

import java.util.ArrayList;

public class StandingsEntity {

    private ArrayList<StandingsLineEntity> standings = new ArrayList<>();

    public ArrayList<StandingsLineEntity> getStandings() {
        return standings;
    }

    public void setStandings(ArrayList<StandingsLineEntity> standings) {
        this.standings = standings;
    }
}
