package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class MonthComponent extends StackPane {
    public static final int RADIUS = 10;

    public MonthComponent(List<Event> events, LocalDate localDate, ObjectProperty<LocalDate> localDateObjectProperty, ToggleGroup viewModeToggleGroup) {
        setAlignment(Pos.CENTER);
        getChildren().clear();
        MonthGridPane monthGridPane = new MonthGridPane(localDate, localDateObjectProperty, viewModeToggleGroup);

        for (Event event : events) {
            LocalDate eventLocalDate = LocalDate.ofInstant(event.getStartTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));

            if (eventLocalDate.getDayOfWeek() != DayOfWeek.SATURDAY && eventLocalDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                int col = eventLocalDate.getDayOfWeek().getValue() - 1;
                int row = getRow(eventLocalDate);

                Pane pane = monthGridPane.getNodeByRowCol(row, col);
                if (null != pane) {
                    Pane subpane = (Pane) pane.getChildren().get(1);
                    Pane circlePane = buildDayComponent(event);
                    subpane.getChildren().add(circlePane);
                }
            }
        }

        getChildren().addAll(monthGridPane);
    }

    private int getRow(LocalDate date) {
        LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);

        int row = 1;
        while (firstOfMonth.getDayOfMonth() != date.getDayOfMonth()) {
            if (firstOfMonth.getDayOfWeek() == DayOfWeek.FRIDAY) {
                row++;
            }
            firstOfMonth = firstOfMonth.plusDays(1);
        }

        return row;
    }

    private Pane buildDayComponent(Event event) {
        Pane pane = new VBox();
        Circle circle = new Circle(RADIUS, Color.BLUE);

        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(new EventSummaryComponent(event));
        Tooltip.install(circle, tooltip);

        pane.getChildren().add(circle);

        return pane;
    }
}
