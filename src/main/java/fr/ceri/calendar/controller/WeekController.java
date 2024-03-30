package fr.ceri.calendar.controller;

import fr.ceri.calendar.component.DayComponent;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsParser;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class WeekController implements Initializable {
    private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>();
    private List<Event> eventList;

    @FXML
    private Pane eventContainer;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            eventList = EventListBuilder.buildEvents(IcsParser.parseIcsFile("m1-alt"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        localDateObjectProperty.addListener((observable, oldValue, newValue) ->
                buildGrid(EventListBuilder.buildEventListByWeek(eventList, newValue))
        );
        localDateObjectProperty.set(LocalDate.now());

        datePicker.setValue(localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> localDateObjectProperty.set(datePicker.getValue()));
    }

    private void buildGrid(List<Event> events) {
        eventContainer.getChildren().clear();

        LocalDate day = localDateObjectProperty.getValue();
        while (day.getDayOfWeek() != DayOfWeek.MONDAY) {
            day = day.minusDays(1);
        }
        do {
            eventContainer
                    .getChildren()
                    .add(new DayComponent(
                            EventListBuilder.buildEventListByDay(events, day)
                    ));
            day = day.plusDays(1);
        } while (day.getDayOfWeek() != DayOfWeek.SATURDAY);
    }

}
