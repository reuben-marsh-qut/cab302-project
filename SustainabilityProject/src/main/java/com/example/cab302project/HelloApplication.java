package com.example.cab302project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    public static final int WIDTH = 732*2;
    public static final int HEIGHT = 412*2;

    @Override
    public void start(Stage stage) throws IOException {
        DatabaseConnection.initialise();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("Rooted: Water your mind");
        stage.setScene(scene);
        databasetesting.run();
        stage.show();
        stage.setOnCloseRequest(event -> {
            try {
                DatabaseConnection.getInstance().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
