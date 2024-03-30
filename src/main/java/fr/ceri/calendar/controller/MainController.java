package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private BorderPane main;

    @FXML
    private VBox content;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Button button = new Button("Welcome");
        // TODO: remove debug
        button.setOnAction(event -> MainApplication.setScene("month"));

        content.getChildren().add(button);
    }

    public void setView(Pane scene) {
        content.getChildren().clear();
        content.getChildren().add(scene);
    }
}
