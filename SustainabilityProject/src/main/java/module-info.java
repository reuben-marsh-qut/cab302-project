module com.example.cab302project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires java.sql;
    requires password4j;

    opens com.example.cab302project to javafx.fxml;
    exports com.example.cab302project;
    exports com.example.cab302project.controller;
    opens com.example.cab302project.controller to javafx.fxml;
    exports com.example.cab302project.model;
    exports com.example.cab302project.model.enums;
}