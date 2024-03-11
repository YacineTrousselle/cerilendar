module fr.ceri.calendar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.sf.biweekly;

    opens fr.ceri.calendar to javafx.fxml;
    exports fr.ceri.calendar;
    exports fr.ceri.calendar.controller;
    opens fr.ceri.calendar.controller to javafx.fxml;
    exports fr.ceri.calendar.entity;
    opens fr.ceri.calendar.entity to javafx.fxml;
}