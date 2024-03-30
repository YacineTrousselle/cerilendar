package fr.ceri.calendar.service;

import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import fr.ceri.calendar.entity.Event;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class DateService {

    public static int getHalfHourBetweenDates(Event event) {
        return DateService.getHalfHourBetweenDates(event.getStartTime(), event.getEndTime());
    }

    public static int getHalfHourBetweenDates(DateStart startTime, DateEnd endTime) {
        long secondsBetween = ChronoUnit.SECONDS.between(startTime.getValue().toInstant(), endTime.getValue().toInstant());

        return (int) (secondsBetween / 1800);
    }

    public static int getHalfHourFromDateStart(Event event) {
        LocalDateTime localDate = LocalDateTime.ofInstant(event.getStartTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));
        LocalDateTime topLocalDate = LocalDateTime.of(localDate.toLocalDate(), LocalTime.of(7, 30));

        return (int) (Duration.between(topLocalDate, localDate).toMinutes() / 30);

    }
}
