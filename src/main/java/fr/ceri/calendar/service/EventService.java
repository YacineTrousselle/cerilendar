package fr.ceri.calendar.service;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class EventService {
    private static String TEACHERS_REGEX = "(?:[A-Za-z]+(?:\\s[A-Za-z]+)?(?:,\\s)?)+";
    private static String[] TYPES = new String[]{"réunion", "entreprise", "voyage", "tp", "td", "occa", "soutenance", "evaluation", "entretien", "conference", "cm"};

    public static EventSummary parseEventSummary(Event event) {

        List<String> summary = new ArrayList<>(Arrays.asList(event.getSummary().getValue().split(" - ")));
        EventSummary eventSummary = new EventSummary();

        eventSummary.setCourse(summary.getFirst());
        summary.removeFirst();
        eventSummary.setTeachers(getTeachers(summary));
        eventSummary.setType(getType(summary));

        eventSummary.setPromotion(getPromotion(summary));

        if (!summary.isEmpty()) {
            eventSummary.setDetails(summary.getLast());
        }

        return eventSummary;
    }

    private static String getType(List<String> summary) {
        for (String field : summary) {
            for (String type : TYPES) {
                if (field.toLowerCase().startsWith(type)) {
                    summary.remove(field);

                    return field;
                }
            }
        }
        return "";
    }

    private static String getTeachers(List<String> summary) {
        Pattern pattern = Pattern.compile(TEACHERS_REGEX);
        if (pattern.matcher(summary.getFirst()).matches()) {

            String teachers = summary.getFirst();
            summary.removeFirst();
            return teachers;
        }
        return "";
    }

    private static String getPromotion(List<String> summary) {
        Pattern pattern = Pattern.compile("[ML]\\d([- ]).*");
        for (String field : summary) {
            if (pattern.matcher(field).matches()) {
                summary.remove(field);

                return field;
            }
        }

        return "";
    }
}
