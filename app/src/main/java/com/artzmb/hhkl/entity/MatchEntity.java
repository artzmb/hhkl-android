package com.artzmb.hhkl.entity;

import com.artzmb.hhkl.model.Match;

public class MatchEntity {

    private int id;
    private PlayerEntity yellow;
    private PlayerEntity red;
    @Match.Status
    private int status;
    private ScoreEntity score;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerEntity getYellow() {
        return yellow;
    }

    public void setYellow(PlayerEntity yellow) {
        this.yellow = yellow;
    }

    public PlayerEntity getRed() {
        return red;
    }

    public void setRed(PlayerEntity red) {
        this.red = red;
    }

    @Match.Status
    public int getStatus() {
        return status;
    }

    public void setStatus(@Match.Status int status) {
        this.status = status;
    }

    public ScoreEntity getScore() {
        return score;
    }

    public void setScore(ScoreEntity score) {
        this.score = score;
    }
}
