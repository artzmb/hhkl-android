package com.artzmb.hhkl.utils;

import com.artzmb.hhkl.model.Player;
import com.artzmb.hhkl.model.Schedule;

import java.util.List;

public interface ScheduleFactory {
    Schedule makeSchedule(List<Player> players);
}
