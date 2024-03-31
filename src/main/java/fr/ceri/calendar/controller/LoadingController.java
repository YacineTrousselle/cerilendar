package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.exception.UserNotFoundException;
import fr.ceri.calendar.exception.UserSettingsNotFoundException;
import fr.ceri.calendar.service.UserSettingsService;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    private boolean shouldChooseUserSettings = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            UserSettingsService userSettingsService = new UserSettingsService();
            MainApplication.userSettings = userSettingsService.findSettingsByUsername(MainApplication.user.getUsername());
            MainApplication.setScene("day");
        } catch (IOException | UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UserSettingsNotFoundException e) {
            shouldChooseUserSettings = true;
        }
    }
}
