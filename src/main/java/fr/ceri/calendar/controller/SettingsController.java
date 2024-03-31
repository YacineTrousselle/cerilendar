package fr.ceri.calendar.controller;

import fr.ceri.calendar.entity.ColorModeEnum;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vBox.setPadding(new Insets(12));

        Label title = new Label("Paramètres");
        title.setFont(new Font(40));

        HBox radios = new HBox();
        radios.setAlignment(Pos.CENTER);
        radios.setSpacing(32.0);

        ToggleGroup colorMode = new ToggleGroup();
        RadioButton darkRadio = new RadioButton("Mode sombre");
        RadioButton lightRadio = new RadioButton("Mode clair");
        darkRadio.setToggleGroup(colorMode);
        lightRadio.setToggleGroup(colorMode);
        darkRadio.setUserData(ColorModeEnum.DARKMODE);
        lightRadio.setUserData(ColorModeEnum.LIGHTMODE);

        radios.getChildren().addAll(darkRadio, lightRadio);

        BorderPane defaultIcs = new BorderPane();
        Label defaultIcsLabel = new Label("Emploi du temps par défaut");
        defaultIcsLabel.setFont(new Font(24));
        BorderPane.setAlignment(defaultIcsLabel, Pos.CENTER);
        defaultIcs.setTop(defaultIcsLabel);

        // TODO: selection edt

        Button saveButton = new Button("Enregistrer");

        vBox.getChildren().addAll(title, radios, defaultIcs, saveButton);
    }

}
