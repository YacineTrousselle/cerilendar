package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.component.DefaultCalendarSelection;
import fr.ceri.calendar.entity.ColorModeEnum;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.service.UserSettingsService;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    private UserSettingsService userSettingsService;

    {
        try {
            userSettingsService = new UserSettingsService();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserSettings userSettings = null;
        try {
            userSettings = userSettingsService.findSettingsByUsername(MainApplication.user.getUsername());
        } catch (Exception e) {
        }
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

        if (null != userSettings) {
            if (userSettings.getColorMode() == ColorModeEnum.LIGHTMODE) {
                lightRadio.setSelected(true);
            } else {
                darkRadio.setSelected(true);
            }
        }

        radios.getChildren().addAll(darkRadio, lightRadio);

        BorderPane defaultIcs = new BorderPane();
        Label defaultIcsLabel = new Label("Emploi du temps par défaut");
        defaultIcsLabel.setFont(new Font(24));
        BorderPane.setAlignment(defaultIcsLabel, Pos.CENTER);
        defaultIcs.setTop(defaultIcsLabel);


        DefaultCalendarSelection defaultCalendarSelection = new DefaultCalendarSelection(userSettings);

        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(event -> {
            if (null != colorMode.getSelectedToggle() && null != defaultCalendarSelection.getChoiceBox().getSelectionModel().getSelectedItem()) {
                UserSettings newUserSettings = new UserSettings(
                        MainApplication.user,
                        (ColorModeEnum) colorMode.getSelectedToggle().getUserData(),
                        defaultCalendarSelection.getChoiceBox().getSelectionModel().getSelectedItem()
                );
                try {
                    userSettingsService.saveSettings(newUserSettings);
                    MainApplication.userSettings = newUserSettings;
                    MainApplication.setScene("day");
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        });

        vBox.getChildren().addAll(title, radios, defaultIcs, defaultCalendarSelection, saveButton);
    }

}
