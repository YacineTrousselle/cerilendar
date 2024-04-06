package fr.ceri.calendar.entity;

import java.util.StringJoiner;

public class EventSummary {
    private String course = "";
    private String teachers = "";
    private String promotion = "";
    private String type = "";
    private String details = "";

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getTeachers() {
        return teachers;
    }

    public void setTeachers(String teachers) {
        this.teachers = teachers;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(" - ");
        if (!course.isEmpty()) {
            joiner.add(course);
        }
        if (!teachers.isEmpty()) {
            joiner.add(teachers);
        }
        if (!promotion.isEmpty()) {
            joiner.add(promotion);
        }
        if (!type.isEmpty()) {
            joiner.add(type);
        }
        if (!details.isEmpty()) {
            joiner.add(details);
        }

        return joiner.toString();
    }
}
