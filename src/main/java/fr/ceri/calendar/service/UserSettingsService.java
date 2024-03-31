package fr.ceri.calendar.service;

import fr.ceri.calendar.entity.User;
import fr.ceri.calendar.entity.UserSettings;
import fr.ceri.calendar.exception.UserNotFoundException;
import fr.ceri.calendar.exception.UserSettingsNotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class UserSettingsService {

    private static final String FILEPATH = "./user-settings.csv";
    private final UserService userService;

    public UserSettingsService() throws IOException {
        userService = new UserService();
        Path path = Paths.get(FILEPATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public UserSettings findSettingsByUsername(String username) throws UserNotFoundException, IOException, UserSettingsNotFoundException {
        List<String> userSettings = Files.readAllLines(Path.of(FILEPATH));

        for (String line : userSettings) {
            if (line.split(";")[0].equals(username)) {
                return getUserSettings(line);
            }
        }

        throw new UserSettingsNotFoundException();
    }

    public void saveSettings(UserSettings userSettings) throws IOException {
        List<String> userSettingsLines = Files.readAllLines(Path.of(FILEPATH));

        for (int i = 0; i < userSettingsLines.size(); i++) {
            String line = userSettingsLines.get(i);
            if (line.split(";")[0].equals(userSettings.getUser().getUsername())) {
                userSettingsLines.set(i, userSettings.toString());
                Files.write(Path.of(FILEPATH), userSettingsLines);
                return;
            }
        }

        Files.writeString(Path.of(FILEPATH), userSettings.toString(), CREATE, APPEND);

    }

    private UserSettings getUserSettings(String line) throws UserNotFoundException, IOException {
        String[] userSettingsInfo = line.split(";");
        User user = userService.findUserByUsername(userSettingsInfo[0]);

        return new UserSettings(user, userSettingsInfo);
    }
}
