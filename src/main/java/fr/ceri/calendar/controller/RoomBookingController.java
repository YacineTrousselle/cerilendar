package fr.ceri.calendar.controller;

import biweekly.component.VEvent;
import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.WeekComponent;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.EventService;
import fr.ceri.calendar.service.IcsManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class RoomBookingController implements Initializable {
    public final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>(LocalDate.now());
    private List<Event> events;

    @FXML
    private ComboBox<String> rooms;

    @FXML
    private DatePicker datepicker;

    @FXML
    private Pane calendar;

    @FXML
    private GridPane form;

    private String room;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        calendar.getChildren().add(new WeekComponent(new ArrayList<>(), LocalDate.now()));
        IcsManager icsManager = new IcsManager();
        try {
            rooms.getItems().addAll(icsManager.getLocations());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        datepicker.setValue(localDateObjectProperty.getValue());
        datepicker.setOnAction(event -> localDateObjectProperty.setValue(datepicker.getValue()));

        rooms.setOnAction(event -> {
            if (null == rooms.getValue()) {
                return;
            }
            List<VEvent> vEvents = new ArrayList<>();
            try {
                vEvents = icsManager.getLocation(rooms.getValue());
            } catch (URISyntaxException | IOException e) {
                System.err.println(e.getMessage());
            }
            events = EventListBuilder.buildEvents(vEvents);

            room = null;
            if (!events.isEmpty()) {
                room = events.getFirst().getLocation().getValue();
            }
            calendar.getChildren().set(0, new WeekComponent(EventListBuilder.buildEventListByWeek(events, localDateObjectProperty.getValue()), localDateObjectProperty.getValue()));

            datepicker.setVisible(true);
            calendar.setVisible(true);
            form.setVisible(true);
        });
        localDateObjectProperty.addListener((observable, oldValue, newValue) -> {
            calendar.getChildren().set(0, new WeekComponent(EventListBuilder.buildEventListByWeek(events, newValue), newValue));

        });

        initForm(icsManager);
    }

    private void initForm(IcsManager icsManager) {
        form.setVisible(false);
        form.setMaxWidth(220);
        form.setHgap(4.0);
        form.setVgap(8.0);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(0, 10.0, 0, 10.0));

        ComboBox<String> startTime = new ComboBox<>();
        ComboBox<String> endTime = new ComboBox<>();
        for (int hour = 7; hour < 19; hour++) {
            startTime.getItems().add(String.format("%02d:00", hour));
            endTime.getItems().add(String.format("%02d:00", hour));
            startTime.getItems().add(String.format("%02d:30", hour));
            endTime.getItems().add(String.format("%02d:30", hour));
        }
        endTime.getItems().add(String.format("%02d:00", 19));

        form.add(new Label("Heure de début"), 0, 0);
        form.add(startTime, 1, 0);
        form.add(new Label("Heure de fin"), 0, 1);
        form.add(endTime, 1, 1);

        ComboBox<String> type = new ComboBox<>();
        for (String authorizedType : EventService.AUTHORIZED_TYPES) {
            type.getItems().add(authorizedType);
        }
        form.add(new Label("Type"), 0, 2);
        form.add(type, 1, 2);

        TextField nameTextField = new TextField();
        form.add(new Label("Cours"), 0, 3);
        form.add(nameTextField, 1, 3);

        TextField promotionTextField = new TextField();
        form.add(new Label("Promotion"), 0, 4);
        form.add(promotionTextField, 1, 4);

        Label error = new Label();
        error.setTextFill(Paint.valueOf("red"));
        error.setWrapText(true);
        form.add(error, 0, 5, 2, 1);

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(event -> {
            error.setText("");
            if (
                    startTime.getSelectionModel().getSelectedItem() == null
                            || endTime.getSelectionModel().getSelectedItem() == null
                            || null == nameTextField.getText()
                            || null == promotionTextField.getText()
                            || null == type.getValue()
                            || null == room
            ) {
                error.setText("Remplissez tous les champs");
                return;
            }

            LocalDate localDate = datepicker.getValue();
            LocalTime startLocalTime = LocalTime.parse(startTime.getValue());
            LocalTime endLocalTime = LocalTime.parse(endTime.getValue());
            if (endLocalTime.isBefore(startLocalTime)) {
                error.setText("L'heure de fin doit être après l'heure de début.");
                return;
            }
            if (localDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) || localDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                error.setText("Les réservations ne sont pas autorisées pendant le week-end.");
                return;
            }
            if (isEventConflict(localDate, startLocalTime, endLocalTime)) {
                error.setText("Conflit avec un autre évènement");
                return;
            }

            VEvent vEvent = new VEvent();

            ZoneId zoneId = ZoneId.of("Europe/Paris");
            LocalDateTime startLocalDateTime = localDate.atTime(startLocalTime);
            LocalDateTime endLocalDateTime = localDate.atTime(endLocalTime);

            vEvent.setDateStart(Date.from(startLocalDateTime.atZone(zoneId).toInstant()));
            vEvent.setDateEnd(Date.from(endLocalDateTime.atZone(zoneId).toInstant()));

            EventSummary eventSummary = new EventSummary();
            eventSummary.setType(type.getValue());
            eventSummary.setCourse(nameTextField.getText());
            eventSummary.setPromotion(promotionTextField.getText());
            eventSummary.setTeachers(MainApplication.user.getUsername());

            vEvent.setSummary(eventSummary.toString());
            icsManager.addVEventForLocation(rooms.getValue(), vEvent);

            try {
                events = EventListBuilder.buildEvents(icsManager.getLocation(rooms.getValue()));
                calendar.getChildren().set(0, new WeekComponent(EventListBuilder.buildEventListByWeek(events, localDateObjectProperty.getValue()), localDateObjectProperty.getValue()));
            } catch (URISyntaxException | IOException e) {
            }
        });
        form.add(saveButton, 0, 6);
    }

    private boolean isEventConflict(LocalDate date, LocalTime startTime, LocalTime endTime) {
        for (Event event : EventListBuilder.buildEventListByDay(events, date)) {
            LocalDate eventDate = LocalDate.ofInstant(event.getStartTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));
            LocalTime eventStartTime = LocalTime.ofInstant(event.getStartTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));
            LocalTime eventEndTime = LocalTime.ofInstant(event.getEndTime().getValue().toInstant(), ZoneId.of("Europe/Paris"));

            if (eventDate.equals(date)) {
                if (!(endTime.isBefore(eventStartTime) || startTime.isAfter(eventEndTime)) || eventStartTime.equals(LocalTime.of(0, 0))) {
                    return true;
                }
            }
        }
        return false;
    }
}
