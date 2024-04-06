package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.service.DateService;
import fr.ceri.calendar.service.EventService;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

public class EventComponent extends Pane {
    private static final double PADDING = 2.0;

    public EventComponent(Event event) {
        setStyle("-fx-background-color: #FFAE42");
        setMinWidth(DayComponent.EVENT_WIDTH - (PADDING * 2));
        setMaxWidth(DayComponent.EVENT_WIDTH - (PADDING * 2));
        setPrefWidth(DayComponent.EVENT_WIDTH - (PADDING * 2));

        int halfHoursDuration = DateService.getHalfHourBetweenDates(event);

        if (halfHoursDuration > DayComponent.NUMBER_OF_ROWS) {
            setMinHeight(DayComponent.GRID_HEIGHT - (2 * PADDING));
            setMaxHeight(DayComponent.GRID_HEIGHT - (2 * PADDING));
        } else {
            setMinHeight(DayComponent.ROW_HEIGHT * halfHoursDuration - PADDING);
            setMaxHeight(DayComponent.ROW_HEIGHT * halfHoursDuration - PADDING);
        }
        Label label = new Label(event.getSummary().getValue() + (
                null != event.getLocation() ? "\n" + event.getLocation().getValue() : ""
        ));
        label.setWrapText(true);
        label.setMaxWidth(DayComponent.EVENT_WIDTH);

        int start = DateService.getHalfHourFromDateStart(event);

        getChildren().add(label);
        AnchorPane.setTopAnchor(this, (double) start <= 0 ? PADDING : DayComponent.ROW_HEIGHT * start);
        AnchorPane.setLeftAnchor(this, PADDING);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem mailtoMenuItem = getMailtoMenuItem(event);
        contextMenu.getItems().add(mailtoMenuItem);
        setOnMouseClicked(event1 -> {
            if (event1.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(this, event1.getScreenX(), event1.getScreenY());
            }
        });
    }

    private static MenuItem getMailtoMenuItem(Event event) {
        MenuItem mailtoMenuItem = new MenuItem("Envoyer un mail");
        mailtoMenuItem.setOnAction(event1 -> {
            EventSummary eventSummary = EventService.parseEventSummary(event);
            if (eventSummary.getTeachers().isEmpty()) {
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            String mails = "mailto:"
                    + String.join(",", Arrays.stream(eventSummary.getTeachers().split(","))
                    .map(
                            teacher -> String.join(".", Arrays.stream(teacher.toLowerCase().trim().split(" ")).toList().reversed()) + "@univ-avignon.fr"
                    ).toList().reversed())
                    + "?subject=Did%20you%20know%20that%20in%20terms%20of";

            URI uri = URI.create(mails);
            try {
                desktop.mail(uri);
            } catch (IOException e) {
            }
        });

        return mailtoMenuItem;
    }
}
