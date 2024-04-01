package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.service.EventService;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class EventSummaryComponent extends VBox {

    public EventSummaryComponent(Event event) {
        EventSummary eventSummary = EventService.parseEventSummary(event);

        getChildren().add(new Label(eventSummary.getCourse()));
        if (!eventSummary.getTeachers().isEmpty()) {
            getChildren().add(new Label("Professeur(s): " + eventSummary.getTeachers()));
        }
        if (!eventSummary.getPromotion().isEmpty()) {
            getChildren().add(new Label("Groupe(s): " + eventSummary.getPromotion()));
        }
        if (!eventSummary.getType().isEmpty()) {
            getChildren().add(new Label("Type: " + eventSummary.getType()));
        }
        if (!eventSummary.getDetails().isEmpty()) {
            getChildren().add(new Label(eventSummary.getDetails()));
        }
    }
}
