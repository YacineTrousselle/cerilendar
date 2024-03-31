package fr.ceri.calendar.component;

import fr.ceri.calendar.MainApplication;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class AppMenuBar extends MenuBar {

    public AppMenuBar() {
        Menu menu = new Menu("Menu");

        MenuItem settingsMenuItem = new MenuItem("Paramètres");
        settingsMenuItem.setOnAction(event -> MainApplication.setScene("settings"));

        menu.getItems().addAll(settingsMenuItem);

        getMenus().add(menu);
    }

}
