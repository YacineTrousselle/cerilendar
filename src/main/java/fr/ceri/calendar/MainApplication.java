package fr.ceri.calendar;

import fr.ceri.calendar.controller.MainController;
import fr.ceri.calendar.entity.ColorModeEnum;
import fr.ceri.calendar.entity.StatusEnum;
import fr.ceri.calendar.entity.User;
import fr.ceri.calendar.exception.InvalidPasswordException;
import fr.ceri.calendar.exception.UserAlreadyExistException;
import fr.ceri.calendar.exception.UserNotFoundException;
import fr.ceri.calendar.service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class MainApplication extends Application {
    private static MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("main.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        mainController = fxmlLoader.getController();

        stage.setTitle("JavaFX forever");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
//        launch();

        try {
            UserService userService = new UserService();

            User userTest = new User("Jean", "abc", ColorModeEnum.LIGHTMODE, StatusEnum.STUDENT);
            userService.createUser(userTest);

            System.out.println(Arrays.toString(userService.getUsers().toArray()));

            userService.checkPassword(userTest.getUsername(), "abc");

        } catch (IOException e) {
            System.err.println(e.getMessage() + ": unable to create file");
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvalidPasswordException e) {
            throw new RuntimeException(e);
        } catch (UserAlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setScene(String scene) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource(String.format("%s.fxml", scene))
            );
            mainController.setView(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
