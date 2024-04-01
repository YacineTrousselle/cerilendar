package fr.ceri.calendar.controller;

import biweekly.component.VEvent;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Location;
import biweekly.property.Summary;
import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.EventSummary;
import fr.ceri.calendar.service.EventService;
import fr.ceri.calendar.service.IcsManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (String authorizedType : EventService.AUTHORIZED_TYPES) {
            type.getItems().add(authorizedType);
        }

        save.setOnAction(event -> {
            if (checkForm()) {
                EventSummary eventSummary = new EventSummary();
                eventSummary.setCourse(course.getText());
                eventSummary.setPromotion(promotion.getText());
                eventSummary.setTeachers(teachers.getText());
                eventSummary.setType(type.getValue());
                eventSummary.setDetails(details.getText());

                VEvent vEvent = new VEvent();
                LocalDate localDate = datePicker.getValue();
                // TODO local date to date
                vEvent.setDateStart();
                vEvent.setDateEnd();
                vEvent.setSummary(eventSummary.toString());
                vEvent.setLocation(eventLocation.getText());

                IcsManager icsManager = new IcsManager();
                icsManager.addVEventForUser(MainApplication.user.getUsername(), vEvent);

            }
        });
    }

    private boolean checkForm() {
        if (datePicker.getValue() == null) {
            return false;
        }
        if (course.getText().isEmpty()) {
            return false;
        }
        Pattern teacherPattern = Pattern.compile(EventService.TEACHERS_REGEX);
        if (teachers.getText().isEmpty() || teacherPattern.matcher(teachers.getText()).matches()) {
            return false;
        }
        if (promotion.getText().isEmpty()) {
            return false;
        }

        return true;
    }
}
