package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    @FXML
    private Button homeButton;

    @FXML
    private Button signOutButton;

    @FXML
    protected void onHomeButtonClick() throws IOException {
        Stage stage = (Stage) homeButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("goal-view.fxml")
        );

        Scene scene = new Scene(
                fxmlLoader.load(),
                HelloApplication.WIDTH,
                HelloApplication.HEIGHT
        );

        stage.setScene(scene);
    }

    @FXML
    protected void onSignOutButtonClick() throws IOException {
        UserSession.getInstance().clearUserSession();

        Stage stage = (Stage) signOutButton.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("login-view.fxml")
        );

        Scene scene = new Scene(
                fxmlLoader.load(),
                HelloApplication.WIDTH,
                HelloApplication.HEIGHT
        );

        stage.setScene(scene);
    }
}