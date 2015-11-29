package com.artzmb.hhkl.utils;

import com.artzmb.hhkl.entity.DayEntity;
import com.artzmb.hhkl.entity.DaysEntity;
import com.artzmb.hhkl.entity.MatchEntity;
import com.artzmb.hhkl.entity.PeriodEntity;
import com.artzmb.hhkl.entity.PlayerEntity;
import com.artzmb.hhkl.entity.ScoreEntity;
import com.artzmb.hhkl.model.Day;
import com.artzmb.hhkl.model.Match;
import com.artzmb.hhkl.model.Period;
import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.Score;

public class DataMapper {

    public static Schedule transform(DaysEntity entity) {
        Schedule schedule = new Schedule();
        for (int i = 0; i < entity.getDays().size(); i++) {
            DayEntity dayEntity = entity.getDays().get(i);
            Day day = new Day();
            day.setNumber(dayEntity.getName());
            for (int j = 0; j < dayEntity.getMatches().size(); j++) {
                MatchEntity matchEntity = dayEntity.getMatches().get(j);
                Match match = new Match(
                        transform(matchEntity.getYellow()),
                        transform(matchEntity.getRed())
                );
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

    public static Player transform(PlayerEntity entity) {
        return new Player(
                entity.getName(),
                entity.getAlias()
        );
    }
}
