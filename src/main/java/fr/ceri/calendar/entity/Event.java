package fr.ceri.calendar.entity;

import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Location;
import biweekly.property.Summary;

import java.util.Calendar;
import java.util.TimeZone;

public class Event implements Comparable<Event> {

    private final Calendar startCalendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
    private DateStart startTime;
    private DateEnd endTime;
    private Summary summary;
    private Location location;

    public Event(DateStart startTime, DateEnd endTime, Summary summary, Location location) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.summary = summary;
        this.location = location;
        this.startCalendar.setTime(startTime.getValue());
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public DateStart getStartTime() {
        return startTime;
    }

    public void setStartTime(DateStart startTime) {
        this.startTime = startTime;
        this.startCalendar.setTime(startTime.getValue());
    }

    public DateEnd getEndTime() {
        return endTime;
    }

    public void setEndTime(DateEnd endTime) {
        this.endTime = endTime;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Event{startTime=%s, endTime=%s, summary=%s, location=%s}".formatted(startTime, endTime, summary, location);
    }

    @Override
    public int compareTo(Event o) {
        return startTime.getValue().compareTo(o.getStartTime().getValue());
    }
}
