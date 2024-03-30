package fr.ceri.calendar.component;

import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;

public class HourGridPane extends Pane {
    public HourGridPane() {
        setMinSize(DayComponent.GRID_WIDTH, DayComponent.GRID_HEIGHT);
        setMaxSize(DayComponent.GRID_WIDTH, DayComponent.GRID_HEIGHT);
        setBorder(Border.stroke(Paint.valueOf("black")));
        for (int i = 1; i < DayComponent.NUMBER_OF_ROWS; i++) {
            Line line = new Line(0, i * DayComponent.ROW_HEIGHT, DayComponent.GRID_WIDTH, i * DayComponent.ROW_HEIGHT);
            getChildren().add(line);
        }
    }
}
