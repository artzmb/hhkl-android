package com.artzmb.hhkl;

import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.Schedule;
import com.artzmb.hhkl.model.StandingsLine;
import com.artzmb.hhkl.utils.RoundRobinScheduleFactory;

import java.util.ArrayList;
import java.util.List;

public class MockDataGenerator {

    public static Schedule generateSchedule() {
        return new RoundRobinScheduleFactory(4).makeSchedule(generatePlayers());
    }

    public static List<Player> generatePlayers() {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Моника", "МНК"));
        players.add(new Player("Камилла", "КМЛ"));
        players.add(new Player("Стелла", "СТЛ"));
        players.add(new Player("Виолетта", "ВЛТ"));
        players.add(new Player("Снежана", "СНЖ"));
        players.add(new Player("Элеонора", "ЭЛН"));
        players.add(new Player("Инесса", "ИНС"));
        players.add(new Player("Анжела", "АНЖ"));
        return players;
    }

    public static List<StandingsLine> generateStandings() {
        List<Player> players = generatePlayers();
        List<StandingsLine> lines = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            lines.add(new StandingsLine(players.get(i), 30, 16, 5, 4, 5, 104, 62));
        }
        return lines;
    }
}
