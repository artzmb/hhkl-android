package com.artzmb.hhkl.utils;

import com.artzmb.hhkl.entity.DayEntity;
import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.entity.MatchEntity;
import com.artzmb.hhkl.entity.PeriodEntity;
import com.artzmb.hhkl.entity.PlayerEntity;
import com.artzmb.hhkl.entity.PlayersEntity;
import com.artzmb.hhkl.entity.ScoreEntity;
import com.artzmb.hhkl.entity.StandingsEntity;
import com.artzmb.hhkl.entity.StandingsLineEntity;
import com.artzmb.hhkl.model.Day;
import com.artzmb.hhkl.model.Match;
import com.artzmb.hhkl.model.Period;
import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.Score;
import com.artzmb.hhkl.model.StandingsLine;

import java.util.ArrayList;
import java.util.List;

public class DataMapper {

    public static Schedule transform(DaysEntity entity) {
        Schedule schedule = new Schedule();
        for (int i = 0; i < entity.getDays().size(); i++) {
            DayEntity dayEntity = entity.getDays().get(i);
            Day day = new Day();
            day.setNumber(dayEntity.getName());
            day.setActive(dayEntity.isActive());
            for (int j = 0; j < dayEntity.getMatches().size(); j++) {
                MatchEntity matchEntity = dayEntity.getMatches().get(j);
                Match match = new Match(
                        transform(matchEntity.getYellow()),
                        transform(matchEntity.getRed())
                );
                match.setId(matchEntity.getId());
                match.setStatus(matchEntity.getStatus());
                Score score = new Score();
                for (int k = 0; k < matchEntity.getScore().size(); k++) {
                    PeriodEntity periodEntity = matchEntity.getScore().get(k);
                    Period period = new Period();
                    period.setYellowGoals(periodEntity.get(0));
                    period.setRedGoals(periodEntity.get(1));
                    score.addPeriod(period);
                }
                match.setScore(score);
                match.setStatus(matchEntity.getStatus());
                day.getMatches().add(match);
            }
            schedule.getDays().add(day);
        }
        return schedule;
    }

    public static List<StandingsLine> transform(StandingsEntity entity) {
        List<StandingsLine> lines = new ArrayList<>();
        for (int i = 0; i < entity.getStandings().size(); i++) {
            StandingsLineEntity lineEntity = entity.getStandings().get(i);
            StandingsLine line = new StandingsLine(
                    transform(lineEntity.getPlayer()),
                    lineEntity.getPlayed(),
                    lineEntity.getWins(),
                    lineEntity.getOvertimeWins(),
                    lineEntity.getOvertimeLosses(),
                    lineEntity.getLosses(),
                    lineEntity.getGoalsFor(),
                    lineEntity.getGoalsAgainst()
            );
            lines.add(line);
        }
        return lines;
    }

    public static List<Player> transform(PlayersEntity entity) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < entity.getPlayers().size(); i++) {
            PlayerEntity playerEntity = entity.getPlayers().get(i);
            players.add(transform(playerEntity));
        }
        return players;
    }

    public static Player transform(PlayerEntity entity) {
        return new Player(
                entity.getName(),
                entity.getAlias()
        );
    }
}
