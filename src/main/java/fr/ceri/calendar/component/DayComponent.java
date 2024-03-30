package fr.ceri.calendar.component;

import fr.ceri.calendar.entity.Event;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class DayComponent extends AnchorPane {
    public final static int EVENT_WIDTH = 200;
    public final static int ROW_HEIGHT = 20;
    public final static int NUMBER_OF_ROWS = 23;
    public final static int GRID_WIDTH = EVENT_WIDTH;
    public final static int GRID_HEIGHT = NUMBER_OF_ROWS * ROW_HEIGHT;

    public DayComponent(List<Event> events) {
        setMinSize(GRID_WIDTH, GRID_HEIGHT);
        setMaxSize(GRID_WIDTH, GRID_HEIGHT);
        getChildren().add(new HourGridPane());
        for (Event event : events) {
            Pane eventComponent = new EventComponent(event);
            getChildren().add(eventComponent);
        }
    }
}
