package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.DayComponent;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DayController implements Initializable {
    private List<Event> eventList;

    @FXML
    private VBox vBox;

    @FXML
    private Pane eventContainer;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button dailyButton = new Button("Jour");
        Button weeklyButton = new Button("Semaine");
        Button monthlyButton = new Button("Mois");

        weeklyButton.setOnAction(event -> MainApplication.setScene("week"));
        monthlyButton.setOnAction(event -> MainApplication.setScene("month"));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(dailyButton, weeklyButton, monthlyButton);
        vBox.getChildren().addFirst(hBox);

        try {
            eventList = EventListBuilder.buildEvents(IcsParser.parseIcsFile("m1-alt"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        MainController.localDateObjectProperty.addListener((observable, oldValue, newValue) ->
                buildGrid(EventListBuilder.buildEventListByDay(eventList, newValue))
        );

        datePicker.setValue(MainController.localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> MainController.localDateObjectProperty.set(datePicker.getValue()));

        buildGrid(EventListBuilder.buildEventListByDay(eventList, MainController.localDateObjectProperty.getValue()));
    }

    private void buildGrid(List<Event> events) {
        eventContainer.getChildren().clear();
        eventContainer.getChildren().add(new DayComponent(events));
    }
}
