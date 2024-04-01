package fr.ceri.calendar;

import fr.ceri.calendar.controller.MainController;
import fr.ceri.calendar.entity.User;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.service.IcsManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class MainApplication extends Application {
    private static MainController mainController;
    public static User user = null;
    public static UserSettings userSettings = null;

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
        launch();
    }

    public static void setScene(String scene) {
        try {
            if (!Objects.equals(scene, "login") && userSettings == null) {
                scene = "settings";
            }
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainApplication.class.getResource(String.format("%s.fxml", scene))
            );
            mainController.setView(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
