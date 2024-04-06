package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.AppMenuBar;
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

        main.setTop(new AppMenuBar());

        Button button = new Button("Welcome");
        button.setOnAction(event -> MainApplication.setScene("login"));

        content.getChildren().add(button);
    }

    public void setView(Pane scene) {
        main.setTop(new AppMenuBar());

        content.getChildren().clear();
        content.getChildren().add(scene);
    }

    public void setClassMain(String classname) {
        content.getStyleClass().clear();
        content.getStyleClass().add(classname);
    }
}
