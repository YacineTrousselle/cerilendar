package fr.ceri.calendar.controller;

import biweekly.component.VEvent;
import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.service.EventService;
import fr.ceri.calendar.service.IcsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CreateEventController implements Initializable {
    @FXML
    private DatePicker datePicker;

    @FXML
    private TextArea course;

    @FXML
    private TextField teachers;

    @FXML
    private TextField promotion;

    @FXML
    private ChoiceBox<String> type;

    @FXML
    private TextField details;

    @FXML
    private TextField eventLocation;

    @FXML
    private Button save;

    @FXML
    private ChoiceBox<String> startTime;

    @FXML
    private ChoiceBox<String> endTime;

    @FXML
    private Label error;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (int hour = 7; hour < 19; hour++) {
            startTime.getItems().add(String.format("%02d:00", hour));
            endTime.getItems().add(String.format("%02d:00", hour));
            startTime.getItems().add(String.format("%02d:30", hour));
            endTime.getItems().add(String.format("%02d:30", hour));
        }
        endTime.getItems().add(String.format("%02d:00", 19));

        for (String authorizedType : EventService.AUTHORIZED_TYPES) {
            type.getItems().add(authorizedType);
        }

        save.setOnAction(event -> {
            if (checkForm()) {
                EventSummary eventSummary = new EventSummary();
                eventSummary.setCourse(course.getText());
                eventSummary.setPromotion(promotion.getText());
                eventSummary.setTeachers(teachers.getText());
                eventSummary.setType(type.getValue().toString());
                eventSummary.setDetails(details.getText());


                VEvent vEvent = new VEvent();
                LocalDate localDate = datePicker.getValue();

                LocalTime startLocalTime = LocalTime.parse(startTime.getValue());
                LocalTime endLocalTime = LocalTime.parse(endTime.getValue());

                ZoneId zoneId = ZoneId.of("Europe/Paris");
                LocalDateTime startLocalDateTime = localDate.atTime(startLocalTime);
                LocalDateTime endLocalDateTime = localDate.atTime(endLocalTime);

                vEvent.setDateStart(Date.from(startLocalDateTime.atZone(zoneId).toInstant()));
                vEvent.setDateEnd(Date.from(endLocalDateTime.atZone(zoneId).toInstant()));
                vEvent.setSummary(eventSummary.toString());
                vEvent.setLocation(eventLocation.getText());

                IcsManager icsManager = new IcsManager();
                icsManager.addVEventForUser(MainApplication.user.getUsername(), vEvent);
                MainApplication.setScene("user-calendar");
            }
        });
    }

    private boolean checkForm() {
        error.setText("");
        if (datePicker.getValue() == null) {
            error.setText("Date invalide");
            return false;
        }
        if (course.getText().isEmpty()) {
            error.setText("Cours invalide");
            return false;
        }
        Pattern teacherPattern = Pattern.compile(EventService.TEACHERS_REGEX);
        if (teachers.getText().isEmpty() || !teacherPattern.matcher(teachers.getText()).matches()) {
            error.setText("Pas de professeurs/Format invalid");
            return false;
        }
        if (promotion.getText().isEmpty()) {
            error.setText("Promotion invalide");
            return false;
        }
        if (type.getValue() == null) {
            error.setText("Type invalide");
            return false;
        }
        if (null == startTime.getValue() || null == endTime.getValue()) {
            error.setText("Heures invalides");
            return false;
        }
        LocalTime startDateTime = LocalTime.parse(startTime.getValue());
        LocalTime endDateTime = LocalTime.parse(endTime.getValue());
        if (startDateTime.isAfter(endDateTime)) {
            error.setText("Date de fin avant date de début");
            return false;
        }

        return true;
    }
}
