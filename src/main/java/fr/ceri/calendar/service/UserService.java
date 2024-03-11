package fr.ceri.calendar.service;

import fr.ceri.calendar.entity.User;
import fr.ceri.calendar.exception.InvalidPasswordException;
import fr.ceri.calendar.exception.UserAlreadyExistException;
import fr.ceri.calendar.exception.UserNotFoundException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class UserService {

    private static final String FILEPATH = "./users.csv";

    public UserService() throws IOException {
        Path path = Paths.get(FILEPATH);
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public List<User> getUsers() throws IOException {
        List<String> users = Files.readAllLines(Path.of(FILEPATH));

        return users.stream().filter(user -> !user.isEmpty()).map(this::stringToUser).toList();
    }

    public User findUserByUsername(String username) throws IOException, UserNotFoundException {
        return getUsers().stream().filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(UserNotFoundException::new);
    }

    public void createUser(User user) throws IOException, UserAlreadyExistException {
        user.encodePassword();
        if (getUsers().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            throw new UserAlreadyExistException(user.getUsername());
        }

        Files.writeString(Path.of(FILEPATH), user.toString(), CREATE, APPEND);
    }

    public void checkPassword(String username, String password) throws InvalidPasswordException, UserNotFoundException {
        try {
            User user = findUserByUsername(username);
            if (!BCrypt.checkpw(password, user.getPassword())) {
                throw new InvalidPasswordException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private User stringToUser(String userLine) {
        String[] userInfo = userLine.split(";");

        return new User(userInfo);
    }
}
