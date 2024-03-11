package fr.ceri.calendar.exception;

public class UserAlreadyExistException extends Exception {
    public UserAlreadyExistException(String username) {
        super(String.format("User %s arleady exists", username));
    }
}
