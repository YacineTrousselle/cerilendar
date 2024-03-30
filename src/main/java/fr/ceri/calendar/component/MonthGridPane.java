package fr.ceri.calendar.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class MonthGridPane extends GridPane {
    public final static int WIDTH = 200;
    public final static int HEIGHT = 100;

    public MonthGridPane(LocalDate localDate) {
        setPadding(new Insets(10));
        setHgap(10);
        setVgap(10);

        int i = 0;
        for (DayOfWeek day : DayOfWeek.values()) {
            if (DayOfWeek.SATURDAY != day && DayOfWeek.SUNDAY != day) {
                String dayName = day.getDisplayName(TextStyle.FULL, Locale.getDefault());
                add(new Label(dayName), i++, 0);
            }
        }

        LocalDate date = LocalDate.of(localDate.getYear(), localDate.getMonth(), 1);
        int row = 1;
        while (date.getMonth() == localDate.getMonth()) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                add(buildDayPane(date.getDayOfMonth()), dayOfWeek.getValue() - 1, row);
            }
            if (dayOfWeek == DayOfWeek.FRIDAY) {
                row++;
            }
            date = date.plusDays(1);
        }
    }

    public Pane getNodeByRowCol(int row, int column) {
        for (Node node : getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer columnIndex = GridPane.getColumnIndex(node);

            if (rowIndex != null && columnIndex != null && rowIndex == row && columnIndex == column && node instanceof Pane) {
                return (Pane) node;
            }
        }

        return null;
    }

    private Pane buildDayPane(int day) {
        VBox pane = new VBox();
        pane.setMaxSize(WIDTH, HEIGHT);
        pane.setMinSize(WIDTH, HEIGHT);

        pane.setBorder(Border.stroke(Paint.valueOf("black")));
        pane.getChildren().add(new Label(String.format("%d", day)));

        return pane;
    }
}
