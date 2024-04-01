package fr.ceri.calendar.controller;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.entity.StatusEnum;
import fr.ceri.calendar.entity.User;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.exception.InvalidPasswordException;
import fr.ceri.calendar.exception.UserAlreadyExistException;
import fr.ceri.calendar.exception.UserNotFoundException;
import fr.ceri.calendar.exception.UserSettingsNotFoundException;
import fr.ceri.calendar.service.UserService;
import fr.ceri.calendar.service.UserSettingsService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private final UserService userService = new UserService();
    private final UserSettingsService userSettingsService = new UserSettingsService();

    @FXML
    public PasswordField password;
    @FXML
    public TextField username;
    @FXML
    public Label error;
    @FXML
    public HBox statusRadioGroup;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    public LoginController() throws IOException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username.setDisable(false);
        username.setText("");
        password.setDisable(false);
        password.setText("");

        for (StatusEnum status : StatusEnum.values()) {
            RadioButton radioButton = new RadioButton(status.toFrenchString());
            radioButton.setToggleGroup(toggleGroup);
            radioButton.setUserData(status);
            statusRadioGroup.getChildren().add(radioButton);
        }
        ((RadioButton) statusRadioGroup.getChildren().getFirst()).setSelected(true);
    }

    public void handleLogin(ActionEvent actionEvent) {
        error.setText("");
        username.setDisable(true);
        password.setDisable(true);

        try {
            userService.checkPassword(username.getText(), password.getText());
            onSuccess(userService.findUserByUsername(username.getText()));
            return;
        } catch (UserNotFoundException e) {
            error.setText("L'utilisateur n'existe pas");
        } catch (InvalidPasswordException e) {
            error.setText("Mot de passe incorrect");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        username.setDisable(false);
        password.setDisable(false);
    }

    public void handleSignup(ActionEvent actionEvent) {
        error.setText("");
        username.setDisable(true);
        password.setDisable(true);

        try {
            User newUser = new User(username.getText(), password.getText(), (StatusEnum) toggleGroup.getSelectedToggle().getUserData());
            userService.createUser(newUser);
            onSuccess(newUser);
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UserAlreadyExistException e) {
            error.setText("L'utilisateur existe déjà");
        }

        username.setDisable(false);
        password.setDisable(false);
    }

    private void onSuccess(User user) {
        MainApplication.user = user;
        try {
            MainApplication.userSettings = userSettingsService.findSettingsByUsername(username.getText());
        } catch (Exception e) {
        }
        MainApplication.setScene("day");
    }
}
