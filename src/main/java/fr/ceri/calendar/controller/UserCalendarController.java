package fr.ceri.calendar.controller;

import biweekly.component.VEvent;
import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.DayComponent;
import fr.ceri.calendar.component.MonthComponent;
import fr.ceri.calendar.component.WeekComponent;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.entity.StatusEnum;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UserCalendarController implements Initializable {
    public final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<>(LocalDate.now());

    private List<Event> events = new ArrayList<>();
    private final ToggleGroup viewToggleGroup = new ToggleGroup();

    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        events = this.getEvents();

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> localDateObjectProperty.set(datePicker.getValue()));

        ToggleButton dayToggle = new ToggleButton("Jour");
        ToggleButton weekToggle = new ToggleButton("Semaine");
        ToggleButton monthToggle = new ToggleButton("Mois");
        HBox toggleBox = new HBox();
        toggleBox.setAlignment(Pos.CENTER);
        toggleBox.setSpacing(16);
        toggleBox.getChildren().addAll(dayToggle, weekToggle, monthToggle);

        dayToggle.setToggleGroup(viewToggleGroup);
        weekToggle.setToggleGroup(viewToggleGroup);
        monthToggle.setToggleGroup(viewToggleGroup);

        viewToggleGroup.selectToggle(dayToggle);

        VBox header = new VBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(8);
        header.getChildren().addAll(datePicker, toggleBox);
        borderPane.setTop(header);

        localDateObjectProperty.addListener(this::handleDateChanged);
        viewToggleGroup.selectedToggleProperty().addListener(this::handleSwitchView);

        borderPane.setCenter(new DayComponent(EventListBuilder.buildEventListByDay(events, localDateObjectProperty.getValue())));
    }

    public void handleSwitchView(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
        if (newValue == null) {
            return;
        }
        switch (((ToggleButton) newValue).getText()) {
            case "Jour":
                borderPane.setCenter(new DayComponent(EventListBuilder.buildEventListByDay(events, localDateObjectProperty.getValue())));
                break;
            case "Semaine":
                borderPane.setCenter(new WeekComponent(EventListBuilder.buildEventListByWeek(events, localDateObjectProperty.getValue()), localDateObjectProperty.getValue()));
                break;
            case "Mois":
                borderPane.setCenter(
                        new MonthComponent(
                                EventListBuilder.buildEventListByMonth(events, localDateObjectProperty.getValue()), localDateObjectProperty.getValue(),
                                localDateObjectProperty, viewToggleGroup
                        )
                );
                break;
        }
    }

    public void handleDateChanged(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
        switch (((ToggleButton) (viewToggleGroup.getSelectedToggle())).getText()) {
            case "Jour":
                borderPane.setCenter(new DayComponent(EventListBuilder.buildEventListByDay(events, newValue)));
                break;
            case "Semaine":
                borderPane.setCenter(new WeekComponent(EventListBuilder.buildEventListByWeek(events, newValue), newValue));
                break;
            case "Mois":
                borderPane.setCenter(
                        new MonthComponent(
                                EventListBuilder.buildEventListByMonth(events, newValue), newValue,
                                localDateObjectProperty, viewToggleGroup
                        )
                );
                break;
        }
    }

    private List<Event> getEvents() {
        IcsManager icsManager = new IcsManager();

        List<VEvent> vEvents = new ArrayList<>();
        List<VEvent> userVEvents = new ArrayList<>();
        try {
            if (MainApplication.user.getStatus() == StatusEnum.STUDENT) {
                vEvents = icsManager.getFormation(MainApplication.userSettings.getBaseEdt());
            } else {
                vEvents = icsManager.getTeacher(MainApplication.userSettings.getBaseEdt());
            }
        } catch (URISyntaxException | IOException ignored) {
        }
        try {
            userVEvents = icsManager.getUser(MainApplication.user.getUsername());
        } catch (URISyntaxException | IOException ignored) {
        }
        vEvents.addAll(userVEvents);

        events = EventListBuilder.buildEvents(vEvents);

        return events;
    }
}
