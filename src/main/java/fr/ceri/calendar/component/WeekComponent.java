package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class WeekComponent extends HBox {
    public WeekComponent(List<Event> events, LocalDate day) {
        setAlignment(Pos.CENTER);
        while (day.getDayOfWeek() != DayOfWeek.MONDAY) {
            day = day.minusDays(1);
        }
        do {
            getChildren().add(new DayComponent(EventListBuilder.buildEventListByDay(events, day)));
            day = day.plusDays(1);
        } while (day.getDayOfWeek() != DayOfWeek.SATURDAY);
    }
}
