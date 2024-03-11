package fr.ceri.calendar.controller;

import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.DateService;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsParser;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class DayController implements Initializable {

    private static final int ROW_HEIGHT = 20;
    private static final int EVENT_WIDTH = 120;

    private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>();
    private List<Event> eventList;

    @FXML
    private DatePicker datePicker;

    @FXML
    private VBox dayColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dayColumn.setPrefWidth(EVENT_WIDTH);
        dayColumn.setPrefHeight(ROW_HEIGHT * 24 * 2);

        try {
            eventList = EventListBuilder.buildEvents(IcsParser.parseIcsFile("m1-alt"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        localDateObjectProperty.addListener((observable, oldValue, newValue) ->
                buildGrid(EventListBuilder.buildEventListByDay(eventList, newValue))
        );
        localDateObjectProperty.set(LocalDate.now());

        datePicker.setValue(localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> localDateObjectProperty.set(datePicker.getValue()));
    }

    private void buildGrid(List<Event> events) {

        for (int hour = 0; hour < 24; hour++) {
            for (int min = 0; min < 60; min += 30) {
                Pane halfHourSlot = new Pane();
                halfHourSlot.setPrefSize(EVENT_WIDTH, ROW_HEIGHT);
                dayColumn.getChildren().add(halfHourSlot);
            }
        }

        for (Event event : events) {
            Pane eventPane = new Pane();
            int startSlotIndex = DateService.getHalfHourFromDateStart(event.getStartTime());
            int durationInHalfHours = DateService.getHalfHourBetweenDates(event.getStartTime(), event.getEndTime());

            eventPane.setStyle("-fx-background-color: lightblue; -fx-border-color: black;");
            eventPane.setPrefSize(EVENT_WIDTH, ROW_HEIGHT * durationInHalfHours);

            eventPane.getChildren().add(new Label(event.getSummary().getValue()));

            eventPane.setLayoutY(startSlotIndex * ROW_HEIGHT);

            dayColumn.getChildren().add(eventPane);
        }
    }
}
