package fr.ceri.calendar.entity;

import org.mindrot.jbcrypt.BCrypt;

public class User {

    private String username;
    private String password;
    private ColorModeEnum colorMode;
    private StatusEnum status;

    public User(String username, String password, ColorModeEnum colorMode, StatusEnum status) {
        this.username = username;
        this.password = password;
        this.colorMode = colorMode;
        this.status = status;
    }

    public User(String[] userInfo) {
        username = userInfo[0];
        password = userInfo[1];
        colorMode = ColorModeEnum.valueOf(userInfo[2]);
        status = StatusEnum.valueOf(userInfo[3]);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ColorModeEnum getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorModeEnum colorMode) {
        this.colorMode = colorMode;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public void encodePassword() {
        password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public String toString() {
        return String.format("%s;%s;%s;%s\n", username, password, colorMode, status);
    }
}
