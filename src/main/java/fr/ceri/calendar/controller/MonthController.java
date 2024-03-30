package fr.ceri.calendar.controller;

import fr.ceri.calendar.component.MonthGridPane;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsParser;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class MonthController implements Initializable {
    private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>();
    private List<Event> eventList;

    @FXML
    private Pane grid;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            eventList = EventListBuilder.buildEvents(IcsParser.parseIcsFile("m1-alt"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        localDateObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null == oldValue || oldValue.getMonth() != newValue.getMonth()) {
                buildGrid(
                        EventListBuilder.buildEventListByMonth(eventList, newValue)
                );
            }
        });
        localDateObjectProperty.setValue(LocalDate.now());

        datePicker.setValue(localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> localDateObjectProperty.set(datePicker.getValue()));
    }

    public void buildGrid(List<Event> events) {
        grid.getChildren().clear();
        MonthGridPane monthGridPane = new MonthGridPane(localDateObjectProperty.getValue());

        for (Event event : events) {
            LocalDate eventLocalDate = LocalDate.ofInstant(event.getStartTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));

            int col = eventLocalDate.getDayOfWeek().getValue() - 1;
            int row = getRow(eventLocalDate);

            Pane pane = monthGridPane.getNodeByRowCol(row, col);
            if (null != pane) {
                pane.getChildren().add(new Label(eventLocalDate.toString()));
            }

        }
        grid.getChildren().add(monthGridPane);
    }

    private int getRow(LocalDate date) {
        LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        DayOfWeek firstDayOfWeek = firstOfMonth.getDayOfWeek();
        int daysUntilDate = firstOfMonth.until(date).getDays();
        int offset = firstDayOfWeek == DayOfWeek.SUNDAY ? 6 : firstDayOfWeek.getValue() - 1;
        return (daysUntilDate + offset) / 5 + 1;
    }
}
