package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.DateService;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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
        Label label = new Label(event.getSummary().getValue());
        label.setWrapText(true);
        label.setMaxWidth(DayComponent.EVENT_WIDTH);

        int start = DateService.getHalfHourFromDateStart(event);

        getChildren().add(label);
        AnchorPane.setTopAnchor(this, (double) start <= 0 ? PADDING : DayComponent.ROW_HEIGHT * start);
        AnchorPane.setLeftAnchor(this, PADDING);
    }
}
