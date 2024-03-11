package fr.ceri.calendar.service;

import biweekly.property.DateEnd;
import biweekly.property.DateStart;

import java.time.temporal.ChronoUnit;

public class DateService {

    public static int getHalfHourBetweenDates(DateStart startTime, DateEnd endTime) {
        long secondsBetween = ChronoUnit.SECONDS.between(startTime.getValue().toInstant(), endTime.getValue().toInstant());

        return (int) (secondsBetween / 1800);
    }

    public static int getHalfHourFromDateStart(DateStart startTime) {
        return startTime.getValue().getRawComponents().getSecond() / 1800;
    }
}
