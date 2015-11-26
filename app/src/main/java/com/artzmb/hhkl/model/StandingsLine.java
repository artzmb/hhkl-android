package com.artzmb.hhkl.model;

import com.artzmb.hhkl.utils.Config;

public class StandingsLine {

    private Player player;
    private int played;
    private int wins;
    private int overtimeWins;
    private int overtimeLosses;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;
    private int points;

    public StandingsLine(Player player,
                         int played,
                         int wins,
                         int overtimeWins,
                         int overtimeLosses,
                         int losses,
                         int goalsFor,
                         int goalsAgainst) {
        this.player = player;
        this.played = played;
        this.wins = wins;
        this.overtimeWins = overtimeWins;
        this.overtimeLosses = overtimeLosses;
        this.losses = losses;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getPlayed() {
        return played;
    }

    public void setPlayed(int played) {
        this.played = played;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getOvertimeWins() {
        return overtimeWins;
    }

    public void setOvertimeWins(int overtimeWins) {
        this.overtimeWins = overtimeWins;
    }

    public int getOvertimeLosses() {
        return overtimeLosses;
    }

    public void setOvertimeLosses(int overtimeLosses) {
        this.overtimeLosses = overtimeLosses;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public int getPoints() {
        return Config.POINTS_FOR_WIN * wins +
                Config.POINTS_FOR_OVERTIME_WIN * overtimeWins +
                Config.POINTS_FOR_OVERTIME_LOSE * overtimeLosses +
                Config.POINTS_FOR_LOSE * losses;
    }
}
