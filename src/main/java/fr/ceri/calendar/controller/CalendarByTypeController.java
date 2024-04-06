package fr.ceri.calendar.controller;

import biweekly.component.VEvent;
import fr.ceri.calendar.component.WeekComponent;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarByTypeController implements Initializable {
    public final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>(LocalDate.now());
    private List<Event> events = new ArrayList<>();

    @FXML
    private ComboBox<String> type;

    @FXML
    private ComboBox<String> name;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Pane calendar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendar.getChildren().add(new WeekComponent(new ArrayList<>(), LocalDate.now()));
        datepicker.setValue(localDateObjectProperty.getValue());

        datepicker.setOnAction(event -> localDateObjectProperty.setValue(datepicker.getValue()));

        IcsManager icsManager = new IcsManager();
        type.getItems().addAll(IcsManager.FORMATIONS, IcsManager.TEACHERS, IcsManager.LOCATION);
        type.setOnAction(event -> {
            name.getItems().clear();
            try {
                name.getItems().addAll(switch (type.getValue()) {
                    case IcsManager.FORMATIONS -> icsManager.getFormations();
                    case IcsManager.TEACHERS -> icsManager.getTeachers();
                    case IcsManager.LOCATION -> icsManager.getLocations();
                    default -> throw new IllegalStateException("Unexpected value: " + type.getValue());
                });
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            name.setVisible(true);
            datepicker.setVisible(false);
            localDateObjectProperty.setValue(LocalDate.now());
            calendar.setVisible(false);
        });

        name.setOnAction(event -> {
            List<VEvent> vEvents = new ArrayList<>();
            try {
                vEvents = switch (type.getValue()) {
                    case IcsManager.FORMATIONS -> icsManager.getFormation(name.getValue());
                    case IcsManager.TEACHERS -> icsManager.getTeacher(name.getValue());
                    case IcsManager.LOCATION -> icsManager.getLocation(name.getValue());
                    default -> throw new IllegalStateException("Unexpected value: " + type.getValue());
                };
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            events = EventListBuilder.buildEvents(vEvents);

            datepicker.setVisible(true);
            calendar.setVisible(true);
        });

        localDateObjectProperty.addListener((observable, oldValue, newValue) -> {
            calendar.getChildren().set(0, new WeekComponent(EventListBuilder.buildEventListByWeek(events, newValue), newValue));
        });
        calendar.visibleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                calendar.getChildren().set(0, new WeekComponent(EventListBuilder.buildEventListByWeek(events, localDateObjectProperty.getValue()), localDateObjectProperty.getValue()));
            }
        });

    }
}
