package fr.ceri.calendar.component;

import fr.ceri.calendar.MainApplication;
import fr.ceri.calendar.entity.StatusEnum;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.service.IcsManager;
import fr.ceri.calendar.service.IcsParser;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static fr.ceri.calendar.MainApplication.ICS_FOLDER;

public class DefaultCalendarSelection extends VBox {

    private ComboBox<String> choiceBox = new ComboBox<>();

    public DefaultCalendarSelection(UserSettings userSettings) {
        setAlignment(Pos.CENTER);
        setMaxWidth(300);
        setSpacing(2.0);
        IcsManager icsManager = new IcsManager();
        List<String> choices;
        try {
            choices = MainApplication.user.getStatus() == StatusEnum.TEACHER ? icsManager.getTeachers() : icsManager.getFormations();
        } catch (IOException e) {
            choices = new ArrayList<>();
        }

        for (String choice : choices) {
            choiceBox.getItems().add(choice);
            if (null != userSettings && Objects.equals(choice, userSettings.getBaseEdt())) {
                choiceBox.getSelectionModel().select(choice);
            }
        }

        TextField urlTextField = new TextField();
        TextField nameTextField = new TextField();
        Label error = new Label();
        error.setTextFill(Color.RED);
        Button saveNewIcs = getSaveButton(error, urlTextField, nameTextField);

        getChildren().addAll(choiceBox, new Label("Lien"), urlTextField, new Label("Nom"), nameTextField, saveNewIcs, error);
    }

    private Button getSaveButton(Label error, TextField urlTextField, TextField nameTextField) {
        Button saveNewIcs = new Button("Récupérer");
        saveNewIcs.setOnAction(event -> {
            try {
                error.setText("");
                if (!urlTextField.getText().isEmpty() && !nameTextField.getText().isEmpty()) {
                    IcsParser.saveIcsFromUrl(
                            urlTextField.getText(),
                            MainApplication.user.getStatus() == StatusEnum.TEACHER
                                    ? Path.of(ICS_FOLDER, IcsManager.TEACHERS, nameTextField.getText() + ".ics")
                                    : Path.of(ICS_FOLDER, IcsManager.FORMATIONS, nameTextField.getText() + ".ics")
                    );
                    choiceBox.getItems().add(nameTextField.getText());
                    choiceBox.getSelectionModel().select(nameTextField.getText());
                }
            } catch (IOException e) {
                error.setText("Echec lors de la récupération de l'emploi du temps");
                System.err.println(e.getMessage());
            }
        });
        return saveNewIcs;
    }

    public ComboBox<String> getChoiceBox() {
        return choiceBox;
    }
}
