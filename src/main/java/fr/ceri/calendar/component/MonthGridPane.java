package fr.ceri.calendar.component;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
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
                add(buildDayPane(date), dayOfWeek.getValue() - 1, row);
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

    private Pane buildDayPane(LocalDate date) {
        int day = date.getDayOfMonth();
        BorderPane pane = new BorderPane();

        pane.setMaxSize(WIDTH, HEIGHT);
        pane.setMinSize(WIDTH, HEIGHT);
        pane.setBorder(Border.stroke(Paint.valueOf("black")));

        configureAction(pane, date);

        FlowPane subpane = new FlowPane();
        subpane.setOrientation(Orientation.HORIZONTAL);
        subpane.setMaxSize(WIDTH, HEIGHT);
        subpane.setVgap(2);
        subpane.setHgap(2);
        subpane.setPadding(new Insets(2));

        pane.setTop(new Label(String.format("%d", day)));
        pane.setBottom(subpane);

        return pane;
    }

    private void configureAction(Pane pane, LocalDate localDate) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem viewDay = new MenuItem("Voir jour");
        MenuItem viewWeek = new MenuItem("Voir semaine");

        viewDay.setOnAction(event -> {
            MainController.localDateObjectProperty.setValue(localDate);
            MainApplication.setScene("day");
        });
        viewWeek.setOnAction(event -> {
            MainController.localDateObjectProperty.setValue(localDate);
            MainApplication.setScene("week");
        });
        contextMenu.getItems().addAll(viewDay, viewWeek);
        pane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                contextMenu.show(pane, event.getScreenX(), event.getScreenY());
            }
        });
    }
}
