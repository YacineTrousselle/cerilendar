package fr.ceri.calendar.component;

import fr.ceri.calendar.MainApplication;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewSwitchComponent extends HBox implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Button dailyButton = new Button("Jour");
        Button weeklyButton = new Button("Semaine");
        Button monthlyButton = new Button("Mois");

        dailyButton.setOnAction(event -> MainApplication.setScene("day"));
        weeklyButton.setOnAction(event -> MainApplication.setScene("week"));
        monthlyButton.setOnAction(event -> MainApplication.setScene("month"));
        getChildren().addAll(dailyButton, weeklyButton, monthlyButton);
    }

}
