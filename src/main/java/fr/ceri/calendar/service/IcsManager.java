package fr.ceri.calendar.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static fr.ceri.calendar.MainApplication.ICS_FOLDER;

public class IcsManager {
    public static final String FORMATIONS = "formations";
    public static final String TEACHERS = "teachers";
    public static final String LOCATION = "locations";
    public static final String USERS = "users";

    public IcsManager() {
        if (Files.notExists(Path.of(ICS_FOLDER, FORMATIONS))) {
            Path.of(ICS_FOLDER, FORMATIONS).toFile().mkdirs();
        }
        if (Files.notExists(Path.of(ICS_FOLDER, TEACHERS))) {
            Path.of(ICS_FOLDER, TEACHERS).toFile().mkdirs();
        }
        if (Files.notExists(Path.of(ICS_FOLDER, LOCATION))) {
            Path.of(ICS_FOLDER, LOCATION).toFile().mkdirs();
        }
        if (Files.notExists(Path.of(ICS_FOLDER, USERS))) {
            Path.of(ICS_FOLDER, USERS).toFile().mkdirs();
        }
    }

    public void addVEventForUser(String username, VEvent vEvent) {
        Path filepath = Path.of(ICS_FOLDER, USERS, username.endsWith(".ics") ? username : username + ".ics");

        try {
            if (!filepath.toFile().exists()) {
                ICalendar iCalendar = new ICalendar();
                iCalendar.addEvent(vEvent);
                Files.createFile(filepath);
                Biweekly.write(iCalendar).go(filepath.toFile());
                return;
            }

            ICalendar iCalendar = Biweekly.parse(filepath.toFile()).first();
            iCalendar.addEvent(vEvent);
            Biweekly.write(iCalendar).go(filepath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<VEvent> getFormation(String name) throws URISyntaxException, IOException {
        Path filepath = Path.of(ICS_FOLDER, FORMATIONS, name.endsWith(".ics") ? name : name + ".ics");

        return IcsParser.parseIcsFile(filepath);
    }

    public List<VEvent> getTeacher(String name) throws URISyntaxException, IOException {
        Path filepath = Path.of(ICS_FOLDER, TEACHERS, name.endsWith(".ics") ? name : name + ".ics");

        return IcsParser.parseIcsFile(filepath);
    }

    public List<VEvent> getLocation(String name) throws URISyntaxException, IOException {
        Path filepath = Path.of(ICS_FOLDER, LOCATION, name.endsWith(".ics") ? name : name + ".ics");

        return IcsParser.parseIcsFile(filepath);
    }

    public List<VEvent> getUser(String name) throws URISyntaxException, IOException {
        Path filepath = Path.of(ICS_FOLDER, USERS, name.endsWith(".ics") ? name : name + ".ics");

        return IcsParser.parseIcsFile(filepath);
    }

    public List<String> getFormations() throws IOException {
        Path directory = Path.of(ICS_FOLDER, FORMATIONS);

        return getFilenameInDirectory(directory);
    }

    public List<String> getTeachers() throws IOException {
        Path directory = Path.of(ICS_FOLDER, TEACHERS);

        return getFilenameInDirectory(directory);
    }

    public List<String> getLocations() throws IOException {
        Path directory = Path.of(ICS_FOLDER, LOCATION);

        return getFilenameInDirectory(directory);
    }

    public List<String> getUsers() throws IOException {
        Path directory = Path.of(ICS_FOLDER, USERS);

        return getFilenameInDirectory(directory);
    }

    private List<String> getFilenameInDirectory(Path directory) throws IOException {
        return Files
                .list(directory)
                .map(Path::getFileName)
                .map(Objects::toString)
                .map(filename -> {
                    int lastDotIndex = filename.lastIndexOf('.');
                    return (lastDotIndex == -1) ? filename : filename.substring(0, lastDotIndex);
                })
                .toList();

    }
}
