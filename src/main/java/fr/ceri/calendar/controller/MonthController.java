package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.MonthGridPane;
import fr.ceri.calendar.entity.Event;
import fr.ceri.calendar.service.EventListBuilder;
import fr.ceri.calendar.service.IcsParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.ResourceBundle;

public class MonthController implements Initializable {
    private List<Event> eventList;

    @FXML
    private VBox vBox;

    @FXML
    private Pane grid;

    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button dailyButton = new Button("Jour");
        Button weeklyButton = new Button("Semaine");
        Button monthlyButton = new Button("Mois");

        dailyButton.setOnAction(event -> MainApplication.setScene("day"));
        weeklyButton.setOnAction(event -> MainApplication.setScene("week"));

        HBox hBox = new HBox();
        hBox.getChildren().addAll(dailyButton, weeklyButton, monthlyButton);
        vBox.getChildren().addFirst(hBox);

        try {
            eventList = EventListBuilder.buildEvents(IcsParser.parseIcsFile("m1-alt"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        MainController.localDateObjectProperty.addListener((observable, oldValue, newValue) -> {
            if (null == oldValue || oldValue.getMonth() != newValue.getMonth()) {
                buildGrid(
                        EventListBuilder.buildEventListByMonth(eventList, newValue)
                );
            }
        });
        MainController.localDateObjectProperty.setValue(LocalDate.now());

        datePicker.setValue(MainController.localDateObjectProperty.getValue());
        datePicker.setOnAction(event -> MainController.localDateObjectProperty.set(datePicker.getValue()));

        buildGrid(EventListBuilder.buildEventListByMonth(eventList, MainController.localDateObjectProperty.getValue()));
    }

    public void buildGrid(List<Event> events) {
        grid.getChildren().clear();
        MonthGridPane monthGridPane = new MonthGridPane(MainController.localDateObjectProperty.getValue());

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
