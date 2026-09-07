package com.example.cab302project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 732*2, 412*2);
        stage.setTitle("Rooted: Water your mind");
        stage.setScene(scene);
        DatabaseInitialisation.initialise();
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
