package com.artzmb.hhkl.utils;

import com.artzmb.hhkl.model.Day;
import com.artzmb.hhkl.model.Match;
import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundRobinScheduleFactory implements ScheduleFactory {
    private int iterations;

    public RoundRobinScheduleFactory(int iterations) {
        if (iterations <= 0) {
            throw new IllegalArgumentException("Iterations must be > 0");
        }
        this.iterations = iterations;
    }

    @Override
    public Schedule makeSchedule(List<Player> players) {

        Schedule schedule = new Schedule();

        int numberOfRounds = players.size();
        if (isEven(numberOfRounds)) {
            numberOfRounds -= 1;
        }
        ArrayList<ArrayList<Match>> rounds = new ArrayList<>(numberOfRounds);
        int[] ribbon = createRibbon(players.size());
        for (int i = 0; i < numberOfRounds; i++) {
            rounds.add(new ArrayList<Match>(players.size() / 2));

            for (int j = 0; j < ribbon.length / 2; j++) {
                int first = ribbon[j];
                int second = ribbon[ribbon.length - j - 1];
                if (second > first) {
                    int temp = second;
                    second = first;
                    first = temp;
                }
                if (first < players.size() && second < players.size()) {
                    Match match = new Match(players.get(first), players.get(second));
                    if (((first + second) % 2) == 0) {
                        match.swap();
                    }
                    rounds.get(i).add(match);
                }
            }

            rotateRibbon(ribbon);
        }

        ArrayList<Match> matches = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            Collections.shuffle(rounds);
            for (ArrayList<Match> round : rounds) {
                Collections.shuffle(round);
                for (Match m : round) {
                    Match copy = new Match(m);
                    if (isEven(i + 1)) {
                        copy.swap();
                    }
                    matches.add(copy);
                }
            }
        }

        int allRounds = numberOfRounds * iterations;
        int daySize = matches.size() / (allRounds);
        for (int i = 0; i < allRounds; i++) {
            Day day = new Day();
            day.setMatches(new ArrayList<>(matches.subList(i * daySize, i * daySize + daySize)));
            schedule.getDays().add(day);
        }

        return schedule;
    }

    private int[] createRibbon(int numberOfParticipants) {
        int[] ribbon = new int[isEven(numberOfParticipants) ? numberOfParticipants : numberOfParticipants + 1];
        for (int i = 0; i < ribbon.length; i++) {
            ribbon[i] = i;
        }
        return ribbon;
    }

    private void rotateRibbon(int[] ribbon) {
        int one = ribbon[1];
        ribbon[1] = ribbon[ribbon.length - 1];
        for (int i = ribbon.length - 1; i >= 2; i--) {
            if (i == 2) {
                ribbon[i] = one;
            } else {
                ribbon[i] = ribbon[i - 1];
            }
        }
    }

    private boolean isEven(int value) {
        return (value & 1) == 0;
    }
}
