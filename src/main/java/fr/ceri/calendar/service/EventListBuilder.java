package fr.ceri.calendar.service;

import biweekly.component.VEvent;
import fr.ceri.calendar.entity.Event;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EventListBuilder {
    public static List<Event> buildEvents(List<VEvent> vEvents) {

        List<Event> events = new java.util.ArrayList<>(vEvents.stream().map(
                vEvent -> new Event(vEvent.getDateStart(), vEvent.getDateEnd(), vEvent.getSummary(), vEvent.getLocation())
        ).toList());
        Collections.sort(events);

        return events;
    }

    public static List<Event> buildEventListByDay(List<Event> events, LocalDate localDate) {
        return events.stream()
                .filter(event -> compareLocalDateToCalendarDay(localDate, event.getStartCalendar()))
                .toList();
    }

    public static List<Event> buildEventListByWeek(List<Event> events, LocalDate localDate) {
        return events.stream()
                .filter(event -> compareLocalDateToCalendarWeek(localDate, event.getStartCalendar()))
                .collect(Collectors.toList());
    }

    public static List<Event> buildEventListByMonth(List<Event> events, LocalDate localDate) {
        return events.stream()
                .filter(event -> compareLocalDateToCalendarMonth(localDate, event.getStartCalendar()))
                .collect(Collectors.toList());
    }

    private static boolean compareLocalDateToCalendarDay(LocalDate localDate, Calendar calendar) {
        return compareLocalDateToCalendarMonth(localDate, calendar)
                && localDate.getDayOfMonth() == calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static boolean compareLocalDateToCalendarWeek(LocalDate localDate, Calendar calendar) {
        LocalDate calendarDate = LocalDate.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId());

        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int localDateWeek = localDate.get(weekFields.weekOfWeekBasedYear());
        int calendarDateWeek = calendarDate.get(weekFields.weekOfWeekBasedYear());
        int localDateYear = localDate.get(weekFields.weekBasedYear());
        int calendarDateYear = calendarDate.get(weekFields.weekBasedYear());

        return localDateWeek == calendarDateWeek && localDateYear == calendarDateYear;
    }

    private static boolean compareLocalDateToCalendarMonth(LocalDate localDate, Calendar calendar) {
        return localDate.getYear() == calendar.get(Calendar.YEAR)
                && localDate.getMonthValue() == calendar.get(Calendar.MONTH) + 1;
    }
}
