package com.example.cab302project.controller;

import com.example.cab302project.model.MockUserDAO;
import com.example.cab302project.model.User;
import com.example.cab302project.model.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.cab302project.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UserManager userManager;

    public LoginController() {

        MockUserDAO userDAO = new MockUserDAO();

        userDAO.addUser(
                new User(
                        1,
                        "test@example.com",
                        "password123"
                )
        );

        userManager = new UserManager(userDAO);
    }

    @FXML
    private void handleLogin() {

        User user = userManager.login(
                emailField.getText(),
                passwordField.getText()
        );

        if (user == null) {
            showError("Incorrect email or password.");
            return;
        }

        hideError();

        System.out.println(
                "Login successful. User ID: " + user.getId()
        );
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError() {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}