package fr.ceri.calendar.entity;

public class UserSettings {
    private final User user;
    private final String baseEdt;
    private ColorModeEnum colorMode;

    public UserSettings(User user, ColorModeEnum colorMode, String baseEdt) {
        this.user = user;
        this.colorMode = colorMode;
        this.baseEdt = baseEdt;
    }

    public UserSettings(User user, String[] userSettingsInfo) {
        this.user = user;
        this.colorMode = ColorModeEnum.valueOf(userSettingsInfo[1]);
        this.baseEdt = userSettingsInfo[2];
    }

    public User getUser() {
        return user;
    }

    public String getBaseEdt() {
        return baseEdt;
    }

    public ColorModeEnum getColorMode() {
        return colorMode;
    }

    public void setColorMode(ColorModeEnum colorMode) {
        this.colorMode = colorMode;
    }

    @Override
    public String toString() {
        return String.format("%s%s%s\n", user.getUsername(), colorMode, baseEdt);
    }
}
