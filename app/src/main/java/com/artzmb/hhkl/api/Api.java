package com.artzmb.hhkl.api;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.entity.MatchEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.Path;

public interface Api {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ONE, TWO})
    @interface League {}
    int ONE = 1;
    int TWO= 2;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({BRIEF, YELLOW, RED})
    @interface TableType {}
    String BRIEF = "brief";
    String YELLOW = "yellow";
    String RED = "red";

    @GET("/api/league/{league_id}/matches/")
    Call<DaysEntity> getMatches(
            @Path("league_id") @League int league
    );

    @PATCH("/api/matches/{match_id}")
    Call<Object> patchMatch(
            @Path("match_id") int matchId,
            Object patchedMatch
    );

    @GET("/api/league/{league_id}/players/")
    Call<Object> getPlayers(
            @Path("league_id") @League int league
    );

    @GET("/api/players/{player_id}")
    Call<Object> getPlayer(
            @Path("player_id") int id
    );

    @GET("/api/league/{league_id}/table/{table_id}")
    Call<Object> getTable(
            @Path("league_id") @League int league,
            @Path("table_id") @TableType int tableType
    );
}
