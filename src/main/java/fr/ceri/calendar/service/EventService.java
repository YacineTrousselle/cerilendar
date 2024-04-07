package fr.ceri.calendar.service;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.entity.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class EventService {
    public static String TEACHERS_REGEX = "(?:[A-Za-z]+(?:\\s[A-Za-z]+)?(?:,\\s)?)+";
    private static String[] TYPES = new String[]{"réunion", "entreprise", "voyage", "tp", "td", "occa", "soutenance", "evaluation", "entretien", "conf", "cm"};

    public static String[] AUTHORIZED_TYPES = new String[]{
            "CM",
            "TD",
            "TP",
            "Evaluation",
            "Réunion",
    };

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

    public static HashMap<Filter.FilterType, List<String>> getAllFilters(List<Event> events) {
        HashMap<Filter.FilterType, List<String>> filters = new HashMap<>();
        filters.put(Filter.FilterType.LOCATION, new ArrayList<>());
        filters.put(Filter.FilterType.COURSE, new ArrayList<>());
        filters.put(Filter.FilterType.TEACHER, new ArrayList<>());
        filters.put(Filter.FilterType.TYPE, new ArrayList<>());

        for (Event event : events) {
            if (null != event.getLocation() && !filters.get(Filter.FilterType.LOCATION).contains(event.getLocation().getValue())) {
                filters.get(Filter.FilterType.LOCATION).add(event.getLocation().getValue());
            }
            EventSummary eventSummary = parseEventSummary(event);
            if (!eventSummary.getCourse().isEmpty() && !filters.get(Filter.FilterType.COURSE).contains(eventSummary.getCourse())) {
                filters.get(Filter.FilterType.COURSE).add(eventSummary.getCourse());
            }
            if (!eventSummary.getType().isEmpty() && !filters.get(Filter.FilterType.TYPE).contains(eventSummary.getType())) {
                filters.get(Filter.FilterType.TYPE).add(eventSummary.getType());
            }
            if (!eventSummary.getTeachers().isEmpty()) {
                List<String> teachers = Arrays.stream(eventSummary.getTeachers().split(", ")).toList();
                for (String teacher : teachers) {
                    if (!filters.get(Filter.FilterType.TEACHER).contains(teacher)) {
                        filters.get(Filter.FilterType.TEACHER).add(teacher);
                    }
                }
            }
        }

        return filters;
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
        if (!summary.isEmpty() && pattern.matcher(summary.getFirst()).matches()) {
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
