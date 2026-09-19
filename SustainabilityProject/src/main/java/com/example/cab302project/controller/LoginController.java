package com.example.cab302project.controller;

import com.example.cab302project.HelloApplication;
import com.example.cab302project.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        // TODO: Replace MockUserDAO with database-backed UserDAO.
        IUserDAO userDAO = new UserDAO();

        userDAO.createUser("test@example.com",
                        "password123",
                            4000
        );

        userDAO.createUser(
                        "second@example.com",
                        "differentPassword",
                        4000

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

        try {
            openHomePage(user);
        } catch (IOException e) {
            showError("Unable to open the home page.");
            e.printStackTrace();
        }
    }

    private void openHomePage(User user) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("goal-view.fxml")
            );

            UserSession.getInstance().setUser(user);

            Parent root = loader.load();

            emailField.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Unable to open goals page.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateAccount() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    HelloApplication.class.getResource("register-view.fxml")
            );

            Parent root = loader.load();

            emailField.getScene().setRoot(root);
        } catch (IOException e) {
            showError("Unable to open registration.");
            e.printStackTrace();
        }
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